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
    public static AprilTagLibrary getEcoEquilibriumTagLibrary() {
        return (new AprilTagLibrary.Builder())
                .addTag(100, "Red Side - Facing Audience", 160.0D, new VectorF(0,0,0), DistanceUnit.MM,new Quaternion(0,0,0,0,0))
                .addTag(101, "Red Side - Facing Dispenser", 160.0D, DistanceUnit.MM)
                .addTag(102, "Center - Facing Audience", 160.0D, DistanceUnit.MM)
                .addTag(103, "Center - Facing Dispenser", 160.0D, DistanceUnit.MM)
                .addTag(104, "Blue Side - Facing Audience", 160.0D, DistanceUnit.MM)
                .addTag(105, "Blue Side - Facing Dispenser", 160.0D, DistanceUnit.MM)
                .build();
    }
}