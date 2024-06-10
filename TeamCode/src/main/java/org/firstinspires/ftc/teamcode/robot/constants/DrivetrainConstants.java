package org.firstinspires.ftc.teamcode.robot.constants;

public class DrivetrainConstants {
    public static final String MOTOR_RIGHT = "drivetrain_motorRight";
    public static final String MOTOR_LEFT = "drivetrain_motorLeft";
    public static final boolean MOTOR_RIGHT_INVERTED  = false;
    public static final boolean MOTOR_LEFT_INVERTED = true;
    public static final String IMU = "imu";
    public static final double MOTORS_REDUCTION = (float) 1 / 4 * 1 / 5;
    public static final double WHEEL_CIRCUMFERENCE = 2 * Math.PI * 0.045;
    public static final double LIMITER_MAX = 1;
    public static final double LIMITER_MIN = 0.2;
    public static final double LIMITER_DEFAULT = 1;
}
