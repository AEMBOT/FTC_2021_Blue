package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.BaseOpMode;


@TeleOp(name = "BasicOpMode", group = "Test")
public class BasicOpMode extends BaseOpMode {

    private final double WOBBLE_ARM_POWER_SCALAR = -.8;

    private boolean intakeOn;
    private boolean shooterOn;
    private boolean indexerOn;
    private boolean wobbleGrab;
    private double flywheelSpeed = .95;
    private double indexerSpeed = 1;


    @Override
    public void loop() {
        // Slow mode lambda toggle
        teleop.runOncePerPress(gamepad1.a, () -> mecanumDrive.toggleSlowMode());

        // Toggles the shooter on or off
        teleop.runOncePerPress(gamepad2.x, () -> shooterOn = !shooterOn);

        // Toggles the intake on or off
        teleop.runOncePerPress(gamepad2.y, () -> intakeOn = !intakeOn);

        //toggles the indexer on or off
        teleop.runOncePerPress(gamepad2.b, () -> indexerOn = !indexerOn);

        //toggles the wobble servo on and off
        teleop.runOncePerPress(gamepad2.a, () -> wobbleGrab = !wobbleGrab);

        if(shooterOn) {
            flyLeft.setPower(flywheelSpeed);
            flyRight.setPower(flywheelSpeed);
        } else {
            flyLeft.setPower(0);
            flyRight.setPower(0);
        }

        if(intakeOn) {
          intakeLeft.setPower(1);
          intakeRight.setPower(1);
        } else {
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }

        if(indexerOn) {
            indexer.setPower(indexerSpeed);
        } else {
            indexer.setPower(0);
        }

        if(wobbleGrab) {
            wobbleServo.setPosition(0);
        } else {
            wobbleServo.setPosition(.5);
        }

        wobbleArm.setPower(WOBBLE_ARM_POWER_SCALAR*gamepad2.right_stick_y);

        telemetry.addData("yaw",getGyroYaw());

        // Send commands to the mecanum drive base
        // mecanumDrive.fieldCentricControl(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, getGyroYaw());

        mecanumDrive.nonFieldCentricControl(imu, gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        // Leave this here as it resets all the values for the next loop
        super.teleop.endPeriodic();
    }


}
