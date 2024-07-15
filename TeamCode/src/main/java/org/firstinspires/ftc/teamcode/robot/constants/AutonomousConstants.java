package org.firstinspires.ftc.teamcode.robot.constants;

import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;

public class AutonomousConstants {
    public static final double LOOKFORWARDCONSTANT = 2;
    public static final double WHEEL_RADIUS = 4.5; //cm
    public static final double MOTOR_REDUCTION = 336; //ticks per revolution of wheel
    public static final double TICK_TO_CM_CONVERSION_VALUE = 2*Math.PI*WHEEL_RADIUS/(MOTOR_REDUCTION);
    //public static final double TRACK_WIDTH=0; //distance between parallel wheels viewing the robot from the front
    //public static final double WHEEL_BASE = TRACK_WIDTH; //same as track width but rom the side
    //public static final double LATERAL_MULTIPLIER=0;
    public static final double TOLERATED_HEADING_ERROR = 25;//degrees
    public static final double MAX_HEADING_VELOCITY = 0.5;//power applied to motor (0 to 1)
    public static double MAXSPEED = 3; // centimeter per second
    public static  double MAXACCELERATION = 0.1; //centimeter per second squared
    public static final double SPEEDPROPORTIONALGAIN = 0;
    public static PIDController pathFollowingPID = new PIDController(0.3,0,0,0); // do not touch kI or the robot goes crazy ü
    public static PIDController stoppedAtPointPID = new PIDController(0.6,0,0,0);

    public static double HeadK= 0.001;
}
