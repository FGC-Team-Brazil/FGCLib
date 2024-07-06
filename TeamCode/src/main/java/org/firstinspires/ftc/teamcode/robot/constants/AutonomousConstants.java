package org.firstinspires.ftc.teamcode.robot.constants;

import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;

public class AutonomousConstants {
    public static final double LOOKFORWARDCONSTANT = 2;
    public static final double WHEEL_RADIUS = 10; //cm
    public static final double MOTOR_REDUCTION = 125; //ticks per revolution of wheel
    public static final double TICK_TO_CM_CONVERSION_VALUE = 2*Math.PI*WHEEL_RADIUS/(MOTOR_REDUCTION);
    public static double MAXSPEED = 30; // centimeter per second
    public static  double MAXACCELERATION = 4; //centimeter per second squared
    public static final double SPEEDPROPORTIONALGAIN = 0;
    public PIDController pathFollowingPID = new PIDController(2,0,0,0); // do not touch kI or the robot goes crazy ü
    public PIDController stoppedAtPointPID = new PIDController(0.6,0,0,0);

}