package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.BaseOpMode;

@Autonomous(name = "BasicAuto", group = "test")
public class BasicAuto extends BaseOpMode{

    boolean autoStop = false;

    double AUTONOMOUS_HEADING_ADJUST_SCALAR = 30;

    double driveDistance = 0;

    @Override
    public void loop() {
        if (!autoStop) {

            //Drive until the color sensor sees more blue than red (the shooting line)
            //while(colorSensor.blue() < colorSensor.red()) {
            //    Drive(1,0,0);
            //}

            Drive(1, 0, 0);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Drive(0, 0, 0);

            Drive(-.5, 0, 0);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Drive(0, 0, 0);

            autoStop = true;

        }
    }

    public void Drive(double FWD, double STR, double RCW) {
        mecanumDrive.autoControl(getGyroYaw(), FWD, STR, RCW, AUTONOMOUS_HEADING_ADJUST_SCALAR);
    }

}