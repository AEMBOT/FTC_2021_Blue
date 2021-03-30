package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 *
 * Handles control of a mecanum drive base with field centric control
 * Formulas Yoinked From: https://www.chiefdelphi.com/uploads/default/original/3X/9/7/97ee2e1cf47de3259638efca7f14c838bc702875.pdf
 *
 * @author Will Richards
 */
public class MecanumDrive {

    private final double TURN_SENSITIVITY = .8;
    private final double HEADING_ADJUST_SCALAR = 20;

    // The current drive base speed multiplier
    private double speedMultiplier = 1;

    // The current back wheel bias
    public double backBias;

    // 0 - FrontLeft, 1 - FrontRight, 2 - BackLeft, 3 - BackRight
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;

    private DcMotor[] driveMotors;

    private double prevHeadingRadians = 0;
    private double currentHeadingRadians = 0;
    /**
     * Construct the mecanum drive class
     *
     * @param hardwareMap a reference to the hardware map from the OpMode
     */
    public MecanumDrive(HardwareMap hardwareMap) {

        // Init all devices to corresponding mapping names
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeft = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRight = hardwareMap.get(DcMotor.class, "backRightMotor");

        driveMotors = new DcMotor[4];
        driveMotors[0] = frontLeft;
        driveMotors[1] = frontRight;
        driveMotors[2] = backLeft;
        driveMotors[3] = backRight;


        // Invert the right side
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    /**
     * Non-Field centric drive is where all directions are relative to the robot as opposed to the driver, this means that when the robot's direction is
     * inverted left becomes right and vice-versa. This is annoying and thus field centric drive makes all directions relative to the driver and thus right will always be right and vice-versa
     */
    public void nonFieldCentricControl(double imu, double FWD, double STR, double RCW) {
        currentHeadingRadians = imu;

        double rotateTweak = 0;

        // Uses a threshold to make sure this still evaluates correctly if the joystick is like 0.0001
        if((RCW < 0.05 && RCW > -0.05) && (STR + FWD) != 0) {

            double headingError = (currentHeadingRadians - prevHeadingRadians);

            /**
             * NOTE: Please Switch to using PID at some point soon, this is a bang-bang approach and can/will result in overshooting in most instances
             * I have migrated a usable PID library over from our FRC code
             */
            if(headingError != 0) {

                if (headingError > Math.PI) {
                    headingError -= (float)Math.PI;
                } else if (headingError < -Math.PI) {
                    headingError += (float)Math.PI;
                }

                rotateTweak = HEADING_ADJUST_SCALAR * headingError;
            }
        }

        STR = -STR;
        RCW = -(RCW + rotateTweak) * TURN_SENSITIVITY;
        double[] motorValues = normalizeWheelSpeeds(FWD, STR, RCW);

        // Loop through the motors values and supply the correct power to the correct motor
        for (int i = 0; i < motorValues.length; i++) {
            driveMotors[i].setPower(motorValues[i] * speedMultiplier);
        }

        prevHeadingRadians = currentHeadingRadians;
    }

    /**
     * Allows for field centric driving relative to where the robot was initialized on
     *
     * @param FWD   the forward joystick input
     * @param STR   the strafe joystick input
     * @param RCW   the rotational joystick input
     * @param theta the current gyro angle
     */
    public void fieldCentricControl(double FWD, double STR, double RCW, double theta) {
        // Temp variable so the true value of FWD can still be used in the calculations
        double temp;
        // Convert normal joystick inputs to ones that match the values we need
        STR = -STR;
        RCW = -RCW * TURN_SENSITIVITY;

        // This math assumes our gyro updates counter clockwise
        temp = FWD * Math.cos(theta) - STR * Math.sin(theta);
        STR = FWD * Math.sin(theta) + STR * Math.cos(theta);
        FWD = temp;

        // Calculate the wheel speed vectors and normalize all of them to be less than one
        double[] motorValues = normalizeWheelSpeeds(FWD, STR, RCW);

        // Loop through the motors values and supply the correct power to the correct motor
        for (int i = 0; i < motorValues.length; i++) {
            driveMotors[i].setPower(motorValues[i] * speedMultiplier);
        }
    }

    public void autoControl(double imu, double FWD, double STR, double RCW, double headingAdjust) {
        currentHeadingRadians = imu;

        double rotateTweak = 0;

        // Uses a threshold to make sure this still evaluates correctly if the joystick is like 0.0001
        if((RCW < 0.05 && RCW > -0.05) && (STR + FWD) != 0) {

            double headingError = (currentHeadingRadians - prevHeadingRadians);

            if(headingError != 0) {

                if (headingError > Math.PI) {
                    headingError -= (float)Math.PI;
                } else if (headingError < -Math.PI) {
                    headingError += (float)Math.PI;
                }

                rotateTweak = headingAdjust * headingError;
            }
        }

        STR = -STR;
        RCW = -(RCW + rotateTweak) * TURN_SENSITIVITY;
        double[] motorValues = normalizeWheelSpeeds(FWD, STR, RCW);

        // Loop through the motors values and supply the correct power to the correct motor
        for (int i = 0; i < motorValues.length; i++) {
            driveMotors[i].setPower(motorValues[i] * speedMultiplier);
        }

        prevHeadingRadians = currentHeadingRadians;
    }

    /**
     * Functionality to easily change the speed multiplier to the requested value
     */
    public void toggleSlowMode() {
        if (speedMultiplier == 1)
            speedMultiplier = 0.5;
        else
            speedMultiplier = 1;
    }

    /**
     * Normalize the wheel speeds to be less than 1 but scaled such that the differences remain
     *
     * @return list of wheel speeds to feed into the motors
     */
    public double[] normalizeWheelSpeeds(double FWD, double STR, double RCW) {
        double front_left = FWD + RCW + STR;
        double front_right = FWD - RCW - STR;
        double back_left = (FWD + RCW - STR*backBias);
        double back_right = (FWD - RCW + STR*backBias);

        if (Math.abs(STR) > .05) {
            backBias = .75;
        } else {
            backBias = 1;
        }

        double max = Math.abs(front_left);
        if (Math.abs(front_right) > max) max = Math.abs(front_right);
        if (Math.abs(back_left) > max) max = Math.abs(back_left);
        if (Math.abs(back_right) > max) max = Math.abs(back_right);

        // Reduce the max value to less than 1
        if (max > 1) {
            front_left /= max;
            front_right /= max;
            back_left /= max;
            back_right /= max;
        }

        return new double[]{front_left, front_right, back_left, back_right};
    }
}
