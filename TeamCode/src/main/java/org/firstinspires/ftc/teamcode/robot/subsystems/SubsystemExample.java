package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.core.lib.pid.PIDController.Mode.ANGLE;
import static org.firstinspires.ftc.teamcode.robot.constants.SubsystemExampleConstants.*;
import static org.firstinspires.ftc.teamcode.robot.constants.GlobalConstants.*;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;

/**
 * Example subsystem that implements the FGCLib.
 * Look at the example to build your own subsystems
 */
public class SubsystemExample implements Subsystem {
    private static SubsystemExample instance;
    private Telemetry telemetry;
    private DcMotor motorRight;
    private DcMotor motorLeft;
    private TouchSensor limitRight;
    private TouchSensor limitLeft;
    private SmartGamepad operator;
    private org.firstinspires.ftc.teamcode.core.lib.pid.PIDController PIDController;

    private SubsystemExample() {
    }

    /**
     * Initialize method from the Subsystem Interface
     * @param hardwareMap
     * @param telemetry
     */
    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        motorRight = hardwareMap.get(DcMotor.class, MOTOR_RIGHT);
        motorLeft = hardwareMap.get(DcMotor.class, MOTOR_LEFT);
        limitRight = hardwareMap.get(TouchSensor.class, LIMIT_RIGHT);
        limitLeft = hardwareMap.get(TouchSensor.class, LIMIT_LEFT);
        this.telemetry = telemetry;

        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        PIDController = new PIDController(PID.kP, PID.kI, PID.kD, PID.kF, ANGLE);

        telemetry.addData("SubsystemExample Subsystem", "Initialized");
    }

    /**
     * Execute method from the Subsystem Interface
     * @param gamepadManager
     */
    @Override
    public void execute(GamepadManager gamepadManager) {
        operator = gamepadManager.getOperator();

        telemetry.addData("SubsystemExample Subsystem", "Running");

        PIDController.calculate(TARGET_DEGREE, motorLeft.getCurrentPosition());

        operator.whileButtonLeftBumper()
                .and(operator.isButtonRightBumper())
                .run(() -> {
                    PIDController.setPowerMotor(motorLeft, CORE_HEX_TICKS_PER_REVOLUTION);
                    PIDController.setPowerMotor(motorRight, CORE_HEX_TICKS_PER_REVOLUTION);
                });

        operator.whileButtonRightBumper()
                .run(() -> {
                    motorLeft.setPower(0);
                    PIDController.setPowerMotor(motorRight, CORE_HEX_TICKS_PER_REVOLUTION);
                });

        operator.whileButtonLeftBumper()
                .run(() -> {
                    motorRight.setPower(0);
                    PIDController.setPowerMotor(motorLeft, CORE_HEX_TICKS_PER_REVOLUTION);
                });

        operator.whileLeftTriggerPressed()
                .and(operator.isRightTriggerPressed())
                .andNot(isLimitRight())
                .andNot(isLimitLeft())
                .run(() -> {
                    motorRight.setPower(operator.getRightTrigger());
                    motorLeft.setPower(operator.getLeftTrigger());
                });

        operator.whileLeftTriggerPressed()
                .andNot(isLimitLeft())
                .run(() -> {
                    motorLeft.setPower(operator.getLeftTrigger());
                }, () -> {
                    resetEncoder(motorLeft);
                    operator.rumbleTimer( 200);
                });

        operator.whileRightTriggerPressed()
                .andNot(isLimitRight())
                .run(() -> {
                    motorRight.setPower(operator.getRightTrigger());
                }, () -> {
                    resetEncoder(motorRight);
                    operator.rumbleTimer(200);
                });

        stop();
    }

    /**
     * Start method from the Subsystem Interface
     */
    @Override
    public void start() {

    }

    /**
     * Stop method from the Subsystem Interface
     */
    @Override
    public void stop() {
        motorRight.setPower(0);
        motorLeft.setPower(0);
    }

    private void resetEncoder(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public boolean isLimitRight() {
        return limitRight.isPressed();
    }

    public boolean isLimitLeft() {
        return limitLeft.isPressed();
    }

    /**
     * getInstance is a method used to create a instance of the subsystem.
     * It's not good to have many objects of the same subsystem, so every
     * subsystem in FGCLib will have just one instance, that is created
     * with the getInstance method
     * @return SubsystemExample SingleTon
     */
    public static synchronized SubsystemExample getInstance() {
        if (instance == null) {
            instance = new SubsystemExample();
        }
        return instance;
    }
}