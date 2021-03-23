package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.EncoderControl;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Gyro;

@Autonomous(name = "BasicAuto", group = "test")
public class BasicAuto extends LinearOpMode {

    private MecanumDrive m_drive;
    private Gyro m_gyro;
    private EncoderControl m_eDrive;

    @Override
    public void runOpMode() throws InterruptedException {

        m_drive = new MecanumDrive(hardwareMap);
        m_gyro = Gyro.get(hardwareMap);
        m_eDrive = new EncoderControl(hardwareMap);


        waitForStart();

        m_eDrive.encoderDrive(1, 72,72, telemetry);

        Thread.sleep(1000);

        m_eDrive.encoderStrafe(1, 24, telemetry);

    }

}