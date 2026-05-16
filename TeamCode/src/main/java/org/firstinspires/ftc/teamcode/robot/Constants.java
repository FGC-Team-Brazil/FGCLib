package org.firstinspires.ftc.teamcode.robot;

public class Constants {
    // Global constants
    public static class Globals {
        public static class Controller {
            public static final double TRIGGER_PRESSED_THRESHOLD_VALUE = 0.5;
            public static final double CONTROLLER_DEADBAND = 0.1;
        }
    }

    public static class Drivetrain {
        public static final double LIMITER_MIN = 0.7;
        public static final double LIMITER_DEFAULT = 1.0;

        public static final String MOTOR_RIGHT_NAME = "motor_right";
        public static final String MOTOR_LEFT_NAME = "motor_left";
        public static final boolean IS_MOTOR_RIGHT_INVERTED = false;
        public static final boolean IS_MOTOR_LEFT_INVERTED = true;
    }

}
