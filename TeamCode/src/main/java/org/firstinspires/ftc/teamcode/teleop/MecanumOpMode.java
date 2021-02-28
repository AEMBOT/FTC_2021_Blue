package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.BaseOpMode;

/**
 * This class will command a basic mecanum drive, the code is a bit gross mainly cause segmenting it too much
 * would result in too much abstraction and the thus less readability
 *
 * @author Will Richards
 */
@TeleOp(name = "MecanumOpMode", group = "Test")
public class MecanumOpMode extends BaseOpMode {

    /**
     * THIS IS A TUNING CONSTANT
     * Increase very slowly (DO NOT EXCEED 1)
     * This adjusts the rotate sensitivity
     * Begin adjusting after strafing works
     */
    private final double turnSensitivity = 0;

    // Variables to store controller values to be used in calculations
    private double forwardVector;
    private double rightVector;
    private double clockwiseRotation;

    @Override
    public void loop() {
        // Invert the Y axis cause it reads them inverted by default
        forwardVector = -gamepad1.left_stick_y; // Forward
        rightVector = gamepad1.left_stick_x; // Strafing

        // Robot rotation is controlled by the right stick which is scaled by the turnSensitivity constant
        clockwiseRotation = turnSensitivity * gamepad1.right_stick_x;

        // Basic mecanum drive
        nonFieldCentricControl();

        // AFTER NON FIELD CENTRIC is working try to get field centric to work and read the comments for each of the methods
        //fieldCentricControl(true);
    }

    /**
     * Non-Field centric drive is where all directions are relative to the robot as opposed to the driver, this means that when the robot's direction is
     * inverted left becomes right and vice-versa. This is annoying and thus field centric drive makes all directions relative to the driver and thus right will always be right and vice-versa
     * GET WORKING BEFORE trying fieldCentric
     */
    public void nonFieldCentricControl(){
        double[] motorValues = normalizeWheelSpeeds();

        // Set the motors to run at the required powers
        super.frontLeft.setPower(motorValues[0]);
        super.frontRight.setPower(motorValues[1]);
        super.backLeft.setPower(motorValues[2]);
        super.backRight.setPower(motorValues[3]);
    }

    /**
     * Allows for field centric driving relative to where the robot was powered on
     * @param gyroClockwise whether or not the gyro values are clockwise if it is going the opposite direction of what you want switch this value
     */
    public void fieldCentricControl(boolean gyroClockwise){
        double temp;
        double theta = super.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
        if(gyroClockwise){
            temp = forwardVector*Math.cos(theta) - rightVector*Math.sin(theta);
            rightVector = -forwardVector*Math.sin(theta) + rightVector*Math.cos(theta);
            forwardVector = temp;
        }else{
            temp = forwardVector*Math.cos(theta) - rightVector*Math.sin(theta);
            rightVector = forwardVector*Math.sin(theta) + rightVector*Math.cos(theta);
            forwardVector = temp;
        }

        double[] motorValues = normalizeWheelSpeeds();

        // Set the motors to run at the required powers
        super.frontLeft.setPower(motorValues[0]);
        super.frontRight.setPower(motorValues[1]);
        super.backLeft.setPower(motorValues[2]);
        super.backRight.setPower(motorValues[3]);
    }

    /**
     * Normalize the wheel speeds to be less than 1 but scaled such that the differences remain
     * @return list of wheel speeds to feed into the motors
     */
    public double[] normalizeWheelSpeeds(){
        double front_left = forwardVector + clockwiseRotation + rightVector;
        double front_right = forwardVector - clockwiseRotation - rightVector;
        double back_left =  forwardVector + clockwiseRotation - rightVector;
        double back_right = forwardVector - clockwiseRotation + rightVector;

        double max = Math.abs(front_left);
        if(Math.abs(front_right)>max) max = Math.abs(front_right);
        if(Math.abs(back_left)>max) max = Math.abs(back_left);
        if(Math.abs(back_right)>max) max = Math.abs(back_right);

        // Reduce the max value to less than 1
        if(max> 1){
            front_left/=max;
            front_right/=max;
            back_left/=max;
            back_right/=max;
        }

        double[] normalizedSpeedArray = {front_left, front_right, back_left, back_right};
        return normalizedSpeedArray;
    }
}
