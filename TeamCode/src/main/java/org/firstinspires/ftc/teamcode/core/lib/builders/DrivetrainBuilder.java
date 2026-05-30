package org.firstinspires.ftc.teamcode.core.lib.builders;

import Ori.Coval.Logging.Logger.KoalaLog;
import androidx.annotation.NonNull;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;

/**
 * Utility subsystem for creating and controlling a two-motor differential drivetrain.
 *
 * <p>This builder encapsulates hardware initialization and basic arcade-drive calculations,
 * allowing teams to quickly configure tank-style drivetrains without creating a dedicated
 * subsystem.
 *
 * <ul>
 *   <li>Supports two drive motors (left and right)
 *   <li>Provides arcade drive control
 *   <li>Supports motor direction inversion
 *   <li>Automatically logs motor outputs through {@link KoalaLog}
 * </ul>
 *
 * <p><b>Note:</b> This class does not support mecanum, omni, or other holonomic drivetrain
 * configurations.
 */
public class DrivetrainBuilder implements Subsystem {
  private static DrivetrainBuilder instance;
  private DcMotorSimple.Direction motorRightDirection;
  private DcMotorSimple.Direction motorLeftDirection;
  private String motorLeftName;
  private String motorRightName;
  private double limiter;
  private DcMotor motorRight;
  private DcMotor motorLeft;

  protected DrivetrainBuilder() {}

  /**
   * Configures the drivetrain hardware before initialization.
   *
   * <p>This method defines the motor names used in the robot configuration and the direction
   * required for each side to move the robot forward.
   *
   * @param motorRightName configuration name of the right drive motor
   * @param motorLeftName configuration name of the left drive motor
   * @param isMotorRightInverted {@code true} if the right motor must be reversed
   * @param isMotorLeftInverted {@code true} if the left motor must be reversed
   * @return configured drivetrain instance
   */
  public static DrivetrainBuilder build(
      @NonNull String motorRightName,
      @NonNull String motorLeftName,
      boolean isMotorRightInverted,
      boolean isMotorLeftInverted) {
    getInstance();

    instance.motorLeftName = motorLeftName;
    instance.motorRightName = motorRightName;
    instance.motorLeftDirection =
        isMotorLeftInverted ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
    instance.motorRightDirection =
        isMotorRightInverted ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;

    return instance;
  }

  /**
   * Maps and configures the drivetrain motors from the robot hardware.
   *
   * <p>This method must be called during subsystem initialization before any drive commands are
   * executed.
   *
   * @param hardwareMap FTC hardware map used to retrieve motor devices
   */
  @Override
  public void initialize(HardwareMap hardwareMap) {
    motorLeft = hardwareMap.get(DcMotor.class, motorLeftName);
    motorRight = hardwareMap.get(DcMotor.class, motorRightName);
    motorLeft.setDirection(motorLeftDirection);
    motorRight.setDirection(motorRightDirection);
  }

  /** Called when the robot enters the start phase. */
  @Override
  public void start() {}

  /** Periodic drivetrain execution method. */
  @Override
  public void execute() {}

  /**
   * Stops all drivetrain movement.
   *
   * <p>This method is automatically called when the robot is disabled or the OpMode ends.
   */
  @Override
  public void stop() {
    setPower(0, 0);
  }

  /**
   * Drives the robot using arcade-drive control with an output limiter.
   *
   * <p>{@code xSpeed} controls forward/backward movement while {@code zRotation} controls turning.
   *
   * @param xSpeed forward/backward command
   * @param zRotation rotational command
   * @param limit maximum output magnitude applied to inputs
   */
  public void arcadeDrive(double xSpeed, double zRotation, double limit) {
    limiter = limit;
    calculatePower(xSpeed, zRotation);
  }

  /**
   * Drives the robot using arcade-drive control at full output range.
   *
   * @param xSpeed forward/backward command
   * @param zRotation rotational command
   */
  public void arcadeDrive(double xSpeed, double zRotation) {
    limiter = 1;
    calculatePower(xSpeed, zRotation);
  }

  /**
   * Converts arcade-drive commands into left and right motor powers.
   *
   * @param xSpeed forward/backward command
   * @param zRotation rotational command
   */
  private void calculatePower(double xSpeed, double zRotation) {
    double xSpeedLimited = Math.max(-limiter, Math.min(limiter, xSpeed));
    double zRotationLimited = Math.max(-limiter, Math.min(limiter, zRotation));

    double leftSpeed = xSpeedLimited - zRotationLimited;
    double rightSpeed = xSpeedLimited + zRotationLimited;

    setPower(leftSpeed, rightSpeed);
  }

  /**
   * Applies power directly to the drivetrain motors.
   *
   * <p>Positive values drive the robot forward according to the configured motor directions.
   *
   * @param leftPower power applied to the left motor
   * @param rightPower power applied to the right motor
   */
  public void setPower(double leftPower, double rightPower) {
    motorLeft.setPower(leftPower);
    motorRight.setPower(rightPower);
    KoalaLog.log("Left_Power", leftPower, true);
    KoalaLog.log("Right_Power", rightPower, true);
  }

  /**
   * Returns the singleton drivetrain instance.
   *
   * <p>Only one drivetrain should exist on a robot. This method guarantees that a single shared
   * instance is used throughout the application.
   *
   * @return singleton {@link DrivetrainBuilder} instance
   */
  public static DrivetrainBuilder getInstance() {
    if (instance == null) {
      synchronized (DrivetrainBuilder.class) {
        if (instance == null) {
          instance = new DrivetrainBuilder();
        }
      }
    }
    return instance;
  }
}
