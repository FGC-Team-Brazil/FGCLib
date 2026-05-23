package org.firstinspires.ftc.teamcode.core.lib.pid;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * PIDController — FGCLib's all-in-one motor control class.
 *
 * <p>── Basic usage ───────────────────────────────────────────────────────
 *
 * <p>// In initialize(): pidController = new PIDController(kP, kI, kD, kF);
 *
 * <p>// In execute(): motor.setPower(pidController.calculate(TARGET, motor.getCurrentPosition()));
 *
 * <p>── With smooth movement ──────────────────────────────────────────────
 *
 * <p>// In initialize(): pidController = new PIDController(kP, kI, kD, kF);
 * pidController.enableMotionProfile(maxVelocity, maxAcceleration);
 *
 * <p>── With voltage compensation ─────────────────────────────────────────
 *
 * <p>// In initialize(): pidController = new PIDController(kP, kI, kD, kF);
 * pidController.enableVoltageCompensation(hardwareMap);
 *
 * <p>── All features together ─────────────────────────────────────────────
 *
 * <p>// In initialize(): pidController = new PIDController(kP, kI, kD, kF);
 * pidController.enableMotionProfile(maxVelocity, maxAcceleration)
 * .enableVoltageCompensation(hardwareMap);
 *
 * <p>── Velocity control (launchers, shooters) ────────────────────────────
 *
 * <p>// In execute(): pidController.runVelocity(motor, TARGET_TICKS_PER_SEC);
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
  // NaN signals "no target planned yet" — avoids double == double comparison
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
   * Creates a PIDController.
   *
   * @param kP Proportional gain — how fast it reacts to error
   * @param kI Integral gain — corrects long-term error (start at 0)
   * @param kD Derivative gain — reduces oscillation (start at 0)
   * @param kF Feedforward gain — constant offset (gravity hold or speed drive)
   */
  public PIDController(double kP, double kI, double kD, double kF) {
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.kF = kF;
    timer.reset();
    velTimer.reset();
  }

  /** Shorthand constructor with kF = 0. */
  public PIDController(double kP, double kI, double kD) {
    this(kP, kI, kD, 0);
  }

  // ── Optional features ─────────────────────────────────────────────────────

  /**
   * Enables trapezoidal motion profiling with symmetric accel/decel. The motor accelerates smoothly
   * to maxVelocity, cruises, then decelerates. Call this in initialize().
   *
   * @param maxVelocity Peak velocity (ticks/s)
   * @param maxAcceleration Acceleration and deceleration (ticks/s²)
   * @return this, for method chaining
   */
  public PIDController enableMotionProfile(double maxVelocity, double maxAcceleration) {
    return enableMotionProfile(maxVelocity, maxAcceleration, maxAcceleration);
  }

  /**
   * Enables trapezoidal motion profiling with asymmetric accel/decel.
   *
   * @param maxVelocity Peak velocity (ticks/s)
   * @param maxAcceleration Acceleration rate (ticks/s²)
   * @param maxDeceleration Deceleration rate (ticks/s²)
   * @return this, for method chaining
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
   * Enables automatic voltage compensation at 12V nominal. Robot behavior stays consistent as the
   * battery drains during a match. Call this in initialize().
   *
   * @param hardwareMap HardwareMap from the OpMode
   * @return this, for method chaining
   */
  public PIDController enableVoltageCompensation(HardwareMap hardwareMap) {
    return enableVoltageCompensation(hardwareMap, 12.0);
  }

  /**
   * Enables automatic voltage compensation with a custom nominal voltage.
   *
   * @param hardwareMap HardwareMap from the OpMode
   * @param nominalVoltage Voltage at which your constants were tuned (default 12.0V)
   * @return this, for method chaining
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
   * Calculates the motor power to reach the target position. Call this every loop iteration in
   * execute().
   *
   * <p>If motion profile is enabled, the target is smoothed automatically. If voltage compensation
   * is enabled, output is scaled automatically.
   *
   * @param target Desired target (ticks or degrees)
   * @param currentPosition Current measured position from the encoder
   * @return Motor power
   */
  public double calculate(double target, double currentPosition) {
    double dt = timer.seconds();
    timer.reset();
    if (dt <= 0) dt = 0.02;

    // Replan motion profile when target changes.
    // Uses Double.isNaN + epsilon comparison to avoid unsafe double == double.
    if (mpEnabled && (Double.isNaN(lastTarget) || Math.abs(target - lastTarget) > 1e-9)) {
      planMotionProfile(currentPosition, target);
      lastTarget = target;
    }

    double profiledTarget = (mpEnabled && mpActive) ? getProfilePosition() : target;

    double error = profiledTarget - currentPosition;

    // Integral with windup guard
    integralSum = clamp(integralSum + error * dt, -integralMax, integralMax);

    // Derivative
    double derivative = (error - lastError) / dt;
    lastError = error;

    // Output
    double output = (kP * error) + (kI * integralSum) + (kD * derivative) + kF;

    // At target: hold with kF only
    if (Math.abs(error) <= tolerance) {
      atTarget = true;
      output = kF;
    } else {
      atTarget = false;
    }

    // Voltage compensation
    output = applyVoltageCompensation(output);

    lastOutput = output;
    return output;
  }

  /**
   * Sets power directly on a motor using the last calculated output. Call calculate() first, then
   * setPowerMotor() to apply.
   *
   * @param motor Motor to drive
   */
  public void setPowerMotor(DcMotor motor) {
    motor.setPower(clamp(lastOutput, -1.0, 1.0));
  }

  /**
   * [Velocity control] Measures speed from encoder and applies PID automatically. Call every loop
   * iteration in execute().
   *
   * <p>Uses its own isolated PID state — safe to use alongside calculate() in the same subsystem
   * without state corruption.
   *
   * @param motor Motor to drive (encoder must be connected)
   * @param target Desired speed in ticks/s
   */
  public void runVelocity(DcMotor motor, double target) {
    double dt = velTimer.seconds();
    velTimer.reset();
    if (dt <= 0) dt = 0.02;

    int currentTicks = motor.getCurrentPosition();
    // Cast to double before subtraction to preserve precision
    measuredVelocity = (double) (currentTicks - velLastTicks) / dt;
    velLastTicks = currentTicks;

    double error = target - measuredVelocity;
    // Isolated integral — does not share state with calculate()
    velIntegralSum = clamp(velIntegralSum + error * dt, -integralMax, integralMax);
    double derivative = (error - velLastError) / dt;
    velLastError = error;

    double output = (kP * error) + (kI * velIntegralSum) + (kD * derivative) + (kF * target);
    output = applyVoltageCompensation(output);
    lastOutput = output;

    motor.setPower(clamp(output, -1.0, 1.0));
  }

  /**
   * Resets all PID state — position and velocity. Call when re-enabling after a pause or changing
   * targets abruptly.
   */
  public void reset() {
    integralSum = 0;
    lastError = 0;
    lastOutput = 0;
    lastTarget = Double.NaN;
    atTarget = false;
    velIntegralSum = 0;
    velLastError = 0;
    velLastTicks = 0;
    measuredVelocity = 0;
    timer.reset();
    velTimer.reset();
  }

  // ── Internal helpers ──────────────────────────────────────────────────────

  private double applyVoltageCompensation(double power) {
    if (!voltageCompEnabled || voltageSensor == null) return power;
    double voltage = voltageSensor.getVoltage();
    if (voltage <= 0) return power;
    return power * (nominalVoltage / voltage);
  }

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
      // Triangle profile — too short for a cruise phase
      double rate = (1.0 / mpMaxAccel) + (1.0 / mpMaxDecel);
      mpPeakVelocity = Math.sqrt(2.0 * mpTotalDist / rate);
      mpAccelTime = mpPeakVelocity / mpMaxAccel;
      mpCruiseTime = 0;
    } else {
      // Full trapezoidal profile
      mpPeakVelocity = mpMaxVelocity;
      mpAccelTime = tAccel;
      mpCruiseTime = (mpTotalDist - dAccel - dDecel) / mpMaxVelocity;
    }

    mpTotalTime = mpAccelTime + mpCruiseTime + (mpPeakVelocity / mpMaxDecel);
    mpTimer.reset();
    mpActive = true;
  }

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

  private double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }

  // ── Setters ───────────────────────────────────────────────────────────────

  public void setKP(double kP) {
    this.kP = kP;
  }

  public void setKI(double kI) {
    this.kI = kI;
  }

  public void setKD(double kD) {
    this.kD = kD;
  }

  public void setKF(double kF) {
    this.kF = kF;
  }

  /**
   * Tolerance in ticks or degrees. Within this range the controller considers itself at target and
   * holds with kF only. Default is 0 — set this to a small value (e.g. 10 ticks) for atTarget() to
   * work.
   */
  public void setTolerance(double tolerance) {
    this.tolerance = Math.abs(tolerance);
  }

  /** Prevents integral windup. A good starting value is 1.0 / kI. Default: unlimited. */
  public void setIntegralMax(double integralMax) {
    this.integralMax = Math.abs(integralMax);
  }

  // ── Getters ───────────────────────────────────────────────────────────────

  public double getKP() {
    return kP;
  }

  public double getKI() {
    return kI;
  }

  public double getKD() {
    return kD;
  }

  public double getKF() {
    return kF;
  }

  public double getLastOutput() {
    return lastOutput;
  }

  public double getMeasuredVelocity() {
    return measuredVelocity;
  }

  public boolean atTarget() {
    return atTarget;
  }

  public boolean isProfileDone() {
    return !mpActive;
  }
}
