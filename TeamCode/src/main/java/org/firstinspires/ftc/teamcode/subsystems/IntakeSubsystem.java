package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeSubsystem {
    private CRServo intakeLeft;
    private CRServo intakeRight;

    private boolean intakeEnabled = false;
    public IntakeSubsystem(HardwareMap hardwareMap){
        intakeLeft = hardwareMap.get(CRServo.class, "intakeLeft");
        intakeRight = hardwareMap.get(CRServo.class, "intakeRight");

        // Invert the right intake servo
        intakeRight.setDirection(CRServo.Direction.REVERSE);
    }

    /**
     * Toggle the indexer on or off
     */
    public void toggleIntake(){
        intakeEnabled = !intakeEnabled;
    }

    /**
     * Called every loop to update motor commands
     */
    public void periodic(){
        if(intakeEnabled){
            intakeLeft.setPower(1);
            intakeRight.setPower(1);
        }
        else{
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }
    }

    public void runIntake(double power){
        intakeRight.setPower(power);
        intakeLeft.setPower(power);
    }
}
