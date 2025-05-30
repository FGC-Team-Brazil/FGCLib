package org.firstinspires.ftc.teamcode.core.lib.pid;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * PIDController is a standard class for building PIDF control systems.
 * It uses the constants: Proportional, Integral and Derivative.
 * In addition to PID, it implements a FeedForward constant.
 * The class works in two modes: Position and Angle.
 */
public class PIDController {
    public enum Mode {
        POSITION,
        ANGLE,
        VELOCITY
    }

    private double kP;
    private double kI;
    private double kD;
    private double kF;

    private double integralSum = 0;

    private double setPoint;
    private boolean atSetPoint;
    private double positionTolerance = 0;

    private double output = 0;
    private Mode actualMode;

    private ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;

    private int revolutionEncoder = 0;

    /**
     *
     * @param kP double
     * @param kI double
     * @param kD double
     * @param kF double
     * @param mode ANGLE if the PID is used for an arm;
     *            POSITION if the PID is used for linear slides/robot movement
     *             VELOCITY if the PID is used for a launcher/shooter
     * @param ticksPerEncoderRevolution int number of ticks in a revolution
     */
    public PIDController(double kP, double kI, double kD, double kF, Mode mode,int ticksPerEncoderRevolution) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.actualMode = mode;
        this.revolutionEncoder = ticksPerEncoderRevolution;
    }

    /**
     * Calculates the controller output given a setpoint and a reference
     * <p>
     *     If the PID mode is ANGLE uses the angleWrap method to make controlling it easier
     * </p>
     * @param setPoint
     * @param realPosition
     * @return
     */
    public double calculate(double setPoint, double realPosition) {
        this.setPoint = setPoint;
        double error = 0;

        if (actualMode == Mode.ANGLE) {
            error = angleWrap(setPoint - realPosition);
        } else {
            error = setPoint - realPosition;
        }

        integralSum += error * timer.seconds();
        double derivative = (error - lastError) / (timer.seconds());
        lastError = error;
        timer.reset();
        double output = (error * kP) + (derivative * kD) + (integralSum * kI) + kF;

        if (Math.abs(error) < positionTolerance) {
            atSetPoint = true;
            output = 0;
        } else {
            this.output = output;
        }
        return output;
    }

    public void setPowerMotor(DcMotor dcMotor) {
        int conversionValue = this.revolutionEncoder / 360;
        dcMotor.setPower(output * conversionValue);
    }

    private double angleWrap(double radians) {
        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }
        return radians;
    }

    // Set Methods
    public void setKP(double newKP) {
        this.kP = newKP;
    }

    public void setKI(double newKI) {
        this.kI = newKI;
    }

    public void setKD(double newKD) {
        this.kD = newKD;
    }

    public void setKF(double newKF) {
        this.kF = newKF;
    }

    public void setSetPoint(double setPoint) {
        this.setPoint = setPoint;
    }

    public void setTolerance(double positionTolerance) {
        this.positionTolerance = Math.abs(positionTolerance);
    }

    // Get Methods
    public double getKP(double newKP) {
        return this.kP;
    }

    public double getKI(double newKI) {
        return this.kI;
    }

    public double getKD(double newKD) {
        return this.kD;
    }

    public double getKF(double newKF) {
        return this.kF;
    }

    public boolean atSetPoint() {
        return atSetPoint;
    }
}