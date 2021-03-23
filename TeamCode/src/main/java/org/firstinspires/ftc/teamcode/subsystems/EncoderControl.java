package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.teleop.BasicOpMode;

/**
 * Handles the control a mecanum drive base with encoders using a power value and distances in inches
 */

public class EncoderControl {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;

    static int COUNTS_PER_INCH = 48;

    public EncoderControl(HardwareMap hardwareMap) {

        // Init all devices to corresponding mapping names
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeft = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRight = hardwareMap.get(DcMotor.class, "backRightMotor");

        // Invert the right side
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void encoderGoTarget(double power, int frontLeftTarget, int frontRightTarget, int backLeftTarget, int backRightTarget, Telemetry telemetry) {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setTargetPosition(frontLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backLeft.setTargetPosition(backLeftTarget);
        backRight.setTargetPosition(backRightTarget);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        while (frontLeft.isBusy()||frontRight.isBusy()||backLeft.isBusy()||backRight.isBusy()) {
            telemetry.addData("Left Front:", frontLeft.getCurrentPosition());
            telemetry.addData("Left Back:", backLeft.getCurrentPosition());
            telemetry.addData("Right Front:", frontRight.getCurrentPosition());
            telemetry.addData("Right Back:", backRight.getCurrentPosition());

            telemetry.update();

        }
    }

    public void encoderDrive(double power, int leftDistance, int rightDistance, Telemetry telemetry) {

        int frontLeftTarget = (leftDistance *  COUNTS_PER_INCH);
        int frontRightTarget = (rightDistance * COUNTS_PER_INCH);
        int backLeftTarget = (leftDistance * COUNTS_PER_INCH);
        int backRightTarget = (rightDistance * COUNTS_PER_INCH);

        encoderGoTarget(power, frontLeftTarget, frontRightTarget, backLeftTarget, backRightTarget, telemetry);
    }

    public void encoderStrafe(double power, int distance, Telemetry telemetry) {

        int frontLeftTarget = (distance *  COUNTS_PER_INCH);
        int frontRightTarget = -(distance * COUNTS_PER_INCH);
        int backLeftTarget = -(distance * COUNTS_PER_INCH);
        int backRightTarget = (distance * COUNTS_PER_INCH);

        encoderGoTarget(power, frontLeftTarget, frontRightTarget, backLeftTarget, backRightTarget, telemetry);
    }
}


