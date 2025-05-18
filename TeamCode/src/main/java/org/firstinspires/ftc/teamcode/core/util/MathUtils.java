package org.firstinspires.ftc.teamcode.core.util;

/**
 * A class that contains many math aplications used in FTC and FGC.
 * <p>
 *     This class can be used to simplify implementations of algorithiims such as odometry and IMU driver based movement.
 * </p>
 */
public class MathUtils {
    private MathUtils() {}

    /**
     * Makes sure a value cannot go past certain bounds
     * <p>
     *     Useful in PIDs and when it is desired to limit power applied to motors
     * </p>
     * <p>
     *     Example: double motorPower = clamp(speedDesired,-0.7,0.7);
     * </p>
     * @param value (double) the desired value
     * @param min (double) the lowest output allowed
     * @param max (double) the maximum output allowed
     * @return value within the range min<=value<=max
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Converts degrees to radians
     * @param angle (double) angle in degrees
     * @return angle in radians
     */
    public static double degreesToRadians(double angle){
        return angle * (Math.PI / 180);
    };

    /**
     * Converts radians to degrees
     * @param angle (double) angle in radians
     * @return angle in degrees
     */
    public static double radiansToDegrees(double angle){
        return angle * (180 / Math.PI);
    };

    /**
     * makes sure and angle cannot go past a certain range
     * <p>Can be used to make sure an angle never goes past the range [-180,180],which is useful for orientation PIDs</p>
     * @param angle (double) the angle to be put in the range
     * @param range (double) used to define the range [-range,range]
     * @return
     */
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
    /**
    Converts the amount of ticks into meters
     <p>
        Useful for odometry or linear slides
     </p>
     @param ticks amount of encoder ticks
     @param circumference circunference of axis/wheel connected to encoder
     @param ticksPerRevolution encoder ticks for each revolution
     @return meters
     */
    public static double ticksToMeters(double ticks, double ticksPerRevolution, double circumference){
        return ((ticks / ticksPerRevolution) * circumference);
    }
    /**
     Converts the amount of meters into ticks
     <p>
     Useful for odometry or linear slides
     </p>
     @param meters amount of meters
     @param circumference circumference of axis/wheel connected to encoder
     @param ticksPerRevolution encoder ticks for each revolution
     @return encoder ticks
     */
    public static double metersToTicks(double meters, double ticksPerRevolution, double circumference){
        return ((meters / circumference) * ticksPerRevolution);
    }
}