package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.robot.constants.SubsystemExampleConstants.*;
import static org.firstinspires.ftc.teamcode.robot.constants.GlobalConstants.*;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;

/**
 * Example subsystem for FGCLib — SmartGamepad version.
 *
 * Shows how to use PIDController with SmartGamepad's fluent API
 * (whileButtonX().run(), whileTriggerPressed().andNot().run(), etc.).
 *
 * Copy this file and adapt it to your mechanism.
 * See SubsystemBasicGamepadExample for a simpler version without SmartGamepad.
 */
public class SubsystemExample implements Subsystem {

    private static SubsystemExample instance;

    private Telemetry    telemetry;
    private DcMotor      motorRight;
    private DcMotor      motorLeft;
    private TouchSensor  limitRight;
    private TouchSensor  limitLeft;
    private SmartGamepad operator;
    private PIDController pidController;

    private SubsystemExample() {}

    // ── Lifecycle ──────────────────────────────────────────────────────────────

    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        motorRight = hardwareMap.get(DcMotor.class, MOTOR_RIGHT);
        motorLeft  = hardwareMap.get(DcMotor.class, MOTOR_LEFT);
        limitRight = hardwareMap.get(TouchSensor.class, LIMIT_RIGHT);
        limitLeft  = hardwareMap.get(TouchSensor.class, LIMIT_LEFT);

        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // ── PID Controller ────────────────────────────────────────────────────
        // Create the controller with your tuned constants from SubsystemExampleConstants.
        // enableMotionProfile() makes the movement smooth — no sudden jerks.
        // enableVoltageCompensation() keeps behavior consistent as the battery drains.
        // Both are optional: remove either line if you don't need it.
        pidController = new PIDController(PID.kP, PID.kI, PID.kD, PID.kF);
        pidController.enableMotionProfile(2200, 4400);
        pidController.enableVoltageCompensation(hardwareMap);

        telemetry.addData("SubsystemExample", "Initialized");
    }

    @Override
    public void execute(GamepadManager gamepadManager) {
        operator = gamepadManager.getOperator();

        // ── PID control ───────────────────────────────────────────────────────
        // Both motors move the same mechanism, so one encoder is enough.
        // motorLeft is used as the reference — its position represents both.
        double power = pidController.calculate(TARGET_DEGREE, motorLeft.getCurrentPosition());

        // Both bumpers → move both motors to target angle via PID
        operator.whileButtonLeftBumper()
                .and(operator.isButtonRightBumper())
                .run(() -> {
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                });

        // Right bumper only → move right motor to target
        operator.whileButtonRightBumper()
                .run(() -> {
                    motorLeft.setPower(0);
                    motorRight.setPower(power);
                });

        // Left bumper only → move left motor to target
        operator.whileButtonLeftBumper()
                .run(() -> {
                    motorRight.setPower(0);
                    motorLeft.setPower(power);
                });

        // ── Manual control with limit switch protection ────────────────────────

        // Both triggers → drive both motors manually (if neither limit is pressed)
        operator.whileLeftTriggerPressed()
                .and(operator.isRightTriggerPressed())
                .andNot(isLimitRight())
                .andNot(isLimitLeft())
                .run(() -> {
                    motorRight.setPower(operator.getRightTrigger());
                    motorLeft.setPower(operator.getLeftTrigger());
                });

        // Left trigger → drive left motor; reset encoder when limit is hit
        operator.whileLeftTriggerPressed()
                .andNot(isLimitLeft())
                .run(
                        () -> motorLeft.setPower(operator.getLeftTrigger()),
                        () -> {
                            resetEncoder(motorLeft);
                            operator.rumbleTimer(200);
                        }
                );

        // Right trigger → drive right motor; reset encoder when limit is hit
        operator.whileRightTriggerPressed()
                .andNot(isLimitRight())
                .run(
                        () -> motorRight.setPower(operator.getRightTrigger()),
                        () -> {
                            resetEncoder(motorRight);
                            operator.rumbleTimer(200);
                        }
                );

        telemetry.addData("SubsystemExample", "Running");
        telemetry.addData("Position", motorLeft.getCurrentPosition());
        telemetry.addData("At target", pidController.atTarget());
    }

    @Override
    public void start() {}

    @Override
    public void stop() {
        motorRight.setPower(0);
        motorLeft.setPower(0);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void resetEncoder(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public boolean isLimitRight() { return limitRight.isPressed(); }
    public boolean isLimitLeft()  { return limitLeft.isPressed(); }

    // ── Singleton ─────────────────────────────────────────────────────────────

    /**
     * Returns the single instance of this subsystem.
     * FGCLib uses the singleton pattern so there is never more than one
     * object per subsystem running at the same time.
     */
    public static synchronized SubsystemExample getInstance() {
        if (instance == null) instance = new SubsystemExample();
        return instance;
    }
}