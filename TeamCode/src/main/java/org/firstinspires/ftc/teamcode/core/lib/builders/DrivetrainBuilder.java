package org.firstinspires.ftc.teamcode.core.lib.builders;

import static org.firstinspires.ftc.teamcode.robot.constants.DrivetrainBuilderConstants.*;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.Pose2d;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.RobotMovementState;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.TargetVelocityData;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.constants.AutonomousConstants;

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
    public Pose2d currentPose;
    double currVX;
    double currVY;
    double desiredVX;
    double desiredVY;
    double currHeadingVelocity;
    public RobotMovementState movementState = new RobotMovementState(0,0);

    private DrivetrainBuilder() {
    }

    public static DrivetrainBuilder build(@NonNull String motorRightName, @NonNull String motorLeftName, boolean isMotorRightInverted, boolean isMotorLeftInverted,Pose2d botStartingPosition) {

        getInstance();
        instance.currentPose = botStartingPosition;
        instance.motorLeftName = motorLeftName;
        instance.motorRightName = motorRightName;
        instance.motorLeftDirection = isMotorLeftInverted
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
        instance.motorRightDirection = isMotorRightInverted
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;

        return instance;
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
    public void execute(GamepadManager gamepadConfig) {
        driver = gamepadConfig.getDriver();

        //telemetry.addData("DrivetrainBuilder Subsystem", "Running");
        //arcadeDrive(-driver.getLeftStickY(), -driver.getRightStickX(), driver);
    }

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

    public void controlBasedOnVelocity(TargetVelocityData movementState,double elapsedTime){
        updatePose2d();
        desiredVX += (movementState.getXV()-currVX)*elapsedTime*(AutonomousConstants.MAXACCELERATION/AutonomousConstants.MAXSPEED);
        desiredVY += (movementState.getXV()-currVX)*elapsedTime*(AutonomousConstants.MAXACCELERATION/AutonomousConstants.MAXSPEED);

        


    }
    public void updatePose2d(){

        //does math to determine bot position and velocity todo
    }
    public Pose2d getCurrentPose(){
        return currentPose;
    }

}
