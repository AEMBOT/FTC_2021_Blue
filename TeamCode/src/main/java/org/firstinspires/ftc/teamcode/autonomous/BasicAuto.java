package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.EncoderControl;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.utilities.Gyro;

@Autonomous(name = "BasicAuto", group = "test")
public class BasicAuto extends LinearOpMode {

    private MecanumDrive m_drive;
    private Gyro m_gyro;
    private EncoderControl m_eDrive;
    private ShooterSubsystem m_shoot;
    double flywheelPower;
    double indexerSpeed;

    @Override
    public void runOpMode() throws InterruptedException {

        m_drive = new MecanumDrive(hardwareMap);
        m_gyro = Gyro.get(hardwareMap);
        m_eDrive = new EncoderControl(hardwareMap);
        m_shoot = new ShooterSubsystem(hardwareMap, flywheelPower, indexerSpeed);


        waitForStart();

        m_eDrive.encoderDrive(.5, 72,72, telemetry);

        m_eDrive.encoderDrive(.75, -24,-24, telemetry);
        Thread.sleep(500);

        m_eDrive.encoderStrafe(1,8,telemetry);
        Thread.sleep(500);

        m_shoot.runShooter(.9);
        Thread.sleep(500);

        m_shoot.runIndexer(.9);
        Thread.sleep(2000);

        m_shoot.runShooter(0);
        m_shoot.runShooter(0);
        Thread.sleep(500);

        m_eDrive.encoderDrive(.5, 22,22,telemetry);
    }

}