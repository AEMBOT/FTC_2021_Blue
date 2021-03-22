package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class Gyro {

    public static Gyro instance;
    private BNO055IMU imu;

    private Gyro(HardwareMap hardwareMap){
        // Create and set the IMU parameters to be used when retrieving angle information.
        BNO055IMU.Parameters imuParams = new BNO055IMU.Parameters();

        imuParams.mode = BNO055IMU.SensorMode.IMU;
        imuParams.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParams.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParams.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(imuParams);

        // Wait for the gyroscope to calibrate
        while(!imu.isGyroCalibrated()){
            try {Thread.sleep(50); } catch (InterruptedException e) {break;}
        }
    }

    public static Gyro get(HardwareMap hardwareMap){
        if(instance == null){
            instance = new Gyro(hardwareMap);
        }
        return instance;
    }

    /**
     * Get the angle of the robot via the gyro scope (in radians)
     * @return the angle of the robot
     */
    public double getGyroYaw(){
        return Math.toRadians(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle);
    }
}
