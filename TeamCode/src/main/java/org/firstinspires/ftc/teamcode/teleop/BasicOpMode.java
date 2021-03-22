package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WobbleSubsystem;
import org.firstinspires.ftc.teamcode.utilities.Gyro;
import org.firstinspires.ftc.teamcode.utilities.TeleopControl;


@TeleOp(name = "BasicOpMode", group = "Test")
public class BasicOpMode extends OpMode {

    private final double FLYWHEEL_SPEED = .95;
    private final double INDEXER_SPEED = 1;

    // Create subsystems
    private MecanumDrive m_drive;
    private ShooterSubsystem m_shooter;
    private IntakeSubsystem m_intake;
    private WobbleSubsystem m_wobble;
    private Gyro m_gyro;

    private TeleopControl teleop;

    private final double WOBBLE_ARM_POWER_SCALAR = -.8;

    private boolean intakeOn;
    private boolean shooterOn;
    private boolean indexerOn;
    private boolean wobbleGrab;



    @Override
    public void init() {
        m_drive = new MecanumDrive(hardwareMap);
        m_shooter = new ShooterSubsystem(hardwareMap, FLYWHEEL_SPEED, INDEXER_SPEED);
        m_intake = new IntakeSubsystem(hardwareMap);
        m_wobble = new WobbleSubsystem(hardwareMap);
        m_gyro = Gyro.get(hardwareMap);

        // Create button handler
        teleop = new TeleopControl();
    }

    @Override
    public void loop() {
        // Slow mode lambda toggle
        teleop.runOncePerPress(gamepad1.a, m_drive::toggleSlowMode);

        // Toggles the shooter on or off
        teleop.runOncePerPress(gamepad2.x, m_shooter::toggleShooter);

        // Toggles the intake on or off
        teleop.runOncePerPress(gamepad2.y, m_intake::toggleIntake);

        //toggles the indexer on or off
        teleop.runOncePerPress(gamepad2.b, m_shooter::toggleIndexer);

        //toggles the wobble servo on and
        teleop.runOncePerPress(gamepad2.a, m_wobble::toggleArmPosition);

        m_wobble.runWobbleGrabber(WOBBLE_ARM_POWER_SCALAR*gamepad2.right_stick_y);

        telemetry.addData("yaw", m_gyro.getGyroYaw());

        // Will update motor commands
        m_shooter.perodic();
        m_intake.periodic();
        m_drive.nonFieldCentricControl(m_gyro.getGyroYaw(), gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        // Leave this here as it resets all the values for the next loop
        teleop.endPeriodic();
    }


}
