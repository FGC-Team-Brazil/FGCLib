package org.firstinspires.ftc.teamcode.core.lib.builders;

import static org.firstinspires.ftc.teamcode.robot.constants.DrivetrainConstants.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartController;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.SubsystemBuilder;

public class DrivetrainBuilder implements SubsystemBuilder {
    private static DrivetrainBuilder instance;
    private DcMotorSimple.Direction motorRightDirection;
    private DcMotorSimple.Direction motorLeftDirection;
    private String motorLeftName;
    private String motorRightName;
    private double limiter;
    private DcMotor motorRight;
    private DcMotor motorLeft;
    private Telemetry telemetry;

    private DrivetrainBuilder() {
    }

    public static DrivetrainBuilder configure(String motorRightName, String motorLeftName, boolean isMotorRightInverted, boolean isMotorLeftInverted) {
        getInstance();

        instance.motorLeftName = motorLeftName;
        instance.motorRightName = motorRightName;
        instance.motorLeftDirection = isMotorLeftInverted
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
        instance.motorRightDirection = isMotorRightInverted
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;

        return instance;
    }

    @Override
    public DrivetrainBuilder configure() {
        return getInstance();
    }


    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        motorLeft = hardwareMap.get(DcMotor.class, motorLeftName);
        motorRight = hardwareMap.get(DcMotor.class, motorRightName);
        motorLeft.setDirection(motorLeftDirection);
        motorRight.setDirection(motorRightDirection);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void execute(SmartController driver) {
        telemetry.addData("DrivetrainBuilder Subsystem", "Running");
        arcadeDrive(-driver.getLeftStickY(), -driver.getRightStickX(), driver);
    }

    public void arcadeDrive(double xSpeed, double zRotation, SmartController driver) {
        limiter = LIMITER_DEFAULT;

        driver.whileButtonRightBumper()
                .run(() -> limiter = LIMITER_MIN);

        driver.whileButtonLeftBumper()
                .run(() -> limiter = LIMITER_MAX);

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

    private static DrivetrainBuilder getInstance() {
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
