package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.Constants;

public class Drivetrain implements Subsystem {
    private static Drivetrain instance;
    private DcMotor motorLeft, motorRight;
    private double limiter;

    private Drivetrain() {}

    public static synchronized Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        motorLeft = hardwareMap.get(DcMotor.class, Constants.Drivetrain.MOTOR_LEFT_NAME);
        motorRight = hardwareMap.get(DcMotor.class, Constants.Drivetrain.MOTOR_RIGHT_NAME);

        motorLeft.setDirection(Constants.Drivetrain.IS_MOTOR_LEFT_INVERTED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        motorRight.setDirection(Constants.Drivetrain.IS_MOTOR_RIGHT_INVERTED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        limiter = Constants.Drivetrain.LIMITER_DEFAULT;

        telemetry.addData("Drivetrain Subsystem", "Initialized");
    }

    @Override
    public void execute() {
    }

    public void arcadeDrive(double ySpeed, double zRotation) {
        double ySpeedLimited = Math.max(-limiter, Math.min(limiter, ySpeed));
        double zRotationLimited = Math.max(-limiter, Math.min(limiter, zRotation));

        double leftSpeed = ySpeedLimited - zRotationLimited;
        double rightSpeed = ySpeedLimited + zRotationLimited;

        setPower(leftSpeed, rightSpeed);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        setPower(0, 0);
    }

    public void setPower(double left, double right) {
        motorLeft.setPower(left);
        motorRight.setPower(right);
    }
}
