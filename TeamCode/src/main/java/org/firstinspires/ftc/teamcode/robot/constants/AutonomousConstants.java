package org.firstinspires.ftc.teamcode.robot.constants;

import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;

public class AutonomousConstants {
    public static final double LOOKFORWARDCONSTANT = 2;
    public static double MAXSPEED = 30; // centimeter per second
    public static  double MAXACCELERATION = 4; //centimeter per second squared
    public static final double SPEEDLOOKFORWARDGAIN = 0;
    public PIDController pathFollowingPID = new PIDController(2,0,0,0); // do not touch kI or the robot goes crazy ü
    public PIDController stoppedAtPointPID = new PIDController(0.6,0,0,0);

}
