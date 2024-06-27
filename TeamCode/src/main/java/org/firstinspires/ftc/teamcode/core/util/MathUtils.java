package org.firstinspires.ftc.teamcode.core.util;

public class MathUtils {
    private MathUtils() {}

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double degreesToRadians(double angle){
        return angle * (Math.PI / 180);
    };

    public static double radiansToDegrees(double angle){
        return angle * (180 / Math.PI);
    };

    public static double wrapAngle(double angle, double range){
        double wrapRange = Math.abs(range);
        double wrapedAngle = angle;
        double multiplier = angle / wrapRange;
        if (angle > wrapRange) {
            wrapedAngle = angle - (multiplier * wrapRange) - wrapRange;
        } else if (angle < -wrapRange) {
            wrapedAngle = angle + (multiplier * wrapRange) + wrapRange;
        }
        return wrapedAngle;
    }

    public static double ticksToMeters(double ticks, double ticksPerRevolution, double circumference){
        return ((ticks / ticksPerRevolution) * circumference);
    }

    public static double metersToTicks(double meters, double ticksPerRevolution, double circumference){
        return ((meters / circumference) * ticksPerRevolution);
    }
}