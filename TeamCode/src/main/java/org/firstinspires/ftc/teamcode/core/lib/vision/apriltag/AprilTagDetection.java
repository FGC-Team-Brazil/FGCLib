package org.firstinspires.ftc.teamcode.core.lib.vision.apriltag;

import java.util.Locale;

public class AprilTagDetection {

    public final int    id;
    public final String name;

    /** Posição em metros. */
    public final double x, y, z;

    /** Rotação em graus. */
    public final double roll, pitch, yaw;

    /** Centro da tag no frame (pixels). */
    public final double centerX, centerY;

    public AprilTagDetection(int id, String name,
                             double x, double y, double z,
                             double roll, double pitch, double yaw,
                             double centerX, double centerY) {
        this.id      = id;
        this.name    = name;
        this.x       = x;
        this.y       = y;
        this.z       = z;
        this.roll    = roll;
        this.pitch   = pitch;
        this.yaw     = yaw;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public String getDisplayName() {
        return name != null ? name : "Tag #" + id;
    }

    public double getHorizontalDistance() {
        return Math.sqrt(x * x + z * z);
    }

    @Override
    public String toString() {
        return String.format(
                Locale.US,
                "AprilTagDetection{id=%d, name='%s', x=%.3f, y=%.3f, z=%.3f, yaw=%.1f}",
                id, getDisplayName(), x, y, z, yaw
        );
    }
}