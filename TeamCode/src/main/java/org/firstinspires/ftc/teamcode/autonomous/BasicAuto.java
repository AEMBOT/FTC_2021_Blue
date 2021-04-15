package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.EncoderControl;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.RingDeterminationSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WobbleSubsystem;
import org.firstinspires.ftc.teamcode.utilities.Gyro;



@Autonomous(name = "BasicAuto", group = "main")
public class BasicAuto extends LinearOpMode {

    private MecanumDrive m_drive;
    private Gyro m_gyro;
    private EncoderControl m_eDrive;
    private ShooterSubsystem m_shoot;
    private WobbleSubsystem m_wobble;
    private RingDeterminationSubsystem m_ring;
    private IntakeSubsystem m_intake;

    double flywheelPower = .93;
    double indexerSpeed = 1;

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
        m_intake = new IntakeSubsystem(hardwareMap);

        waitForStart();

        switch(m_ring.getPosition()) {
            case NONE:
                //Target zone A
                telemetry.addLine("No rings detected: Target zone A");
                telemetry.update();
                Thread.sleep(500);

                wobbleHolder.setPosition(.5);

                m_eDrive.encoderStrafe(.5,-12,telemetry);

                m_eDrive.encoderDrive(.5, 46,46, telemetry);
                Thread.sleep(500);

                wobbleHolder.setPosition(1);
                Thread.sleep(1000);

                m_eDrive.encoderDrive(.75, -4,-4, telemetry);
                Thread.sleep(500);

                m_eDrive.encoderStrafe(1,22,telemetry);
                Thread.sleep(500);

                runShooter();

                m_eDrive.encoderDrive(.5, 22,22,telemetry);

                break;
            case ONE:
                //Target zone B
                telemetry.addLine("One ring detected: Target zone B");
                telemetry.update();
                Thread.sleep(500);

                wobbleHolder.setPosition(.5);

                m_eDrive.encoderDrive(.5,74,74, telemetry);

                m_eDrive.encoderStrafe(.5,14, telemetry);
                Thread.sleep(500);

                wobbleHolder.setPosition(1);
                Thread.sleep(1000);

                m_eDrive.encoderDrive(1,-26,-26, telemetry);

                runShooter();
                Thread.sleep(4500);

                m_intake.runIntake(1);
                Thread.sleep(1000);

                m_eDrive.encoderDrive(.2, -24,-24, telemetry);
                Thread.sleep(2000);

                m_eDrive.encoderDrive(.5, 24,24, telemetry);

                runShooter();
                Thread.sleep(4500);

                m_eDrive.encoderDrive(.5, 18,18,telemetry);

                break;
            case FOUR:
                //Target zone C
                telemetry.addLine("Four rings detected: Target zone C");
                telemetry.update();
                Thread.sleep(500);

                wobbleHolder.setPosition(.5);

                m_eDrive.encoderStrafe(.5,-12, telemetry);

                m_eDrive.encoderDrive(.5,112,112, telemetry);
                Thread.sleep(500);

                wobbleHolder.setPosition(1);
                Thread.sleep(1000);

                m_eDrive.encoderDrive(1,-64,-64, telemetry);

                m_eDrive.encoderStrafe(1,22,telemetry);
                Thread.sleep(1000);

                runShooter();

                m_eDrive.encoderDrive(.5, 24,24,telemetry);

                break;
            default:
                telemetry.addLine("Ring Determination Failed!");
                telemetry.addLine("Resulting to default");
                telemetry.update();
                Thread.sleep(500);

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
        Thread.sleep(2000);

        m_shoot.runIndexer(indexerSpeed);
        Thread.sleep(2000);

        m_shoot.runShooter(0);
        m_shoot.runShooter(0);
        Thread.sleep(500);
    }

}