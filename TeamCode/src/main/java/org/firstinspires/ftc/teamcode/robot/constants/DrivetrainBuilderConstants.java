package org.firstinspires.ftc.teamcode.robot.constants;

import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.Pose2d;

public class DrivetrainBuilderConstants {
    public static final String MOTOR_RIGHT = "drivetrain_motorRight";
    public static final String MOTOR_LEFT = "drivetrain_motorLeft";
    public static final boolean MOTOR_RIGHT_INVERTED  = false;
    public static final boolean MOTOR_LEFT_INVERTED = true;
    public static final double LIMITER_MIN = 0.7;
    public static final double LIMITER_DEFAULT = 1.0;
    public static final Pose2d START_POSITION = new Pose2d(0,0,0);
}
