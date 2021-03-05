package org.firstinspires.ftc.teamcode.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.BaseOpMode;

/**
 *
 * Handles control of a mecanum drive base with field centric control
 * Formulas Yoinked From: https://www.chiefdelphi.com/uploads/default/original/3X/9/7/97ee2e1cf47de3259638efca7f14c838bc702875.pdf
 *
 * @author Will Richards
 */
public class MecanumDrive {

    private final double TURN_SENSITIVITY = .8;

    // The current drive base speed multiplier
    private double speedMultiplier = 1;

    // 0 - FrontLeft, 1 - FrontRight, 2 - BackLeft, 3 - BackRight
    private DcMotor[] driveMotors;

    /**
     * Construct the mecanum drive class
     * @param driveMotors array of drive motors
     */
    public MecanumDrive(DcMotor[] driveMotors){
        this.driveMotors = driveMotors;
    }

    /**
     * Non-Field centric drive is where all directions are relative to the robot as opposed to the driver, this means that when the robot's direction is
     * inverted left becomes right and vice-versa. This is annoying and thus field centric drive makes all directions relative to the driver and thus right will always be right and vice-versa
     */
    public void nonFieldCentricControl(double FWD, double STR, double RCW){
        STR = -STR;
        RCW = -RCW * TURN_SENSITIVITY;
        double[] motorValues = normalizeWheelSpeeds(FWD, STR, RCW);

        // Loop through the motors values and supply the correct power to the correct motor
        for (int i=0; i<motorValues.length;i++){
            driveMotors[i].setPower(motorValues[i]*speedMultiplier);
        }

    }

    /**
     * Allows for field centric driving relative to where the robot was initialized on
     * @param FWD the forward joystick input
     * @param STR the strafe joystick input
     * @param RCW the rotational joystick input
     * @param theta the current gyro angle
     */
    public void fieldCentricControl(double FWD, double STR, double RCW, double theta){
        // Temp variable so the true value of FWD can still be used in the calculations
        double temp;
        // Convert normal joystick inputs to ones that match the values we need
        STR = -STR;
        RCW = -RCW * TURN_SENSITIVITY;

        // This math assumes our gyro updates counter clockwise
        temp = FWD*Math.cos(theta) - STR*Math.sin(theta);
        STR = FWD*Math.sin(theta) + STR*Math.cos(theta);
        FWD = temp;

        // Calculate the wheel speed vectors and normalize all of them to be less than one
        double[] motorValues = normalizeWheelSpeeds(FWD, STR, RCW);

        // Loop through the motors values and supply the correct power to the correct motor
        for (int i=0; i<motorValues.length;i++){
            driveMotors[i].setPower(motorValues[i]*speedMultiplier);
        }
    }

    /**
     * Functionality to easily change the speed multiplier to the requested value
     */
    public void toggleSlowMode(){
        if(speedMultiplier == 1)
            speedMultiplier = 0.5;
        else
            speedMultiplier = 1;
    }

    /**
     * Normalize the wheel speeds to be less than 1 but scaled such that the differences remain
     * @return list of wheel speeds to feed into the motors
     */
    public double[] normalizeWheelSpeeds(double FWD, double STR, double RCW){
        double front_left = FWD + RCW + STR;
        double front_right = FWD - RCW - STR;
        double back_left =  (FWD + RCW - STR)*.75;
        double back_right = (FWD - RCW + STR)*.75;

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

        return new double[]{front_left, front_right, back_left, back_right};
    }
}
