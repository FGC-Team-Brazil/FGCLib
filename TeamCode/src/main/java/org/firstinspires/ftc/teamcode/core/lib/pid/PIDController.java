package org.firstinspires.ftc.teamcode.core.lib.pid;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * PIDF controller for FTC/FGC mechanisms that require stable closed-loop control.
 *
 * <p>This controller is designed for common robot subsystems such as:
 *
 * <ul>
 *   <li>Linear slides and elevators</li>
 *   <li>Rotating arms and turrets</li>
 *   <li>Shooters and flywheels</li>
 *   <li>Intake and conveyor mechanisms</li>
 * </ul>
 *
 * <p>It supports:
 *
 * <ul>
 *   <li><b>Position control</b> through {@link #calculate(double, double)}</li>
 *   <li><b>Velocity control</b> through {@link #runVelocity(DcMotor, double)}</li>
 *   <li><b>Trapezoidal motion profiling</b> for smoother movement</li>
 *   <li><b>Voltage compensation</b> to keep behavior consistent as battery voltage changes</li>
 * </ul>
 *
 * <p>Typical position-control usage:
 *
 * <pre>{@code
 * PIDController pid = new PIDController(kP, kI, kD, kF);
 * pid.enableMotionProfile(maxVelocity, maxAcceleration);
 * pid.enableVoltageCompensation(hardwareMap);
 *
 * double power = pid.calculate(targetTicks, motor.getCurrentPosition());
 * motor.setPower(power);
 * }</pre>
 *
 * <p>Typical velocity-control usage:
 *
 * <pre>{@code
 * PIDController pid = new PIDController(kP, kI, kD, kF);
 *
 * pid.runVelocity(shooterMotor, targetTicksPerSecond);
 * }</pre>
 */
public class PIDController {

  // ── PIDF gains ────────────────────────────────────────────────────────────

  private double kP, kI, kD, kF;

  // ── Core state ────────────────────────────────────────────────────────────

  private double lastError = 0;
  private double integralSum = 0;
  private double integralMax = Double.MAX_VALUE;
  private double tolerance = 0;
  private boolean atTarget = false;
  private double lastOutput = 0;
  // NaN signals "no target planned yet" and avoids direct double equality checks.
  private double lastTarget = Double.NaN;

  private final ElapsedTime timer = new ElapsedTime();

  // ── Motion profile (optional) ─────────────────────────────────────────────

  private boolean mpEnabled = false;
  private double mpMaxVelocity = 0;
  private double mpMaxAccel = 0;
  private double mpMaxDecel = 0;
  private double mpStartPos = 0;
  private double mpEndPos = 0;
  private double mpTotalDist = 0;
  private double mpDirection = 1;
  private double mpPeakVelocity = 0;
  private double mpAccelTime = 0;
  private double mpCruiseTime = 0;
  private double mpTotalTime = 0;
  private boolean mpActive = false;

  private final ElapsedTime mpTimer = new ElapsedTime();

  // ── Velocity state (isolated from position state) ─────────────────────────

  private double velLastError = 0;
  private double velIntegralSum = 0;
  private int velLastTicks = 0;
  private double measuredVelocity = 0;
  private final ElapsedTime velTimer = new ElapsedTime();

  // ── Voltage compensation (optional) ───────────────────────────────────────

  private boolean voltageCompEnabled = false;
  private double nominalVoltage = 12.0;
  private VoltageSensor voltageSensor = null;

  // ── Constructors ──────────────────────────────────────────────────────────

  /**
   * Creates a new PIDF controller instance.
   *
   * <p>The controller output is computed from position or velocity error and can
   * be used to drive a motor that moves a physical mechanism toward a target.
   *
   * @param kP proportional gain used to react to current error
   * @param kI integral gain used to reduce long-term steady-state error
   * @param kD derivative gain used to reduce overshoot and oscillation
   * @param kF feedforward gain used to offset known load or motion demand
   */
  public PIDController(double kP, double kI, double kD, double kF) {
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.kF = kF;
    timer.reset();
    velTimer.reset();
  }

  /**
   * Creates a new PID controller with no feedforward term.
   *
   * @param kP proportional gain used to react to current error
   * @param kI integral gain used to reduce long-term steady-state error
   * @param kD derivative gain used to reduce overshoot and oscillation
   */
  public PIDController(double kP, double kI, double kD) {
    this(kP, kI, kD, 0);
  }

  // ── Optional features ─────────────────────────────────────────────────────

  /**
   * Enables trapezoidal motion profiling with symmetric acceleration and deceleration.
   *
   * <p>This is useful for mechanisms that should not jump abruptly to a target,
   * such as slides, arms, and turret-like systems. The controller will generate
   * smoother intermediate setpoints to reduce mechanical shock and current spikes.
   *
   * @param maxVelocity peak velocity in ticks per second
   * @param maxAcceleration acceleration and deceleration in ticks per second squared
   * @return this controller instance for method chaining
   */
  public PIDController enableMotionProfile(double maxVelocity, double maxAcceleration) {
    return enableMotionProfile(maxVelocity, maxAcceleration, maxAcceleration);
  }

  /**
   * Enables trapezoidal motion profiling with independent acceleration and deceleration limits.
   *
   * <p>Use this when the mechanism can accelerate and brake at different rates,
   * which is common in real robot systems where gravity, inertia, or gear ratio
   * make motion asymmetric.
   *
   * @param maxVelocity peak velocity in ticks per second
   * @param maxAcceleration acceleration rate in ticks per second squared
   * @param maxDeceleration deceleration rate in ticks per second squared
   * @return this controller instance for method chaining
   */
  public PIDController enableMotionProfile(
          double maxVelocity, double maxAcceleration, double maxDeceleration) {
    mpEnabled = true;
    mpMaxVelocity = Math.abs(maxVelocity);
    mpMaxAccel = Math.abs(maxAcceleration);
    mpMaxDecel = Math.abs(maxDeceleration);
    return this;
  }

  /**
   * Enables automatic voltage compensation using a default nominal voltage of 12V.
   *
   * <p>This helps keep motor behavior more consistent as the battery voltage drops
   * during a match. Without compensation, the same tuning may feel stronger on a
   * fresh battery and weaker near the end of a match.
   *
   * @param hardwareMap active FTC hardware map
   * @return this controller instance for method chaining
   */
  public PIDController enableVoltageCompensation(HardwareMap hardwareMap) {
    return enableVoltageCompensation(hardwareMap, 12.0);
  }

  /**
   * Enables automatic voltage compensation using a custom nominal voltage.
   *
   * <p>Use the nominal voltage that matches the battery level used when tuning the
   * mechanism. This is especially helpful for flywheels, shooters, and arms that
   * need consistent output across different battery conditions.
   *
   * @param hardwareMap active FTC hardware map
   * @param nominalVoltage voltage used as the tuning reference
   * @return this controller instance for method chaining
   */
  public PIDController enableVoltageCompensation(HardwareMap hardwareMap, double nominalVoltage) {
    if (hardwareMap.voltageSensor.iterator().hasNext()) {
      this.voltageSensor = hardwareMap.voltageSensor.iterator().next();
      this.nominalVoltage = nominalVoltage;
      this.voltageCompEnabled = true;
    }
    return this;
  }

  // ── Main API ──────────────────────────────────────────────────────────────

  /**
   * Calculates the motor output required to drive a mechanism toward a target position.
   *
   * <p>This method is intended for mechanisms such as slides, arms, and turrets where
   * the robot must move to a target encoder position or angle and hold it steadily.
   *
   * <p>If motion profiling is enabled, the target is first transformed into a smoother
   * trajectory. If voltage compensation is enabled, the final output is scaled according
   * to the current battery voltage.
   *
   * @param target desired mechanism position in ticks, degrees, or another consistent unit
   * @param currentPosition current measured position from the encoder
   * @return motor power command to apply to the actuator
   */
  public double calculate(double target, double currentPosition) {
    double dt = timer.seconds();
    timer.reset();
    if (dt <= 0) dt = 0.02;

    // Replan motion profile when target changes.
    // Uses Double.isNaN + epsilon comparison to avoid unsafe double equality checks.
    if (mpEnabled && (Double.isNaN(lastTarget) || Math.abs(target - lastTarget) > 1e-9)) {
      planMotionProfile(currentPosition, target);
      lastTarget = target;
    }

    double profiledTarget = (mpEnabled && mpActive) ? getProfilePosition() : target;

    double error = profiledTarget - currentPosition;

    // Integral with windup guard.
    integralSum = clamp(integralSum + error * dt, -integralMax, integralMax);

    // Derivative.
    double derivative = (error - lastError) / dt;
    lastError = error;

    // PIDF output.
    double output = (kP * error) + (kI * integralSum) + (kD * derivative) + kF;

    // At target: hold with feedforward only.
    if (Math.abs(error) <= tolerance) {
      atTarget = true;
      output = kF;
    } else {
      atTarget = false;
    }

    // Voltage compensation.
    output = applyVoltageCompensation(output);

    lastOutput = output;
    return output;
  }

  /**
   * Applies the last calculated control output directly to a motor.
   *
   * <p>This method is meant to be called after {@link #calculate(double, double)} so the
   * most recent controller output is sent to the actuator driving the physical mechanism.
   *
   * @param motor motor receiving the calculated control effort
   */
  public void setPowerMotor(DcMotor motor) {
    motor.setPower(clamp(lastOutput, -1.0, 1.0));
  }

  /**
   * Controls motor speed using encoder feedback.
   *
   * <p>This mode is intended for flywheels, shooters, and other rotating mechanisms
   * that must maintain a stable rotational velocity rather than a target position.
   *
   * <p>The controller estimates speed from encoder tick changes over time and then
   * computes a power output that drives the motor toward the requested velocity.
   *
   * <p>This velocity loop uses its own internal error state and is isolated from
   * position control so both behaviors can coexist safely in the same subsystem.
   *
   * @param motor motor to drive; the encoder must be connected and producing valid ticks
   * @param target desired velocity in ticks per second
   */
  public void runVelocity(DcMotor motor, double target) {
    double dt = velTimer.seconds();
    velTimer.reset();
    if (dt <= 0) dt = 0.02;

    int currentTicks = motor.getCurrentPosition();
    measuredVelocity = (double) (currentTicks - velLastTicks) / dt;
    velLastTicks = currentTicks;

    double error = target - measuredVelocity;

    // Isolated integral — does not share state with calculate().
    velIntegralSum = clamp(velIntegralSum + error * dt, -integralMax, integralMax);
    double derivative = (error - velLastError) / dt;
    velLastError = error;

    double output = (kP * error) + (kI * velIntegralSum) + (kD * derivative) + (kF * target);
    output = applyVoltageCompensation(output);
    lastOutput = output;

    motor.setPower(clamp(output, -1.0, 1.0));
  }

  /**
   * Resets all controller state.
   *
   * <p>This clears position and velocity history, cancels the current motion profile,
   * and resets timing references. It should be called when the subsystem is re-enabled,
   * when the robot changes control mode, or when a new target requires a clean start.
   */
  public void reset() {
    integralSum = 0;
    lastError = 0;
    lastOutput = 0;
    lastTarget = Double.NaN;
    atTarget = false;
    mpActive = false;
    velIntegralSum = 0;
    velLastError = 0;
    velLastTicks = 0;
    measuredVelocity = 0;
    timer.reset();
    velTimer.reset();
  }

  // ── Internal helpers ──────────────────────────────────────────────────────

  /**
   * Applies battery-voltage scaling to the output power.
   *
   * <p>When enabled, the controller compensates for voltage sag so the mechanism
   * behaves more consistently across the battery discharge curve.
   *
   * @param power raw controller output
   * @return compensated output, or the original output if compensation is disabled
   */
  private double applyVoltageCompensation(double power) {
    if (!voltageCompEnabled || voltageSensor == null) return power;
    double voltage = voltageSensor.getVoltage();
    if (voltage <= 0) return power;
    return power * (nominalVoltage / voltage);
  }

  /**
   * Plans a trapezoidal or triangular motion profile between two positions.
   *
   * <p>This generates a smoother movement path for mechanisms that should accelerate,
   * travel, and decelerate in a controlled way instead of jumping directly to the target.
   *
   * @param from current mechanism position
   * @param to desired mechanism position
   */
  private void planMotionProfile(double from, double to) {
    mpStartPos = from;
    mpEndPos = to;
    mpTotalDist = Math.abs(to - from);
    mpDirection = (to >= from) ? 1.0 : -1.0;

    if (mpTotalDist < 1e-6) {
      mpActive = false;
      return;
    }

    double tAccel = mpMaxVelocity / mpMaxAccel;
    double tDecel = mpMaxVelocity / mpMaxDecel;
    double dAccel = 0.5 * mpMaxAccel * tAccel * tAccel;
    double dDecel = 0.5 * mpMaxDecel * tDecel * tDecel;

    if (dAccel + dDecel > mpTotalDist) {
      // Triangle profile — too short for a cruise phase.
      double rate = (1.0 / mpMaxAccel) + (1.0 / mpMaxDecel);
      mpPeakVelocity = Math.sqrt(2.0 * mpTotalDist / rate);
      mpAccelTime = mpPeakVelocity / mpMaxAccel;
      mpCruiseTime = 0;
    } else {
      // Full trapezoidal profile.
      mpPeakVelocity = mpMaxVelocity;
      mpAccelTime = tAccel;
      mpCruiseTime = (mpTotalDist - dAccel - dDecel) / mpMaxVelocity;
    }

    mpTotalTime = mpAccelTime + mpCruiseTime + (mpPeakVelocity / mpMaxDecel);
    mpTimer.reset();
    mpActive = true;
  }

  /**
   * Computes the current position setpoint generated by the active motion profile.
   *
   * <p>The returned value is a smoothed intermediate target that gradually advances
   * from the start position to the final target while respecting the configured
   * motion limits.
   *
   * @return current profiled position setpoint
   */
  private double getProfilePosition() {
    double t = mpTimer.seconds();
    double decelStart = mpAccelTime + mpCruiseTime;

    if (t >= mpTotalTime) {
      mpActive = false;
      return mpEndPos;
    }

    double displacement;
    if (t <= mpAccelTime) {
      displacement = 0.5 * mpMaxAccel * t * t;
    } else if (t <= decelStart) {
      double dAccel = 0.5 * mpMaxAccel * mpAccelTime * mpAccelTime;
      displacement = dAccel + mpPeakVelocity * (t - mpAccelTime);
    } else {
      double dAccel = 0.5 * mpMaxAccel * mpAccelTime * mpAccelTime;
      double dCruise = mpPeakVelocity * mpCruiseTime;
      double tD = t - decelStart;
      displacement = dAccel + dCruise + mpPeakVelocity * tD - 0.5 * mpMaxDecel * tD * tD;
    }

    return mpStartPos + mpDirection * Math.min(displacement, mpTotalDist);
  }

  /**
   * Clamps a value to the provided inclusive range.
   *
   * @param value value to clamp
   * @param min minimum allowed value
   * @param max maximum allowed value
   * @return clamped value
   */
  private double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }

  // ── Setters ───────────────────────────────────────────────────────────────

  /**
   * Updates the proportional gain.
   *
   * @param kP new proportional gain
   */
  public void setKP(double kP) {
    this.kP = kP;
  }

  /**
   * Updates the integral gain.
   *
   * @param kI new integral gain
   */
  public void setKI(double kI) {
    this.kI = kI;
  }

  /**
   * Updates the derivative gain.
   *
   * @param kD new derivative gain
   */
  public void setKD(double kD) {
    this.kD = kD;
  }

  /**
   * Updates the feedforward gain.
   *
   * @param kF new feedforward gain
   */
  public void setKF(double kF) {
    this.kF = kF;
  }

  /**
   * Sets the acceptable error band for target detection.
   *
   * <p>When the absolute error falls within this range, the controller considers
   * the mechanism close enough to the target to hold position instead of continuing
   * aggressive correction.
   *
   * @param tolerance acceptable error in the same unit used by {@link #calculate(double, double)}
   */
  public void setTolerance(double tolerance) {
    this.tolerance = Math.abs(tolerance);
  }

  /**
   * Sets the maximum absolute integral accumulation.
   *
   * <p>This limits integral windup so the mechanism does not overshoot heavily after
   * long periods of saturation or stall.
   *
   * @param integralMax maximum absolute integral value
   */
  public void setIntegralMax(double integralMax) {
    this.integralMax = Math.abs(integralMax);
  }

  // ── Getters ───────────────────────────────────────────────────────────────

  /**
   * Returns the proportional gain.
   *
   * @return current {@code kP} value
   */
  public double getKP() {
    return kP;
  }

  /**
   * Returns the integral gain.
   *
   * @return current {@code kI} value
   */
  public double getKI() {
    return kI;
  }

  /**
   * Returns the derivative gain.
   *
   * @return current {@code kD} value
   */
  public double getKD() {
    return kD;
  }

  /**
   * Returns the feedforward gain.
   *
   * @return current {@code kF} value
   */
  public double getKF() {
    return kF;
  }

  /**
   * Returns the most recent controller output.
   *
   * @return last computed motor command
   */
  public double getLastOutput() {
    return lastOutput;
  }

  /**
   * Returns the measured velocity used by the velocity loop.
   *
   * @return measured encoder velocity in ticks per second
   */
  public double getMeasuredVelocity() {
    return measuredVelocity;
  }

  /**
   * Indicates whether the controller is inside the configured position tolerance.
   *
   * @return {@code true} when the controller considers the mechanism at target
   */
  public boolean atTarget() {
    return atTarget;
  }

  /**
   * Indicates whether the active motion profile has finished.
   *
   * @return {@code true} when no motion profile is currently active
   */
  public boolean isProfileDone() {
    return !mpActive;
  }
}