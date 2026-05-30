package org.firstinspires.ftc.teamcode.robot;

public class Constants {

  public class DrivetrainBuilderConstants {
    public static final String MOTOR_RIGHT = "drivetrain_motorRight";
    public static final String MOTOR_LEFT = "drivetrain_motorLeft";
    public static final boolean MOTOR_RIGHT_INVERTED = false;
    public static final boolean MOTOR_LEFT_INVERTED = true;
  }

  public static class SubsystemExample {
    public static final String MOTOR_LEFT = "subsystemExample_motorLeft";
    public static final String MOTOR_RIGHT = "subsystemExample_motorRight";
    public static final String LIMIT_LEFT = "subsystemExample_limitLeft";
    public static final String LIMIT_RIGHT = "subsystemExample_limitRight";

    /**
     * It is possible to subdivide constants by using subclasses for further organization
     *
     * <p>the following example shows how to do this to a PID's constants
     */
    public static class PID {
      public static final double kP = 1.8;
      public static final double kI = 0.0;
      public static final double kD = 0.031;
      public static final double kF = 0.1;
    }
  }
}
