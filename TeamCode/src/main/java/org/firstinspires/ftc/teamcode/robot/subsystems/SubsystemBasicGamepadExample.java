package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.core.lib.pid.PIDController.Mode.ANGLE;
import static org.firstinspires.ftc.teamcode.robot.constants.SubsystemExampleConstants.*;
import static org.firstinspires.ftc.teamcode.robot.constants.GlobalConstants.*;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;
import org.firstinspires.ftc.teamcode.robot.constants.GlobalConstants;

/**
 * Example subsystem that implements the FGCLib.
 * Look at the example to build your own subsystems.
 * This example does the same thing as Subsystem example but
 * without using the GamepadManager portion of the lib
 */
public class SubsystemBasicGamepadExample implements Subsystem {
    private static SubsystemBasicGamepadExample instance;
    private Telemetry telemetry;
    private DcMotor motorRight;
    private DcMotor motorLeft;
    private TouchSensor limitRight;
    private TouchSensor limitLeft;
    private Gamepad operator;
    private org.firstinspires.ftc.teamcode.core.lib.pid.PIDController PIDController;
    private SubsystemBasicGamepadExample(){

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

        PIDController = new PIDController(PID.kP, PID.kI, PID.kD, PID.kF, ANGLE, CORE_HEX_TICKS_PER_REVOLUTION);

        telemetry.addData("SubsystemExample Subsystem", "Initialized");
    }

    /**
     * Execute method from the Subsystem Interface
     * @param gamepadManager
     */
    @Override
    public void execute(GamepadManager gamepadManager) {
        operator = gamepadManager.getOperatorBasic();

        telemetry.addData("SubsystemExample Subsystem", "Running");

        PIDController.calculate(TARGET_DEGREE, motorLeft.getCurrentPosition());
        if (operator.left_bumper && operator.right_bumper){
            PIDController.setPowerMotor(motorLeft);
            PIDController.setPowerMotor(motorRight);
        }
        if (operator.left_bumper) {
            motorRight.setPower(0);
            PIDController.setPowerMotor(motorLeft);
        }
        if (operator.right_bumper) {
            motorLeft.setPower(0);
            PIDController.setPowerMotor(motorRight);
        }

        if (operator.left_trigger>0.9 && operator.right_trigger>0.9 && !(isLimitLeft()) && !(isLimitRight())){
            motorRight.setPower(operator.right_trigger);
            motorLeft.setPower(operator.left_trigger);
        }

        if (operator.left_trigger>0.9 && !(isLimitLeft())){
            motorLeft.setPower(operator.left_trigger);
            resetEncoder(motorLeft);
            operator.rumble( 200);
        }

        if (operator.right_trigger>0.9 && !(isLimitRight())){
            motorRight.setPower(operator.right_trigger);
            resetEncoder(motorRight);
            operator.rumble(200);
        }

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
     * @return SubsystemBasicGamepadExample SingleTon
     */
    public static SubsystemBasicGamepadExample getInstance() {
        if (instance == null) {
            instance = new SubsystemBasicGamepadExample();
        }
        return instance;
    }
}