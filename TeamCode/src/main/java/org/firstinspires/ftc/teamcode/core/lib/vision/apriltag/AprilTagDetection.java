package org.firstinspires.ftc.teamcode.core.lib.vision.apriltag;

import java.util.Locale;

/**
 * Represents an AprilTag detection returned by the vision system.
 *
 * <p>
 * Position values follow the FTC AprilTag coordinate system
 * relative to the robot center.
 * </p>
 *
 * <ul>
 *     <li><b>X</b> → Horizontal/lateral offset relative to the robot.</li>
 *     <li><b>Y</b> → Forward distance from the robot to the tag.</li>
 *     <li><b>Z</b> → Vertical offset (height) relative to the robot.</li>
 * </ul>
 *
 * <p>
 * Rotation values are measured in degrees.
 * </p>
 */
public class AprilTagDetection {

    /**
     * Represents a 3D transform between
     * the robot center and the camera.
     */
    public static class Transform3D {

        /** Camera lateral offset relative to robot center. */
        public final double x;

        /** Camera forward offset relative to robot center. */
        public final double y;

        /** Camera vertical offset relative to robot center. */
        public final double z;

        /**
         * Creates a new 3D transform.
         *
         * @param x lateral offset in meters.
         * @param y forward offset in meters.
         * @param z vertical offset in meters.
         */
        public Transform3D(
                double x,
                double y,
                double z
        ) {

            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    /** AprilTag numeric ID. */
    private final int id;

    /** AprilTag metadata name. */
    private final String name;

    /** Position in meters relative to robot center. */
    private final double x, y, z;

    /** Rotation in degrees. */
    private final double roll, pitch, yaw;

    /** Center position of the tag inside the camera frame (pixels). */
    private final double centerX, centerY;

    /**
     * Creates a new AprilTag detection.
     *
     * @param id AprilTag numeric ID.
     * @param name AprilTag metadata name.
     * @param x tag X position relative to camera.
     * @param y tag Y position relative to camera.
     * @param z tag Z position relative to camera.
     * @param roll roll rotation in degrees.
     * @param pitch pitch rotation in degrees.
     * @param yaw yaw rotation in degrees.
     * @param centerX horizontal center in pixels.
     * @param centerY vertical center in pixels.
     * @param cameraTransform camera transform relative to robot center.
     */
    public AprilTagDetection(
            int id,
            String name,
            double x,
            double y,
            double z,
            double roll,
            double pitch,
            double yaw,
            double centerX,
            double centerY,
            Transform3D cameraTransform
    ) {

        this.id   = id;
        this.name = name;

        this.x = x + cameraTransform.x;
        this.y = y + cameraTransform.y;
        this.z = z+ cameraTransform.z;

        this.roll  = roll;
        this.pitch = pitch;
        this.yaw   = yaw;

        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Returns the best display name for this tag.
     *
     * @return tag metadata name if available,
     * otherwise "Tag #ID".
     */
    public String getDisplayName() {
        return name != null ? name : "Tag #" + id;
    }

    /**
     * Returns the euclidean distance between
     * the robot center and the AprilTag center.
     *
     * @return 3D distance in meters.
     */
    public double getDistance() {
        return Math.sqrt(
                x * x +
                        y * y +
                        z * z
        );
    }

    /**
     * Returns the horizontal/lateral offset.
     *
     * @return X position in meters.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the forward distance from the robot.
     *
     * @return Y position in meters.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the vertical offset (height).
     *
     * @return Z position in meters.
     */
    public double getZ() {
        return z;
    }

    /**
     * Returns the roll rotation.
     *
     * @return roll in degrees.
     */
    public double getRoll() {
        return roll;
    }

    /**
     * Returns the pitch rotation.
     *
     * @return pitch in degrees.
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * Returns the yaw rotation.
     *
     * @return yaw in degrees.
     */
    public double getYaw() {
        return yaw;
    }

    /**
     * Returns the X center of the tag
     * inside the camera frame.
     *
     * @return horizontal pixel coordinate.
     */
    public double getCenterX() {
        return centerX;
    }

    /**
     * Returns the Y center of the tag
     * inside the camera frame.
     *
     * @return vertical pixel coordinate.
     */
    public double getCenterY() {
        return centerY;
    }

    /**
     * Returns the AprilTag metadata name.
     *
     * @return tag name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the AprilTag ID.
     *
     * @return numeric tag ID.
     */
    public int getID() {
        return id;
    }

    /**
     * Returns a formatted string representation
     * of this AprilTag detection.
     *
     * <p>
     * Useful for debugging and telemetry output.
     * </p>
     *
     * @return formatted detection information.
     */
    @Override
    public String toString() {

        return String.format(
                Locale.US,
                "AprilTagDetection{id=%d, name='%s', x=%.3f, y=%.3f, z=%.3f, yaw=%.1f}",
                id,
                getDisplayName(),
                x,
                y,
                z,
                yaw
        );
    }
}