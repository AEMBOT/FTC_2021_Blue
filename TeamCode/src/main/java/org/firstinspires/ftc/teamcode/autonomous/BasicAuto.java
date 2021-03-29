package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.EncoderControl;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WobbleSubsystem;
import org.firstinspires.ftc.teamcode.utilities.Gyro;
import org.firstinspires.ftc.teamcode.subsystems.RingDeterminationSubsystem;



@Autonomous(name = "BasicAuto", group = "test")
public class BasicAuto extends LinearOpMode {

    private MecanumDrive m_drive;
    private Gyro m_gyro;
    private EncoderControl m_eDrive;
    private ShooterSubsystem m_shoot;
    private WobbleSubsystem m_wobble;
    private RingDeterminationSubsystem m_ring;

    double flywheelPower = 1;
    double indexerSpeed = .5;

    private Servo wobbleHolder;


    @Override
    public void runOpMode() throws InterruptedException {


        wobbleHolder = hardwareMap.get(Servo.class, "wobbleHolder");

        m_drive = new MecanumDrive(hardwareMap);
        m_gyro = Gyro.get(hardwareMap);
        m_eDrive = new EncoderControl(hardwareMap);
        m_shoot = new ShooterSubsystem(hardwareMap, flywheelPower, indexerSpeed);
        m_wobble = new WobbleSubsystem(hardwareMap);
        m_ring = new RingDeterminationSubsystem(hardwareMap, telemetry);

        waitForStart();

        switch(m_ring.getPosition()) {
            case NONE:
                //Target zone A
                wobbleHolder.setPosition(.5);

                m_eDrive.encoderStrafe(.5,-12,telemetry);

                m_eDrive.encoderDrive(.5, 70,70, telemetry);
                Thread.sleep(500);

                wobbleHolder.setPosition(1);
                Thread.sleep(1000);

                m_eDrive.encoderDrive(.75, -22,-22, telemetry);
                Thread.sleep(500);

                m_eDrive.encoderStrafe(1,22,telemetry);
                Thread.sleep(500);

                runShooter();

                m_eDrive.encoderDrive(.5, 24,24,telemetry);

                break;
            case ONE:
                //Target zone B
                wobbleHolder.setPosition(.5);

                m_eDrive.encoderDrive(.5,78,78, telemetry);

                m_eDrive.encoderStrafe(.5,14, telemetry);
                Thread.sleep(500);

                wobbleHolder.setPosition(1);
                Thread.sleep(1000);

                m_eDrive.encoderDrive(1,-30,-30, telemetry);

                runShooter();

                m_eDrive.encoderDrive(.5, 18,18,telemetry);

                break;
            case FOUR:
                //Target zone C
                wobbleHolder.setPosition(.5);

                m_eDrive.encoderStrafe(.5,-12, telemetry);

                m_eDrive.encoderDrive(.5,112,112, telemetry);
                Thread.sleep(500);

                wobbleHolder.setPosition(1);
                Thread.sleep(1000);

                m_eDrive.encoderDrive(1,-64,-64, telemetry);

                m_eDrive.encoderStrafe(1,22,telemetry);
                Thread.sleep(500);

                runShooter();

                m_eDrive.encoderDrive(.5, 24,24,telemetry);

                break;
            default:
                wobbleHolder.setPosition(.5);

                m_eDrive.encoderDrive(.5, 70,70, telemetry);
                Thread.sleep(500);

                wobbleHolder.setPosition(1);
                Thread.sleep(1000);

                m_eDrive.encoderDrive(.75, -22,-22, telemetry);
                Thread.sleep(500);

                m_eDrive.encoderStrafe(1,8,telemetry);
                Thread.sleep(500);

                m_shoot.runShooter(1);
                Thread.sleep(500);

                m_shoot.runIndexer(.5);
                Thread.sleep(3000);

                m_shoot.runShooter(0);
                m_shoot.runShooter(0);
                Thread.sleep(500);

                m_eDrive.encoderDrive(.5, 24,24,telemetry);

        }

        telemetry.addLine("DONE");
        telemetry.update();
    }

    public void runShooter() throws InterruptedException {
        m_shoot.runShooter(flywheelPower);
        Thread.sleep(500);

        m_shoot.runIndexer(indexerSpeed);
        Thread.sleep(3000);

        m_shoot.runShooter(0);
        m_shoot.runShooter(0);
        Thread.sleep(500);
    }

}