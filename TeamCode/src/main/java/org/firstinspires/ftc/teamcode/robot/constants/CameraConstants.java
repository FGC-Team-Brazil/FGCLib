package org.firstinspires.ftc.teamcode.robot.constants;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.MotionDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;


public class CameraConstants {
    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    public static double fx = 578.272;
    public static double fy = 578.272;
    public static double cx = 402.145;
    public static double cy = 221.506;

    // UNITS ARE METERS
    public static double TAG_SIZE = 0.166;
    public static final String WebcamName = "webcam1";
    public static final Position CameraPosition = new Position(DistanceUnit.CM, 0, 0, 0, 0);
    public static final YawPitchRollAngles CameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES, 0, -90, 0, 0);

    //example of how to make an april tag lib since they wont release the darn robot manual with the april tags >:(
    public static AprilTagLibrary getFeedingTheFutureTagLibrary() {
        return (new AprilTagLibrary.Builder())
                .addTag(100, "Blue Nexus Goal - Field Center - Facing Platform", 160.0D, new VectorF(0,0,0), DistanceUnit.MM,new Quaternion(0,0,0,0,0))
                .addTag(101, "Red Nexus Goal - Field Center - Facing Platform", 160.0D, DistanceUnit.MM)
                .addTag(102, "Red Nexus Goal - Field Center - Facing Food Warehouse", 160.0D, DistanceUnit.MM)
                .addTag(103, "Blue Nexus Goal - Field Center - Facing Food Warehouse", 160.0D, DistanceUnit.MM)
                .addTag(104, "Blue Nexus Goal - Field Edge - Alliance Station", 160.0D, DistanceUnit.MM)
                .addTag(105, "Blue Nexus Goal - Field Edge - Center Field", 160.0D, DistanceUnit.MM)
                .build();
    }
}