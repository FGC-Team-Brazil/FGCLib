package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.robot.constants.SubsystemExampleConstants.*;
import static org.firstinspires.ftc.teamcode.robot.constants.GlobalConstants.*;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;

/**
 * Example subsystem for FGCLib — basic Gamepad version.
 *
 * Same behavior as SubsystemExample, but uses the raw FTC Gamepad object
 * instead of SmartGamepad. This is simpler to understand for teams that
 * are not yet familiar with SmartGamepad's fluent API.
 *
 * Copy this file and adapt it to your mechanism.
 * See SubsystemExample for the SmartGamepad version.
 */
public class SubsystemBasicGamepadExample implements Subsystem {

    private static SubsystemBasicGamepadExample instance;

    private Telemetry    telemetry;
    private DcMotor      motorRight;
    private DcMotor      motorLeft;
    private TouchSensor  limitRight;
    private TouchSensor  limitLeft;
    private Gamepad      operator;
    private PIDController pidController;

    private SubsystemBasicGamepadExample() {}

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
        pidController.enableVoltageCompensation(hardwareMap, 12);

        telemetry.addData("SubsystemBasicGamepadExample", "Initialized");
    }

    @Override
    public void execute(GamepadManager gamepadManager) {
        operator = gamepadManager.getRawOperatorGamepad();

        // ── PID control ───────────────────────────────────────────────────────
        // Both motors move the same mechanism, so one encoder is enough.
        // motorLeft is used as the reference — its position represents both.
        double power = pidController.calculate(TARGET_DEGREE, motorLeft.getCurrentPosition());

        // Both bumpers → move both motors to target angle via PID
        if (operator.left_bumper && operator.right_bumper) {
            motorLeft.setPower(power);
            motorRight.setPower(power);

            // Right bumper only → move right motor to target
        } else if (operator.right_bumper) {
            motorLeft.setPower(0);
            motorRight.setPower(power);

            // Left bumper only → move left motor to target
        } else if (operator.left_bumper) {
            motorRight.setPower(0);
            motorLeft.setPower(power);
        }

        // ── Manual control with limit switch protection ────────────────────────

        // Both triggers → drive both motors manually (if neither limit is pressed)
        if (operator.left_trigger > 0.1 && operator.right_trigger > 0.1
                && !isLimitLeft() && !isLimitRight()) {
            motorLeft.setPower(operator.left_trigger);
            motorRight.setPower(operator.right_trigger);

            // Left trigger → drive left motor; reset encoder when limit is hit
        } else if (operator.left_trigger > 0.1) {
            if (!isLimitLeft()) {
                motorLeft.setPower(operator.left_trigger);
            } else {
                resetEncoder(motorLeft);
                operator.rumble(200);
            }

            // Right trigger → drive right motor; reset encoder when limit is hit
        } else if (operator.right_trigger > 0.1) {
            if (!isLimitRight()) {
                motorRight.setPower(operator.right_trigger);
            } else {
                resetEncoder(motorRight);
                operator.rumble(200);
            }
        }

        telemetry.addData("SubsystemBasicGamepadExample", "Running");
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
    public static synchronized SubsystemBasicGamepadExample getInstance() {
        if (instance == null) instance = new SubsystemBasicGamepadExample();
        return instance;
    }
}