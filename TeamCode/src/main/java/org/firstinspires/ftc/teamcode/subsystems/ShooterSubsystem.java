package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ShooterSubsystem {
    private DcMotor flywheelRight;
    private DcMotor flywheelLeft;

    private DcMotor indexerWheel;

    private double flywheelPower = 0;
    private double indexerSpeed = 0;

    private boolean shooterEnabled = false;
    private boolean indexerEnabled = false;

    public ShooterSubsystem(HardwareMap hardwareMap, double flywheelPower, double indexerSpeed){
        flywheelLeft = hardwareMap.get(DcMotor.class, "flyLeft");
        flywheelRight = hardwareMap.get(DcMotor.class, "flyRight");

        indexerWheel = hardwareMap.get(DcMotor.class, "indexer");

        // Invert the indexer motor
        indexerWheel.setDirection(DcMotorSimple.Direction.REVERSE);

        // Invert the right shooter motor
        flywheelRight.setDirection(DcMotorSimple.Direction.REVERSE);

        this.flywheelPower = flywheelPower;
        this.indexerSpeed = indexerSpeed;
    }

    /**
     * Toggle the shooter on or off
     */
    public void toggleShooter(){
        shooterEnabled = !shooterEnabled;
    }

    public void toggleIndexer(){
        indexerEnabled = !indexerEnabled;
    }

    /**
     * Called during the loop
     */
    public void perodic(){
        if (shooterEnabled){
            flywheelLeft.setPower(flywheelPower);
            flywheelRight.setPower(flywheelPower);
        }
        else{
            flywheelLeft.setPower(0);
            flywheelRight.setPower(0);
        }

        if(indexerEnabled){
            indexerWheel.setPower(1);
        }
        else{
            indexerWheel.setPower(0);
        }
    }

    /**
     * Run the shooter at a set power
     * @param power duty cycle to run the shooter at
     */
    public void runShooter(double power){
        flywheelLeft.setPower(power);
        flywheelRight.setPower(power);
    }

    public void runIndexer(double power){
        indexerWheel.setPower(power);
    }


}
