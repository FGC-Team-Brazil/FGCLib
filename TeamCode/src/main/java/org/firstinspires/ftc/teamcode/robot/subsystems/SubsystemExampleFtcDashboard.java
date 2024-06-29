package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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

public class SubsystemExampleFtcDashboard implements Subsystem {
    private static SubsystemExampleFtcDashboard instance;
    private Telemetry telemetry;
    private DcMotor motorRight;
    private DcMotor motorLeft;
    private TouchSensor limitRight;
    private TouchSensor limitLeft;
    private SmartGamepad operator;
    private PIDController PIDController;

    private SubsystemExampleFtcDashboard() {
    }

    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        motorRight = hardwareMap.get(DcMotor.class, MOTOR_RIGHT);
        motorLeft = hardwareMap.get(DcMotor.class, MOTOR_LEFT);
        limitRight = hardwareMap.get(TouchSensor.class, LIMIT_RIGHT);
        limitLeft = hardwareMap.get(TouchSensor.class, LIMIT_LEFT);

        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        PIDController = new PIDController(PID.kP, PID.kI, PID.kD, PID.kF, ANGLE);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("SubsystemExampleFtcDashboard Subsystem", "Initialized");
    }

    @Override
    public void execute(GamepadManager gamepadManager) {
        operator = gamepadManager.getOperator();

        telemetry.addData("SubsystemExampleFtcDashboard Subsystem", "Running");

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
                    operator.rumble(1, 0, 200);
                });

        operator.whileRightTriggerPressed()
                .andNot(isLimitRight())
                .run(() -> {
                    motorRight.setPower(operator.getRightTrigger());
                }, () -> {
                    resetEncoder(motorRight);
                    operator.rumble(0, 1, 200);
                });

        telemetry.update();
        stop();
    }

    @Override
    public void start() {

    }

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

    public static synchronized SubsystemExampleFtcDashboard getInstance() {
        if (instance == null) {
            instance = new SubsystemExampleFtcDashboard();
        }
        return instance;
    }
}