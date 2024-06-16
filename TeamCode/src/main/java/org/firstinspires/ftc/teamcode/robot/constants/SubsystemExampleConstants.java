package org.firstinspires.ftc.teamcode.robot.constants;

/**
 * SubsystemExampleConstants is a example of constant class.
 * You would like to have a constant class for every subsystem.
 */
public class SubsystemExampleConstants {
    public static final String MOTOR_LEFT = "subsystemExample_motorLeft";
    public static final String MOTOR_RIGHT = "subsystemExample_motorRight";
    public static final String LIMIT_LEFT = "subsystemExample_limitLeft";
    public static final String LIMIT_RIGHT = "subsystemExample_limitRight";
    public static final int TARGET_DEGREE = 110;


    public static class PID {
        public static final double kP = 1.8;
        public static final double kI = 0.0;
        public static final double kD = 0.031;
        public static final double kF = 0.1;
    }
}
