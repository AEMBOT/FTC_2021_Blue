package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.RingDeterminationSubsystem;

@Autonomous (name = "RingDetectionTest", group = "test")
public class RingDetectionTest extends LinearOpMode {

    private RingDeterminationSubsystem m_ring;

    @Override
    public void runOpMode() throws InterruptedException {

        m_ring = new RingDeterminationSubsystem(hardwareMap, telemetry);

        waitForStart();



        while (opModeIsActive()) {
            telemetry.addData("Rings:", String.valueOf(m_ring.getPosition()));
            telemetry.addLine("");
            telemetry.addLine("If this is not the correct assessment of the rings, make sure the detection range is correct and there is no glare on the camera.");
            telemetry.addLine("");
            telemetry.update();
        }

        Thread.sleep(25000);
        telemetry.addLine("If nothing is displayed you did not wait until the phone confirmed that the webcam was initialized before you hit play.");
        telemetry.update();
    }
}
