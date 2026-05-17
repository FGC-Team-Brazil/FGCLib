package org.firstinspires.ftc.teamcode.core.lib.pid;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * PIDController — FGCLib's all-in-one motor control class.
 *
 * ── Beginner: use a factory method ───────────────────────────────────
 *
 *   Factory methods choose the right mode automatically.
 *   Pick the one that matches your mechanism:
 *
 *   PIDController pid = PIDController.forSlide(kP, kI, kD);           // horizontal slides
 *   PIDController pid = PIDController.forElevator(kP, kI, kD, kF);    // vertical elevator / lift
 *   PIDController pid = PIDController.forArm(kP, kI, kD, kF);         // rotating arms
 *   PIDController pid = PIDController.forFlywheel(kP, kF);            // launchers / shooters
 *
 *   // Set the target once (e.g. when a button is pressed):
 *   pid.setTarget(1000, motor.getCurrentPosition());
 *
 *   // In execute() — one line, that's it:
 *   motor.setPower(pid.calculate(motor.getCurrentPosition()));
 *
 *   // For flywheels, use runVelocity() instead:
 *   pid.runVelocity(motor);
 *
 * ── Advanced: use the full constructor ────────────────────────────────
 *
 *   PIDController pid = new PIDController(kP, kI, kD, kF, Mode.POSITION);
 *
 * ── Optional features (both beginner and advanced) ────────────────────
 *
 *   pid.enableMotionProfile(maxVelocity, maxAcceleration); // smooth movement
 *   pid.enableVoltageCompensation(hardwareMap);            // stable power as battery drains
 *
 *   // Features chain together:
 *   pid.enableMotionProfile(2000, 4000)
 *      .enableVoltageCompensation(hardwareMap);
 *
 * ── Modes (for reference) ─────────────────────────────────────────────
 *
 *   POSITION — linear slides, robot movement       → calculate() / run()
 *   ANGLE    — rotating arms, gravity compensation → calculate() / run()
 *   VELOCITY — launchers, shooters, flywheels      → calculateVelocity() / runVelocity()
 */
public class PIDController {

    // ── Mode ──────────────────────────────────────────────────────────────────

    public enum Mode {
        /** Linear movement: slides, linear actuators, robot translation. */
        POSITION,
        /** Rotating arms. Feedforward uses cos(angle) to compensate gravity. */
        ANGLE,
        /** Speed control: launchers, flywheels. Use calculateVelocity() / runVelocity(). */
        VELOCITY
    }

    // ── PIDF gains ────────────────────────────────────────────────────────────

    private double kP, kI, kD, kF;

    // ── Core state ────────────────────────────────────────────────────────────

    private final Mode mode;
    private double  target      = 0;
    private double  lastError   = 0;
    private double  integralSum = 0;
    private double  integralMax = Double.MAX_VALUE;
    private double  tolerance   = 0;
    private boolean atTarget    = false;
    private double  lastOutput  = 0;

    private final ElapsedTime timer = new ElapsedTime();

    // ── Motion profile ────────────────────────────────────────────────────────

    private boolean mpEnabled       = false;
    private double  mpMaxVelocity   = 0;
    private double  mpMaxAccel      = 0;
    private double  mpMaxDecel      = 0;
    private double  mpStartPos      = 0;
    private double  mpEndPos        = 0;
    private double  mpTotalDist     = 0;
    private double  mpDirection     = 1;
    private double  mpPeakVelocity  = 0;
    private double  mpAccelTime     = 0;
    private double  mpCruiseTime    = 0;
    private double  mpTotalTime     = 0;
    private boolean mpActive        = false;

    private final ElapsedTime mpTimer = new ElapsedTime();

    // ── Velocity measurement (VELOCITY mode) ──────────────────────────────────

    private int    velLastTicks = 0;
    private double measuredVelocity = 0;
    private final ElapsedTime velTimer = new ElapsedTime();

    // ── Voltage compensation ──────────────────────────────────────────────────

    private boolean       voltageCompEnabled = false;
    private double        nominalVoltage     = 12.0;
    private VoltageSensor voltageSensor      = null;

    // ── Constructors ──────────────────────────────────────────────────────────

    /**
     * Creates a PIDController.
     *
     * @param kP   Proportional gain
     * @param kI   Integral gain
     * @param kD   Derivative gain
     * @param kF   Feedforward gain
     * @param mode POSITION, ANGLE, or VELOCITY
     */
    public PIDController(double kP, double kI, double kD, double kF, Mode mode) {
        this.kP   = kP;
        this.kI   = kI;
        this.kD   = kD;
        this.kF   = kF;
        this.mode = mode;
        timer.reset();
        velTimer.reset();
    }

    /**
     * Shorthand constructor with kF = 0.
     */
    public PIDController(double kP, double kI, double kD, Mode mode) {
        this(kP, kI, kD, 0, mode);
    }

    // ── Factory methods (beginner-friendly) ───────────────────────────────────

    /**
     * Creates a PIDController for a linear slide or any horizontal linear mechanism.
     *
     * Use calculate(motor.getCurrentPosition()) in execute().
     *
     * @param kP Proportional gain — how fast it reacts to error (start around 0.005)
     * @param kI Integral gain    — corrects long-term error (start at 0, add only if needed)
     * @param kD Derivative gain  — reduces oscillation (start at 0, add only if needed)
     */
    public static PIDController forSlide(double kP, double kI, double kD) {
        return new PIDController(kP, kI, kD, 0, Mode.POSITION);
    }

    /**
     * Creates a PIDController for a vertical elevator or lift.
     *
     * The feedforward (kF) applies a constant power offset to hold the elevator
     * against gravity, so the PID only corrects small position errors.
     * Without kF, the elevator slowly drifts down even when at the target.
     *
     * Use calculate(motor.getCurrentPosition()) in execute().
     *
     * @param kP Proportional gain — how fast it reacts to error (start around 0.005)
     * @param kI Integral gain    — corrects long-term error (start at 0, add only if needed)
     * @param kD Derivative gain  — reduces oscillation (start at 0, add only if needed)
     * @param kF Feedforward gain — gravity hold (start around 0.05 and tune until it holds still)
     */
    public static PIDController forElevator(double kP, double kI, double kD, double kF) {
        return new PIDController(kP, kI, kD, kF, Mode.POSITION);
    }

    /**
     * Creates a PIDController for a rotating arm.
     *
     * The feedforward (kF) compensates gravity automatically using cos(angle),
     * so the arm holds its position without the motor fighting gravity alone.
     *
     * Use calculate(motor.getCurrentPosition()) in execute().
     *
     * @param kP Proportional gain — how fast it reacts to error (start around 0.005)
     * @param kI Integral gain    — corrects long-term error (start at 0, add only if needed)
     * @param kD Derivative gain  — reduces oscillation (start at 0, add only if needed)
     * @param kF Feedforward gain — gravity compensation (start around 0.1 and tune)
     */
    public static PIDController forArm(double kP, double kI, double kD, double kF) {
        return new PIDController(kP, kI, kD, kF, Mode.ANGLE);
    }

    /**
     * Creates a PIDController for a flywheel, launcher, or any speed-controlled motor.
     *
     * The feedforward (kF) does most of the work — it spins the motor near the
     * target speed so the PID only corrects small deviations.
     *
     * Use runVelocity(motor) in execute() — velocity is measured automatically.
     *
     * @param kP Proportional gain — corrects speed error (start around 0.001)
     * @param kF Feedforward gain  — main speed driver (start around 0.0008 and tune)
     */
    public static PIDController forFlywheel(double kP, double kF) {
        return new PIDController(kP, 0, 0, kF, Mode.VELOCITY);
    }

    /**
     * Creates a PIDController for a flywheel with full PIDF control.
     * Use this when forFlywheel(kP, kF) alone isn't accurate enough.
     *
     * @param kP Proportional gain
     * @param kI Integral gain
     * @param kD Derivative gain
     * @param kF Feedforward gain
     */
    public static PIDController forFlywheelPIDF(double kP, double kI, double kD, double kF) {
        return new PIDController(kP, kI, kD, kF, Mode.VELOCITY);
    }

    // ── Optional features ─────────────────────────────────────────────────────

    /**
     * Enables trapezoidal motion profiling with symmetric accel/decel.
     * Call this in initialize(), before the first setTarget().
     *
     * @param maxVelocity     Peak velocity (ticks/s or degrees/s)
     * @param maxAcceleration Acceleration and deceleration rate (ticks/s²)
     * @return this, for method chaining
     */
    public PIDController enableMotionProfile(double maxVelocity, double maxAcceleration) {
        return enableMotionProfile(maxVelocity, maxAcceleration, maxAcceleration);
    }

    /**
     * Enables trapezoidal motion profiling with asymmetric accel/decel.
     * Useful when the robot needs to decelerate faster than it accelerates.
     *
     * @param maxVelocity     Peak velocity (ticks/s or degrees/s)
     * @param maxAcceleration Acceleration rate (ticks/s²)
     * @param maxDeceleration Deceleration rate (ticks/s²)
     * @return this, for method chaining
     */
    public PIDController enableMotionProfile(double maxVelocity, double maxAcceleration, double maxDeceleration) {
        mpEnabled     = true;
        mpMaxVelocity = Math.abs(maxVelocity);
        mpMaxAccel    = Math.abs(maxAcceleration);
        mpMaxDecel    = Math.abs(maxDeceleration);
        return this;
    }

    /**
     * Enables automatic voltage compensation at 12V nominal.
     * Keeps robot behavior consistent as the battery drains during a match.
     * Call this in initialize().
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
     * @param hardwareMap    HardwareMap from the OpMode
     * @param nominalVoltage Voltage at which your PID constants were tuned (default 12.0V)
     * @return this, for method chaining
     */
    public PIDController enableVoltageCompensation(HardwareMap hardwareMap, double nominalVoltage) {
        if (hardwareMap.voltageSensor.iterator().hasNext()) {
            this.voltageSensor      = hardwareMap.voltageSensor.iterator().next();
            this.nominalVoltage     = nominalVoltage;
            this.voltageCompEnabled = true;
        }
        return this;
    }

    // ── Main API ──────────────────────────────────────────────────────────────

    /**
     * Sets the desired target.
     * If motion profiling is enabled, use setTarget(target, currentPosition) instead
     * so the profile can plan from the actual current position.
     *
     * @param target Desired target (ticks for POSITION, degrees for ANGLE, ticks/s for VELOCITY)
     */
    public void setTarget(double target) {
        this.target = target;
        resetPID();
        if (mpEnabled) planMotionProfile(0, target);
    }

    /**
     * Sets the desired target with the current position for accurate motion profile planning.
     * Always prefer this overload when motion profiling is enabled.
     *
     * @param target          Desired target
     * @param currentPosition Current measured position
     */
    public void setTarget(double target, double currentPosition) {
        this.target = target;
        resetPID();
        if (mpEnabled) planMotionProfile(currentPosition, target);
    }

    /**
     * Calculates and returns the motor power for the current position.
     * Call this every loop iteration in execute().
     *
     * Handles automatically (when enabled):
     *   - Motion profile: follows a smooth trajectory to the target
     *   - Voltage compensation: scales power for battery level
     *
     * @param currentPosition Current measured position (ticks or degrees)
     * @return Motor power, clamped to [-1, 1]
     */
    public double calculate(double currentPosition) {
        double dt = timer.seconds();
        timer.reset();
        if (dt <= 0) dt = 0.02;

        double setpoint = (mpEnabled && mpActive) ? getProfilePosition() : target;

        double error = (mode == Mode.ANGLE)
                ? angleWrap(setpoint - currentPosition)
                : setpoint - currentPosition;

        integralSum = clamp(integralSum + error * dt, -integralMax, integralMax);

        double derivative = (error - lastError) / dt;
        lastError = error;

        double feedforward = computeFeedforward(setpoint);
        double output = (kP * error) + (kI * integralSum) + (kD * derivative) + feedforward;

        if (Math.abs(error) <= tolerance) {
            atTarget = true;
            output   = feedforward;
        } else {
            atTarget = false;
        }

        output = applyVoltageCompensation(clamp(output, -1.0, 1.0));

        lastOutput = output;
        return output;
    }

    /**
     * One-liner: calculates and immediately sets motor power.
     * Use for POSITION and ANGLE modes.
     *
     * @param motor           Motor to drive
     * @param currentPosition Current encoder position (ticks or degrees)
     */
    public void run(DcMotor motor, double currentPosition) {
        motor.setPower(calculate(currentPosition));
    }

    /**
     * [VELOCITY mode] Calculates motor power by measuring speed from encoder ticks.
     * The velocity (ticks/s) is computed internally — no manual calculation needed.
     *
     * @param currentTicks Current encoder position from motor.getCurrentPosition()
     * @return Motor power, clamped to [-1, 1]
     */
    public double calculateVelocity(int currentTicks) {
        double dt = velTimer.seconds();
        velTimer.reset();
        if (dt <= 0) dt = 0.02;

        measuredVelocity = (currentTicks - velLastTicks) / dt;
        velLastTicks = currentTicks;

        return calculate(measuredVelocity);
    }

    /**
     * [VELOCITY mode] One-liner: measures speed and sets motor power immediately.
     *
     * @param motor Motor to drive (encoder must be connected)
     */
    public void runVelocity(DcMotor motor) {
        motor.setPower(calculateVelocity(motor.getCurrentPosition()));
    }

    /**
     * Resets the PID state (integral, derivative memory).
     * setTarget() calls this automatically. Call manually only if you need to
     * reset without changing the target.
     */
    public void reset() {
        resetPID();
        velLastTicks    = 0;
        measuredVelocity = 0;
        velTimer.reset();
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    /** Resets only PID state, not velocity tracker. Called on every setTarget(). */
    private void resetPID() {
        integralSum = 0;
        lastError   = 0;
        lastOutput  = 0;
        atTarget    = false;
        timer.reset();
    }

    private double computeFeedforward(double setpoint) {
        switch (mode) {
            case ANGLE:
                // cos(angle) compensates the gravity torque on a rotating arm.
                // At 0° (horizontal) the arm is heaviest; at 90° (vertical) it needs no help.
                return kF * Math.cos(Math.toRadians(setpoint));
            case VELOCITY:
                // Feedforward proportional to target speed — does most of the work
                // so the PID only corrects the small remaining error.
                return kF * target;
            default: // POSITION
                // Constant offset to overcome static friction.
                return kF;
        }
    }

    private double applyVoltageCompensation(double power) {
        if (!voltageCompEnabled || voltageSensor == null) return power;
        double voltage = voltageSensor.getVoltage();
        if (voltage <= 0) return power;
        return clamp(power * (nominalVoltage / voltage), -1.0, 1.0);
    }

    private void planMotionProfile(double from, double to) {
        mpStartPos    = from;
        mpEndPos      = to;
        mpTotalDist   = Math.abs(to - from);
        mpDirection   = (to >= from) ? 1.0 : -1.0;

        if (mpTotalDist < 1e-6) {
            mpActive = false;
            return;
        }

        double tAccel = mpMaxVelocity / mpMaxAccel;
        double tDecel = mpMaxVelocity / mpMaxDecel;
        double dAccel = 0.5 * mpMaxAccel * tAccel * tAccel;
        double dDecel = 0.5 * mpMaxDecel * tDecel * tDecel;

        if (dAccel + dDecel > mpTotalDist) {
            // Triangle profile: distance too short to reach mpMaxVelocity
            double rate    = (1.0 / mpMaxAccel) + (1.0 / mpMaxDecel);
            mpPeakVelocity = Math.sqrt(2.0 * mpTotalDist / rate);
            mpAccelTime    = mpPeakVelocity / mpMaxAccel;
            mpCruiseTime   = 0;
        } else {
            // Full trapezoidal profile
            mpPeakVelocity = mpMaxVelocity;
            mpAccelTime    = tAccel;
            mpCruiseTime   = (mpTotalDist - dAccel - dDecel) / mpMaxVelocity;
        }

        mpTotalTime = mpAccelTime + mpCruiseTime + (mpPeakVelocity / mpMaxDecel);
        mpTimer.reset();
        mpActive = true;
    }

    private double getProfilePosition() {
        double t          = mpTimer.seconds();
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
            displacement  = dAccel + mpPeakVelocity * (t - mpAccelTime);

        } else {
            double dAccel  = 0.5 * mpMaxAccel * mpAccelTime * mpAccelTime;
            double dCruise = mpPeakVelocity * mpCruiseTime;
            double tD      = t - decelStart;
            displacement   = dAccel + dCruise + mpPeakVelocity * tD - 0.5 * mpMaxDecel * tD * tD;
        }

        return mpStartPos + mpDirection * Math.min(displacement, mpTotalDist);
    }

    private double angleWrap(double degrees) {
        while (degrees >  180) degrees -= 360;
        while (degrees < -180) degrees += 360;
        return degrees;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setKP(double kP) { this.kP = kP; }
    public void setKI(double kI) { this.kI = kI; }
    public void setKD(double kD) { this.kD = kD; }
    public void setKF(double kF) { this.kF = kF; }

    /** Position/angle tolerance in ticks or degrees. Controller holds at target within this range. */
    public void setTolerance(double tolerance) { this.tolerance = Math.abs(tolerance); }

    /**
     * Prevents the integral term from growing unbounded (integral windup).
     * A good starting value is 1.0 / kI. Default is unlimited.
     */
    public void setIntegralMax(double integralMax) { this.integralMax = Math.abs(integralMax); }

    // ── Getters ───────────────────────────────────────────────────────────────

    public double  getKP()               { return kP; }
    public double  getKI()               { return kI; }
    public double  getKD()               { return kD; }
    public double  getKF()               { return kF; }
    public double  getTarget()           { return target; }
    public double  getLastOutput()       { return lastOutput; }
    public double  getMeasuredVelocity() { return measuredVelocity; }
    public boolean atTarget()            { return atTarget; }
    public boolean isProfileDone()       { return !mpActive; }
    public Mode    getMode()             { return mode; }
}