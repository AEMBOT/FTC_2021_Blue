package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class WobbleSubsystem {

    private DcMotor wobbleArm;
    private Servo wobbleServo;

    private boolean armDown = false;

    public WobbleSubsystem(HardwareMap hardwareMap){
        wobbleArm = hardwareMap.get(DcMotor.class, "wobbleArm");
        wobbleServo = hardwareMap.get(Servo.class, "wobbleServo");

        // Set wobble arm motor to brake
        wobbleArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    /**
     * Toggles the arm position of the wobble grabber
     */
    public void toggleArmPosition(){
        if(!armDown) {
            wobbleServo.setPosition(0);
            armDown = true;
        }
        else{
            wobbleServo.setPosition(0.5);
            armDown = false;
        }
    }

    /**
     * Runs the wobble grabber at a set power
     * @param power duty cycle to run the wobble grabber at
     */
    public void runWobbleGrabber(double power){
        wobbleArm.setPower(power);
    }
}
