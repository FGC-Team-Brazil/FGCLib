package org.firstinspires.ftc.teamcode.core.lib.builders;

import static org.firstinspires.ftc.teamcode.robot.constants.DrivetrainBuilderConstants.*;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.MotorVelocityData;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.Pose2d;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.RobotMovementState;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.TargetVelocityData;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.Vector2d;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;
import org.firstinspires.ftc.teamcode.robot.constants.AutonomousConstants;
import org.firstinspires.ftc.teamcode.robot.constants.DrivetrainBuilderConstants;

import java.util.List;

public class DrivetrainBuilder implements Subsystem {
    private static DrivetrainBuilder instance;
    private DcMotorSimple.Direction motorRightDirection;
    private DcMotorSimple.Direction motorLeftDirection;
    private String motorLeftFrontName;
    private String motorRightFrontName;
    private String motorLeftBackName;
    private String motorRightBackName;
    private double limiter;
    private DcMotorEx motorFrontRight;
    private DcMotorEx motorFrontLeft;
    private DcMotorEx motorBackRight;
    private DcMotorEx motorBackLeft;
    private PIDController motorFLPID = AutonomousConstants.pathFollowingPID;
    private PIDController motorFRPID = AutonomousConstants.pathFollowingPID;
    private PIDController motorBLPID = AutonomousConstants.pathFollowingPID;
    private PIDController motorBRPID = AutonomousConstants.pathFollowingPID;

    private IMU imu; //deppends on how old each driver hub is, I dont know what is the imu we have
    private Telemetry telemetry;
    private SmartGamepad driver;
    public Pose2d currentPose;
    double currVX;
    double currVY;
    double currentHeading;
    double desiredVX;
    double desiredVY;
    double Headingerror;
    public RobotMovementState movementState = new RobotMovementState(0,0);
    private String imuName;

    private DrivetrainBuilder() {
    }

    public static DrivetrainBuilder build(@NonNull String motorFrontRightName, @NonNull String motorFrontLeftName,@NonNull String motorBackRightName, @NonNull String motorBackLeftName,@NonNull String imuName, boolean isMotorRightInverted, boolean isMotorLeftInverted,Pose2d botStartingPosition) {

        getInstance();
        instance.currentPose = botStartingPosition;
        instance.motorLeftFrontName = motorFrontLeftName;
        instance.motorRightFrontName = motorFrontRightName;
        instance.motorLeftBackName = motorBackLeftName;
        instance.motorRightBackName = motorBackRightName;
        instance.imuName = imuName;

        //instance.motorLeftDirection = isMotorLeftInverted
        //        ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
        //instance.motorRightDirection = isMotorRightInverted
        //        ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;

        return instance;
    }


    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        motorFrontLeft = hardwareMap.get(DcMotorEx.class, motorLeftFrontName);
        motorFrontRight = hardwareMap.get(DcMotorEx.class, motorRightFrontName);
        motorBackLeft = hardwareMap.get(DcMotorEx.class, motorLeftBackName);
        motorBackRight = hardwareMap.get(DcMotorEx.class, motorRightBackName);
        imu = hardwareMap.get(IMU.class,imuName);

        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
        ));
        imu.resetYaw();

        //motorLeft.setDirection(motorLeftDirection);
        //motorRight.setDirection(motorRightDirection);
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
        //motorLeft.setPower(leftSpeed);
        //motorRight.setPower(rightSpeed);
    }
    public void setPower(double FLSpeed,double FRSpeed,double BLSpeed,double BRSpeed){
        motorFrontLeft.setPower(FLSpeed);
        motorFrontRight.setPower(FRSpeed);
        motorBackLeft.setPower(BLSpeed);
        motorBackRight.setPower(BRSpeed);
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

    public void controlBasedOnVelocity(@NonNull TargetVelocityData movementState, double elapsedTime){

        relativeOdometryUpdate(elapsedTime);

        desiredVX += (movementState.getXV()-currVX)*elapsedTime*(AutonomousConstants.MAXACCELERATION/AutonomousConstants.MAXSPEED);
        desiredVY += (movementState.getYV()-currVY)*elapsedTime*(AutonomousConstants.MAXACCELERATION/AutonomousConstants.MAXSPEED);
        Headingerror = (movementState.getH()-currentHeading);
        Pose2d desiredVelocityField = new Pose2d(desiredVX,desiredVY,Headingerror*elapsedTime*AutonomousConstants.HeadK);
        Pose2d desiredVelocityBot = fieldToRobotVelocity(desiredVelocityField);

        MotorVelocityData desiredMotorSpeed = getDesiredWheelVelocities(desiredVelocityBot);
        MotorVelocityData actualMotorSpeed = getRegistredWheelVelocities();
        //pid for motor speeds
        double appliedFL = motorFLPID.calculate(desiredMotorSpeed.velocityFrontLeft,actualMotorSpeed.velocityFrontLeft);
        double appliedFR = motorFLPID.calculate(desiredMotorSpeed.velocityFrontRight,actualMotorSpeed.velocityFrontRight);
        double appliedBL = motorFLPID.calculate(desiredMotorSpeed.velocityBackLeft,actualMotorSpeed.velocityBackLeft);
        double appliedBR = motorFLPID.calculate(desiredMotorSpeed.velocityBackRight,actualMotorSpeed.velocityBackRight);

        setPower(appliedFL,appliedFR,appliedBL,appliedBR);
    }


    public MotorVelocityData getDesiredWheelVelocities(Pose2d botRelativeVelocity){
        return new MotorVelocityData().updateAppliedVelocities(botRelativeVelocity).normalize();
    }
    public MotorVelocityData getRegistredWheelVelocities(){
        double frontLeft = (motorFrontLeft.getVelocity(AngleUnit.RADIANS)*AutonomousConstants.WHEEL_RADIUS);
        double rearLeft = (motorBackLeft.getVelocity(AngleUnit.RADIANS)*AutonomousConstants.WHEEL_RADIUS);
        double rearRight = (motorBackRight.getVelocity(AngleUnit.RADIANS)*AutonomousConstants.WHEEL_RADIUS);
        double frontRight = (motorFrontRight.getVelocity(AngleUnit.RADIANS)*AutonomousConstants.WHEEL_RADIUS);
        return new MotorVelocityData(frontLeft,frontRight,rearLeft,rearRight);
    }
    public void setPose2d(Pose2d newPose){
        currentPose =  newPose;
    }
    public Pose2d getCurrentPose(){
        return currentPose;
    }

    public  void relativeOdometryUpdate(double elapsedSeconds) {
        double dtheta;
        Pose2d robotPoseDelta = wheelToRobotVelocities();

        //double dtheta = currentPose.getHeadingRadians()+(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)-currentPose.getHeadingRadians());
        if (imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)-currentPose.getHeadingRadians()>Math.PI){
            dtheta = -2*Math.PI+imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)-currentPose.getHeadingRadians();
        } else{
            dtheta = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)-currentPose.getHeadingRadians();
        }

        double botXComponentRelativeToField = robotPoseDelta.getX()*Math.sin(currentPose.getHeadingRadians()) - robotPoseDelta.getY()*Math.cos(currentPose.getHeadingRadians()); // i dont know if this is right, gotta test it
        double botYComponentRelativeToField = robotPoseDelta.getY()*Math.sin(currentPose.getHeadingRadians()) + robotPoseDelta.getX()*Math.cos(currentPose.getHeadingRadians());

        //Pair var8 = var10000;
        //double sineTerm = Math.sin(dtheta);
        //double cosTerm = Math.cos(dtheta);
        //Vector2d fieldPositionDelta = new Vector2d(sineTerm * robotPoseDelta.getX() - cosTerm * robotPoseDelta.getY(), cosTerm * robotPoseDelta.getX() + sineTerm * robotPoseDelta.getY()); probably closer to this

        Pose2d fieldPoseDelta = new Pose2d(currentPose.getX()+ botXComponentRelativeToField*elapsedSeconds,currentPose.getY()+botYComponentRelativeToField*elapsedSeconds,
                robotPoseDelta.getHeadingDegrees());
        currentPose.updatePose(
                currentPose.getX() + fieldPoseDelta.getX(),
                currentPose.getY() + fieldPoseDelta.getY(),
                currentPose.getHeadingRadians() + dtheta);
    }
    public Pose2d wheelToRobotVelocities() {
        //double k = (AutonomousConstants.TRACK_WIDTH + AutonomousConstants.WHEEL_BASE) / 2.0;
        double frontLeft = (motorFrontLeft.getVelocity(AngleUnit.RADIANS)/AutonomousConstants.MOTOR_REDUCTION*AutonomousConstants.WHEEL_RADIUS);
        double rearLeft = (motorBackLeft.getVelocity(AngleUnit.RADIANS)/AutonomousConstants.MOTOR_REDUCTION*AutonomousConstants.WHEEL_RADIUS);
        double rearRight = (motorBackRight.getVelocity(AngleUnit.RADIANS)/AutonomousConstants.MOTOR_REDUCTION*AutonomousConstants.WHEEL_RADIUS);
        double frontRight = (motorFrontRight.getVelocity(AngleUnit.RADIANS)/AutonomousConstants.MOTOR_REDUCTION*AutonomousConstants.WHEEL_RADIUS);

        //i think this shouldnt be divided by 4, instead should be multiplied by root(2) but gotta test that first
        return new Pose2d(
                frontLeft+frontRight+rearLeft+rearRight/4,
                (rearLeft + frontRight - frontLeft - rearRight)/4,
                imu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate*1 //(rearRight + frontRight - frontLeft - rearLeft) / k/4
               );
        //(novo x = (x * cos a - y* sin a), novo y = () )
    }
    public Pose2d fieldToRobotVelocity(Pose2d fieldVel) {
        return new Pose2d(
                fieldVel.getX()*Math.sin(-currentPose.getHeadingRadians()) - fieldVel.getY()*Math.cos(-currentPose.getHeadingRadians()),
                fieldVel.getY()*Math.sin(-currentPose.getHeadingRadians()) + fieldVel.getX()*Math.cos(-currentPose.getHeadingRadians())
                ,currentPose.getHeadingDegrees());
    }
}
