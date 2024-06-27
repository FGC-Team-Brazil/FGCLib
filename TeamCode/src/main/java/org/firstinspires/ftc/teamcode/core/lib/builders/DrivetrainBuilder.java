package org.firstinspires.ftc.teamcode.core.lib.builders;

import static org.firstinspires.ftc.teamcode.robot.constants.DrivetrainBuilderConstants.*;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;


/**
 * DrivetrainBuilder is a helper class that assists on the creation
 * of a Drivetrain Subsystem.
 * It contains some usual boilerplate for creating a subsystem.
 * Use it when creating tank drivetrains with two motors.
 * <p>
 * Caution: This class don't support holonomic
 * drivetrains
 */
public class DrivetrainBuilder implements Subsystem {
    private static DrivetrainBuilder instance;
    private DcMotorSimple.Direction motorRightDirection;
    private DcMotorSimple.Direction motorLeftDirection;
    private String motorLeftName;
    private String motorRightName;
    private double limiter;
    private DcMotor motorRight;
    private DcMotor motorLeft;
    private Telemetry telemetry;
    private SmartGamepad driver;

    private DrivetrainBuilder() {
    }

    public static DrivetrainBuilder build(@NonNull String motorRightName, @NonNull String motorLeftName, boolean isMotorRightInverted, boolean isMotorLeftInverted) {
        getInstance();

        instance.motorLeftName = motorLeftName;
        instance.motorRightName = motorRightName;
        instance.motorLeftDirection = isMotorLeftInverted
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
        instance.motorRightDirection = isMotorRightInverted
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;

        return instance;
    }

    /**
     * Initialize method from Subsystem Interface
     */
    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        motorLeft = hardwareMap.get(DcMotor.class, motorLeftName);
        motorRight = hardwareMap.get(DcMotor.class, motorRightName);
        motorLeft.setDirection(motorLeftDirection);
        motorRight.setDirection(motorRightDirection);
    }

    /**
     * Start method from Subsystem Interface
     */
    @Override
    public void start() {

    }

    /**
     * Execute method from Subsystem Interface
     * @param gamepadConfig
     */
    @Override
    public void execute(GamepadManager gamepadConfig) {
        driver = gamepadConfig.getDriver();

        telemetry.addData("DrivetrainBuilder Subsystem", "Running");
        arcadeDrive(-driver.getLeftStickY(), -driver.getRightStickX(), driver);
    }

    /**
     * Stop method from Subsystem Interface
     */
    @Override
    public void stop() {

    }

    /**
     * Default Method for drive control. This method is for
     * tank drivetrains.
     * @param xSpeed
     * @param zRotation
     * @param driver
     */
    public void arcadeDrive(double xSpeed, double zRotation, SmartGamepad driver) {
        limiter = LIMITER_DEFAULT;

        driver.whileButtonRightBumper()
                .run(() -> limiter = LIMITER_MIN);

        double xSpeedLimited = Math.max(-limiter, Math.min(limiter, xSpeed));
        double zRotationLimited = Math.max(-limiter, Math.min(limiter, zRotation));

        double leftSpeed = xSpeedLimited - zRotationLimited;
        double rightSpeed = xSpeedLimited + zRotationLimited;

        setPower(leftSpeed, rightSpeed);
    }

    public void setPower(double leftSpeed, double rightSpeed) {
        motorLeft.setPower(leftSpeed);
        motorRight.setPower(rightSpeed);
    }

    /**
     * getInstance is a method used to create a instance of the subsystem.
     * It's not good to have many objects of the same subsystem, so every
     * subsystem in FGCLib will have just one instance, that is created
     * with the getInstance method
     * @return DriveTrainBuilder SingleTon
     */
    public static DrivetrainBuilder getInstance() {
        if (instance == null) {
            synchronized (DrivetrainBuilder.class) {
                if (instance == null) {
                    instance = new DrivetrainBuilder();
                }
            }
        }
        return instance;
    }

}
