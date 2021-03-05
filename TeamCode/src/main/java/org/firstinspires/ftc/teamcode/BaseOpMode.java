package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.drivetrain.MecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.TeleopControl;

/**
 * BaseOpMode abstract class that all other OpModes should extend from to allow easy initialization in one place
 */
public abstract class BaseOpMode extends OpMode {

    // Generic DcMotors to command the 4 drive wheels
    protected DcMotor frontLeft;
    protected DcMotor frontRight;
    protected DcMotor backLeft;
    protected DcMotor backRight;

    protected DcMotor[] driveMotorArray;

    // Generic DcMotors to command the 2 shooter wheels
    protected DcMotor flyLeft;
    protected DcMotor flyRight;

    // Generic DcMotor to command the wobble arm
    protected DcMotor wobbleArm;

    // Continuous Servos to command the 2 intake wheels
    protected CRServo intakeLeft;
    protected CRServo intakeRight;

    protected BNO055IMU imu;

    protected TeleopControl teleop;
    protected MecanumDrive mecanumDrive;

    @Override
    public void init() {

        // Init all motors to corresponding mapping names
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeft = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRight = hardwareMap.get(DcMotor.class, "backRightMotor");

        // Create an array of the drive motors to easily pass into sub-methods
        driveMotorArray = new DcMotor[]{frontLeft, frontRight, backLeft, backRight};

        flyLeft = hardwareMap.get(DcMotor.class, "flyLeft");
        flyRight = hardwareMap.get(DcMotor.class, "flyRight");

        wobbleArm = hardwareMap.get(DcMotor.class, "wobbleArm");

        intakeLeft = hardwareMap.get(CRServo.class, "intakeLeft");
        intakeRight = hardwareMap.get(CRServo.class, "intakeRight");

        // Control method that allows easier use of button inputs
        teleop = new TeleopControl();

        // Handles control of the mecanum drive base
        mecanumDrive = new MecanumDrive(driveMotorArray);

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

        // Invert the right shooter motor
        flyRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Invert the right intake servo
        intakeRight.setDirection(CRServo.Direction.REVERSE);

        // Set wobble arm motor to brake
        wobbleArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Get the angle of the robot via the gyro scope (in radians)
     * @return the angle of the robot
     */
    public double getGyroYaw(){
        return Math.toRadians(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle);
    }
}
