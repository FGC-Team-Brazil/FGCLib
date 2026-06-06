package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.vision.VisionSystem;
import org.firstinspires.ftc.teamcode.core.lib.vision.apriltag.AprilTagDetection;

public class VisionSubsystem implements Subsystem {

    private static VisionSubsystem instance;

    private VisionSubsystem() {}

    @Override
    public void initialize(HardwareMap hardwareMap) {

        VisionSystem.build("Nome da webCam");

        VisionSystem.getInstance().initialize(hardwareMap);
    }

    @Override
    public void start() {}

    @Override
    public void execute() {

        if (!VisionSystem.getInstance().hasAprilTagDetections()) {
            return;
        }

        AprilTagDetection tag =
                VisionSystem.getInstance().getFirstAprilTag();

        System.out.println("April Tag");
        System.out.println("ID: " + tag.getID());
        System.out.println("Name: " + tag.getDisplayName());
        System.out.println("X: " + tag.getX());
        System.out.println("Y: " + tag.getY());
        System.out.println("Z: " + tag.getZ());
        System.out.println("Yaw: " + tag.getYaw());
        System.out.println("Pitch: " + tag.getPitch());
        System.out.println("Roll: " + tag.getRoll());
    }

    @Override
    public void stop() {
        VisionSystem.getInstance().stop();
    }

    public boolean hasTarget() {
        return VisionSystem.getInstance().hasAprilTagDetections();
    }

    public AprilTagDetection getTarget() {
        return VisionSystem.getInstance().getFirstAprilTag();
    }

    public void enableVision() {
        VisionSystem.getInstance().enableAprilTags();
    }

    public void disableVision() {
        VisionSystem.getInstance().disableAprilTags();
    }

    public AprilTagDetection getTagById(int id) {
        return VisionSystem.getInstance().getAprilTagById(id);
    }

    public static VisionSubsystem getInstance() {

        if (instance == null) {
            instance = new VisionSubsystem();
        }

        return instance;
    }
}