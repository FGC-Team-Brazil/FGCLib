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

import Ori.Coval.Logging.AutoLog;

/**
 * Example subsystem that implements the FGCLib.
 * Look at the example to build your own subsystems
 * This example also shows how to use the GamepadManager
 * section of the LIB
 *
 * Shows how to use PIDController with SmartGamepad's fluent API
 *  (whileButtonX().run(), whileTriggerPressed().andNot().run(), etc.).
 *
 * Copy this file and adapt it to your mechanism.
 *  See SubsystemBasicGamepadExample for a simpler version without SmartGamepad.
 * 
 * To the Logger:
 *  // Instead of this:
 *  SubsystemExample example = new SubsystemExample();
 *
 *  // Use this: 
 *  SubsystemExampleAutoLogged example = new SubsystemExampleAutoLogged();
 *
 */
@AutoLog
public class SubsystemExample implements Subsystem {
    private static SubsystemExampleAutoLogged instance;
    private Telemetry telemetry;
    private DcMotor motorRight;
    private DcMotor motorLeft;
    private TouchSensor limitRight;
    private TouchSensor limitLeft;
    private SmartGamepad operator;
    private PIDController pidController;

    private final double TICKS_PER_REV = 537.7;
    private final double TICKS_PER_DEGREE = TICKS_PER_REV / 360.0;

    protected SubsystemExample() {
    }

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
        pidController = new PIDController(PID.kP, PID.kI, PID.kD, PID.kF);
        pidController.enableMotionProfile(2200, 4400);
        pidController.enableVoltageCompensation(hardwareMap);

        telemetry.addData("SubsystemExample", "Initialized");
    }

    @Override
    public void execute(GamepadManager gamepadManager) {
        operator = gamepadManager.getOperator();

        boolean usePID = operator.isButtonLeftBumper() || operator.isButtonRightBumper();
        boolean useManual = operator.isLeftTriggerPressed() || operator.isRightTriggerPressed();
        double power;

        if (usePID) {
            double targetTicks = TARGET_DEGREE * TICKS_PER_DEGREE;
            power = pidController.calculate(targetTicks, motorLeft.getCurrentPosition());
        } else {
            power = 0;
            pidController.reset();
        }

        if (!usePID && !useManual) {
            motorLeft.setPower(0);
            motorRight.setPower(0);
        }

        operator.whileButtonLeftBumper()
                .and(operator.isButtonRightBumper())
                .run(() -> {
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                });

        operator.whileButtonRightBumper()
                .andNot(operator.isButtonLeftBumper())
                .run(() -> {
                    motorLeft.setPower(0);
                    motorRight.setPower(power);
                });

        operator.whileButtonLeftBumper()
                .andNot(operator.isButtonRightBumper())
                .run(() -> {
                    motorRight.setPower(0);
                    motorLeft.setPower(power);
                });

        // ── Comandos Manuais ──────────────────────────────────────────────────
        operator.whileLeftTriggerPressed()
                .and(operator.isRightTriggerPressed())
                .andNot(isLimitRight())
                .andNot(isLimitLeft())
                .run(() -> {
                    motorRight.setPower(operator.getRightTrigger());
                    motorLeft.setPower(operator.getLeftTrigger());
                });

        operator.whileLeftTriggerPressed()
                .andNot(operator.isRightTriggerPressed())
                .andNot(isLimitLeft())
                .run(() -> motorLeft.setPower(operator.getLeftTrigger()));

        operator.whileRightTriggerPressed()
                .andNot(operator.isLeftTriggerPressed())
                .andNot(isLimitRight())
                .run(() -> motorRight.setPower(operator.getRightTrigger()));

        // ── Proteção de Hardware e Reset de Encoders ──────────────────────────
        if (isLimitLeft()) {
            resetEncoderSafely(motorLeft);
        }

        if (isLimitRight()) {
            resetEncoderSafely(motorRight);
        }

        telemetry.addData("SubsystemExample", "Running");
        telemetry.addData("Position (Ticks)", motorLeft.getCurrentPosition());
        telemetry.addData("Position (Degrees)", motorLeft.getCurrentPosition() / TICKS_PER_DEGREE);
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

    private void resetEncoderSafely(DcMotor motor) {
        if (motor.getCurrentPosition() != 0) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            pidController.reset();
        }
    }


    public boolean isLimitRight() {
        return limitRight.isPressed();
    }

    public boolean isLimitLeft()  {
        return limitLeft.isPressed();
    }


    /**
     * getInstance is a method used to create a instance of the subsystem.
     * It's not good to have many objects of the same subsystem, so every
     * subsystem in FGCLib will have just one instance, that is created
     * with the getInstance method
     * @return SubsystemExample SingleTon
     */
    public static SubsystemExampleAutoLogged getInstance() {
        if (instance == null) {
            instance = new SubsystemExampleAutoLogged();
        }
        return instance;
    }
}