package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Gyro;

@Autonomous(name = "BasicAuto", group = "test")
public class BasicAuto extends LinearOpMode {

    boolean autoStop = false;

    double AUTONOMOUS_HEADING_ADJUST_SCALAR = 30;

    double driveDistance = 0;

    private MecanumDrive m_drive;
    private Gyro m_gyro;

    @Override
    public void runOpMode() throws InterruptedException {

        m_drive = new MecanumDrive(hardwareMap);
        m_gyro = Gyro.get(hardwareMap);

        waitForStart();

        Drive(1, 0, 0);

        Thread.sleep(5000);

        Drive(0, 0, 0);

        Drive(-.5, 0, 0);

        Thread.sleep(500);

        Drive(0, 0, 0);

    }

    public void Drive(double FWD, double STR, double RCW) {
        m_drive.autoControl(m_gyro.getGyroYaw(), FWD, STR, RCW, AUTONOMOUS_HEADING_ADJUST_SCALAR);
    }

}