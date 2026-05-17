package org.firstinspires.ftc.teamcode.robot;

public class Constants {

    // Global constants
    public static class Globals {
        public static final int CORE_HEX_TICKS_PER_REVOLUTION = 288;
    }

    public static class Drivetrain {
        public static final double LIMITER_MIN = 0.7;
        public static final double LIMITER_DEFAULT = 1.0;

        public static final String MOTOR_RIGHT_NAME = "motor_right";
        public static final String MOTOR_LEFT_NAME = "motor_left";
        public static final boolean IS_MOTOR_RIGHT_INVERTED = false;
        public static final boolean IS_MOTOR_LEFT_INVERTED = true;
    }

    public static class SubsystemExample {
        public static final String MOTOR_LEFT = "subsystemExample_motorLeft";
        public static final String MOTOR_RIGHT = "subsystemExample_motorRight";
        public static final String LIMIT_LEFT = "subsystemExample_limitLeft";
        public static final String LIMIT_RIGHT = "subsystemExample_limitRight";

        /**
         * It is possible to subdivide constants by using subclasses for further organization
         * <p>the following example shows how to do this to a PID's constants</p>
         */
        public static class PID {
            public static final double kP = 1.8;
            public static final double kI = 0.0;
            public static final double kD = 0.031;
            public static final double kF = 0.1;
        }
    }
}
