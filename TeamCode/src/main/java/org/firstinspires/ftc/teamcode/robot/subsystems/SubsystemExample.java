package org.firstinspires.ftc.teamcode.robot.subsystems;

import Ori.Coval.Logging.AutoLog;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;
import org.firstinspires.ftc.teamcode.robot.Constants;

/**
 * Example subsystem for FGCLib.
 *
 * <p>This subsystem isolates the hardware logic from the control logic. Methods like setPower() and
 * setTargetAngle() should be called by Commands or the RobotContainer to control the mechanism. The
 * execute() method runs the PID loop and hardware protections automatically.
 */
@AutoLog
public class SubsystemExample implements Subsystem {

  private static SubsystemExampleAutoLogged instance;

  private DcMotor motorRight;
  private DcMotor motorLeft;
  private TouchSensor limitRight;
  private TouchSensor limitLeft;

  private PIDController pidController;

  private final double TICKS_PER_REV = 537.7;
  private final double TICKS_PER_DEGREE = TICKS_PER_REV / 360.0;

  private boolean isPidEnabled = false;
  private double targetAngle = 0.0;
  private double manualPower = 0.0;

  protected SubsystemExample() {}

  // ── Lifecycle ──────────────────────────────────────────────────────────────
  @Override
  public void initialize(HardwareMap hardwareMap) {
    motorRight = hardwareMap.get(DcMotor.class, Constants.SubsystemExample.MOTOR_RIGHT);
    motorLeft = hardwareMap.get(DcMotor.class, Constants.SubsystemExample.MOTOR_LEFT);
    limitRight = hardwareMap.get(TouchSensor.class, Constants.SubsystemExample.LIMIT_RIGHT);
    limitLeft = hardwareMap.get(TouchSensor.class, Constants.SubsystemExample.LIMIT_LEFT);

    motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    pidController =
        new PIDController(
            Constants.SubsystemExample.PID.kP,
            Constants.SubsystemExample.PID.kI,
            Constants.SubsystemExample.PID.kD,
            Constants.SubsystemExample.PID.kF);
    pidController.enableMotionProfile(2200, 4400);
    pidController.enableVoltageCompensation(hardwareMap);
  }

  @Override
  public void start() {
    // Called when the OpMode starts
  }

  /**
   * Periodic method called every loop iteration. Handles hardware safety (limit switches) and PID
   * calculations.
   */
  @Override
  public void execute() {
    int currentPosition = motorLeft.getCurrentPosition();
    double currentAngle = currentPosition / TICKS_PER_DEGREE;

    if (isLimitLeft()) {
      resetEncoderSafely(motorLeft);
      currentPosition = 0;
    }
    if (isLimitRight()) {
      resetEncoderSafely(motorRight);
    }

    double power = 0.0;

    if (isPidEnabled) {
      double targetTicks = targetAngle * TICKS_PER_DEGREE;
      power = pidController.calculate(targetTicks, currentPosition);
    } else {
      pidController.reset();
      power = manualPower;
    }

    if (isLimitLeft() && power < 0) {
      power = 0;
    }
    if (isLimitRight() && power > 0) {
      power = 0;
    }

    motorLeft.setPower(power);
    motorRight.setPower(power);
  }

  @Override
  public void stop() {
    setPower(0);
    isPidEnabled = false;
  }

  // ── Control Methods ───────────────────────────────────────────────────────

  /**
   * Sets the manual power to the motors and disables the PID controller.
   *
   * @param power Motor power from -1.0 to 1.0
   */
  public void setPower(double power) {
    this.isPidEnabled = false;
    this.manualPower = power;
  }

  /**
   * Sets the target angle and enables the PID controller.
   *
   * @param angle Target angle in degrees
   */
  public void setTargetAngle(double angle) {
    this.targetAngle = angle;
    this.isPidEnabled = true;
  }

  // ── Hardware Helpers ──────────────────────────────────────────────────────

  /**
   * Safely resets the motor encoder without completely stopping the loop flow.
   *
   * @param motor The DcMotor to reset
   */
  private void resetEncoderSafely(DcMotor motor) {
    if (motor.getCurrentPosition() != 0) {
      motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      pidController.reset();
    }
  }

  /** Resets both motor encoders manually. */
  public void resetEncoders() {
    resetEncoderSafely(motorLeft);
    resetEncoderSafely(motorRight);
  }

  public boolean isLimitRight() {
    return limitRight.isPressed();
  }

  public boolean isLimitLeft() {
    return limitLeft.isPressed();
  }

  // ── Singleton ─────────────────────────────────────────────────────────────

  /**
   * Returns the singleton instance of the subsystem.
   *
   * @return SubsystemExample instance
   */
  public static synchronized SubsystemExampleAutoLogged getInstance() {
    if (instance == null) {
      instance = new SubsystemExampleAutoLogged();
    }
    return instance;
  }
}
