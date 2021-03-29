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
import org.firstinspires.ftc.teamcode.utilities.RingDeterminationPipeline;



@Autonomous(name = "BasicAuto", group = "test")
public class BasicAuto extends LinearOpMode {

    private MecanumDrive m_drive;
    private Gyro m_gyro;
    private EncoderControl m_eDrive;
    private ShooterSubsystem m_shoot;
    private WobbleSubsystem m_wobble;
    private RingDeterminationSubsystem m_ring;

    double flywheelPower;
    double indexerSpeed;

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
                telemetry.addData(String.valueOf(RingDeterminationPipeline.RingPosition.NONE),"Rings");
                break;
            case ONE:
                telemetry.addData(String.valueOf(RingDeterminationPipeline.RingPosition.ONE),"Rings");
                break;
            case FOUR:
                telemetry.addData(String.valueOf(RingDeterminationPipeline.RingPosition.FOUR),"Rings");
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

        Thread.sleep(10000);
    }

}