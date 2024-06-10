package org.firstinspires.ftc.teamcode.core.lib.builders;

import static org.firstinspires.ftc.teamcode.robot.constants.DrivetrainConstants.MOTOR_LEFT;
import static org.firstinspires.ftc.teamcode.robot.constants.DrivetrainConstants.MOTOR_RIGHT;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartController;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;

public class DrivetrainBuilder implements Subsystem{
    private static DrivetrainBuilder instance;

    private String motorRightName;
    private String motorLeftName;
    private boolean isMotorRightInverted;
    private boolean isMotorLeftInverted;
    private double limiter;
    private DcMotor motorRight;
    private DcMotor motorLeft;
    private Telemetry telemetry;

    private DrivetrainBuilder() {
    }

    public static DrivetrainBuilder configure(String motorRightName, String motorLeftName, boolean isMotorRightInverted, boolean isMotorLeftInverted) {
        if (instance == null) {
            instance = new DrivetrainBuilder();
        }

        instance.motorRightName = motorRightName;
        instance.motorLeftName = motorLeftName;
        instance.isMotorRightInverted = isMotorRightInverted;
        instance.isMotorLeftInverted = isMotorLeftInverted;

        return instance;
    }


    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        motorLeft = hardwareMap.get(DcMotor.class, MOTOR_LEFT);
        motorRight = hardwareMap.get(DcMotor.class, MOTOR_RIGHT);
        motorLeft.setDirection(isMotorLeftInverted ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        motorRight.setDirection(isMotorRightInverted ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
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
        limiter = 0.8;

        driver.whileButtonRightBumper()
                .run(() -> limiter = 0.5);

        driver.whileButtonLeftBumper()
                .run(() -> limiter = 1.0);

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

}
