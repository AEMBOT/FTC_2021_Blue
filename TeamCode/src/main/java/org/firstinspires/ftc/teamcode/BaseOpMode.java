package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * BaseOpMode abstract class that all other OpModes should extend from to allow easy initialization in one place
 */
public abstract class BaseOpMode extends OpMode {

    // Generic DcMotors to command the 4 drive wheels
    protected DcMotor frontLeft;
    protected DcMotor frontRight;
    protected DcMotor backLeft;
    protected DcMotor backRight;
    protected DcMotor flyLeft;
    protected DcMotor flyRight;

    protected BNO055IMU imu;

    @Override
    public void init() {

        // Init all motors to corresponding mapping names
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeft = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRight = hardwareMap.get(DcMotor.class, "backRightMotor");
        //flyLeft = hardwareMap.get(DcMotor.class, "FlyLeft");
        //flyRight = hardwareMap.get(DcMotor.class, "FlyRight");

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

        // Invert the right side
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
