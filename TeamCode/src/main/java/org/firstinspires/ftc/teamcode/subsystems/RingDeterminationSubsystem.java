package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.utilities.RingDeterminationPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

public class RingDeterminationSubsystem {

    public boolean isOk = false;

    OpenCvWebcam webcam;
    RingDeterminationPipeline pipeline;

    public RingDeterminationSubsystem(HardwareMap hardwareMap, Telemetry telemetry)
    {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new RingDeterminationPipeline();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {

                isOk = true;
                try {
                    webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
                } catch(Exception e) {
                    isOk = false;
                }

                if(isOk) telemetry.addLine("OK");

                else telemetry.addLine("ERROR");

                telemetry.update();
            }
        });
    }

    public RingDeterminationPipeline.RingPosition getPosition() {
        return pipeline.getPosition();
    }
}
