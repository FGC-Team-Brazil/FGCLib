package org.firstinspires.ftc.teamcode.robot;

/**
 * Centralized storage for robot configuration values such as hardware names,
 * inversion settings, and control constants.
 *
 * <p>Organizing constants in a single location makes maintenance easier and
 * avoids hardcoded values throughout the codebase.
 */
public class Constants {

  /** Drivetrain hardware configuration. */
  public static class DrivetrainBuilderConstants {
    public static final String MOTOR_RIGHT = "drivetrain_motorRight";
    public static final String MOTOR_LEFT = "drivetrain_motorLeft";
    public static final boolean MOTOR_RIGHT_INVERTED = false;
    public static final boolean MOTOR_LEFT_INVERTED = true;
  }

  /** Configuration values for the example subsystem. */
  public static class SubsystemExample {
    public static final String MOTOR_LEFT = "subsystemExample_motorLeft";
    public static final String MOTOR_RIGHT = "subsystemExample_motorRight";
    public static final String LIMIT_LEFT = "subsystemExample_limitLeft";
    public static final String LIMIT_RIGHT = "subsystemExample_limitRight";

    /** PID tuning constants. */
    public static class PID {
      public static final double kP = 1.8;
      public static final double kI = 0.0;
      public static final double kD = 0.031;
      public static final double kF = 0.1;
    }
  }
}