package org.firstinspires.ftc.teamcode.robot.constants;

import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.Pose2d;

public class DrivetrainBuilderConstants {
    public static final String MOTOR_FRONT_RIGHT = "drivetrain_motorFrontRight";
    public static final String MOTOR_FRONT_LEFT = "drivetrain_motorFrontLeft";
    public static final String MOTOR_BACK_RIGHT = "drivetrain_motorBackRight";
    public static final String MOTOR_BACK_LEFT = "drivetrain_motorBackLeft";
    public static final String IMU_NAME = "imu";
    public static final boolean MOTOR_RIGHT_INVERTED  = false;
    public static final boolean MOTOR_LEFT_INVERTED = true;
    public static final double LIMITER_MIN = 0.7;
    public static final double LIMITER_DEFAULT = 1.0;
    public static final Pose2d START_POSITION = new Pose2d(0,0,0);
}
