package org.firstinspires.ftc.teamcode.core.lib.vision;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.teamcode.core.lib.vision.camera.CameraConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VisionSystem implements Subsystem {

    private static final long CAMERA_TIMEOUT_MS = 5000;

    private String webcamName;
    private CameraName camera;
    private CameraConfig config;
    private AprilTagLibrary tagLibrary;

    private final List<VisionProcessor> extraProcessors =
            new ArrayList<>();

    private boolean useAprilTags = false;

    private VisionPortal portal;
    private AprilTagProcessor aprilTagProcessor;

    private VisionSystem() {
    }

    public static VisionSystem build(String webcamName) {

        VisionSystem vision = new VisionSystem();

        vision.webcamName =
                Objects.requireNonNull(
                        webcamName,
                        "webcamName não pode ser null"
                );

        return vision;
    }

    public VisionSystem withAprilTags() {
        this.useAprilTags = true;
        return this;
    }

    public VisionSystem withConfig(CameraConfig config)
    {

        this.config =
                Objects.requireNonNull(
                        config,
                        "config não pode ser null"
                );

        return this;
    }

    public VisionSystem withProcessor(VisionProcessor processor) {
        this.extraProcessors.add(Objects.requireNonNull(processor, "processor não pode ser null"));
        return this;
    }

    @Override
    public void initialize(
            HardwareMap hardwareMap,
            Telemetry telemetry
    ) {

        if (config == null) {
            config = CameraConfig.defaultConfig();
        }

        if (camera == null) {

            if (webcamName == null ||
                    webcamName.isBlank()) {

                throw new IllegalStateException(
                        "Nenhuma câmera foi definida."
                );
            }

            camera = hardwareMap.get(
                    WebcamName.class,
                    webcamName
            );
        }

        VisionPortal.Builder portalBuilder =
                new VisionPortal.Builder()
                        .setCamera(camera)
                        .setCameraResolution(
                                new Size(
                                        config.resolutionWidth,
                                        config.resolutionHeight
                                )
                        )
                        .setStreamFormat(
                                VisionPortal.StreamFormat.YUY2
                        )
                        .enableLiveView(
                                config.streamEnabled
                        )
                        .setAutoStopLiveView(
                                config.autoStopLiveView
                        );

        if (useAprilTags) {

            AprilTagProcessor.Builder aprilBuilder =
                    new AprilTagProcessor.Builder()
                            .setDrawAxes(false)
                            .setDrawCubeProjection(true)
                            .setDrawTagOutline(false);

            if (tagLibrary != null) {
                aprilBuilder.setTagLibrary(tagLibrary);
            }

            aprilTagProcessor = aprilBuilder.build();

            aprilTagProcessor.setDecimation(
                    config.decimation
            );

            portalBuilder.addProcessor(
                    aprilTagProcessor
            );
        }

        for (VisionProcessor processor : extraProcessors) {
            portalBuilder.addProcessor(processor);
        }

        portal = portalBuilder.build();

        waitForStreaming();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {

        if (portal != null) {

            portal.close();
            portal = null;
        }
    }

    @Override
    public void execute(
            GamepadManager gamepadManager
    ) {
    }

    public List<AprilTagDetection>
    getAprilTagDetections() {

        if (aprilTagProcessor == null) {
            return Collections.emptyList();
        }

        List<org.firstinspires.ftc.vision.apriltag.AprilTagDetection>
                raw = aprilTagProcessor.getDetections();

        if (raw == null || raw.isEmpty()) {
            return Collections.emptyList();
        }

        List<AprilTagDetection> result =
                new ArrayList<>(raw.size());

        for (
                org.firstinspires.ftc.vision.apriltag.AprilTagDetection sdk
                : raw
        ) {

            result.add(
                    convertDetection(sdk)
            );
        }

        return Collections.unmodifiableList(result);
    }

    public AprilTagDetection getFirstAprilTag() {

        return getAprilTagDetections()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public AprilTagDetection getAprilTagById(
            int id
    ) {

        for (
                AprilTagDetection d
                : getAprilTagDetections()
        ) {

            if (d.id == id) {
                return d;
            }
        }

        return null;
    }

    public boolean hasAprilTagDetections() {
        return !getAprilTagDetections().isEmpty();
    }

    public VisionProcessor getProcessor(
            int index
    ) {

        return extraProcessors.get(index);
    }

    public void setProcessorEnabled(
            int index,
            boolean enabled
    ) {

        if (portal == null) return;

        if (
                index < 0 ||
                        index >= extraProcessors.size()
        ) {
            return;
        }

        portal.setProcessorEnabled(
                extraProcessors.get(index),
                enabled
        );
    }

    private void waitForStreaming() {

        long start =
                System.currentTimeMillis();

        while (
                portal.getCameraState()
                        != VisionPortal.CameraState.STREAMING
        ) {

            if (
                    System.currentTimeMillis() - start
                            > CAMERA_TIMEOUT_MS
            ) {

                throw new IllegalStateException(
                        "Timeout ao iniciar VisionPortal."
                );
            }

            Thread.yield();
        }
    }

    private AprilTagDetection convertDetection(
            org.firstinspires.ftc.vision.apriltag.AprilTagDetection sdk
    ) {

        String name =
                sdk.metadata != null
                        ? sdk.metadata.name
                        : "unknown";

        double x = 0;
        double y = 0;
        double z = 0;

        double roll = 0;
        double pitch = 0;
        double yaw = 0;

        if (sdk.ftcPose != null) {

            x = sdk.ftcPose.x;
            y = sdk.ftcPose.y;
            z = sdk.ftcPose.z;

            roll = sdk.ftcPose.roll;
            pitch = sdk.ftcPose.pitch;
            yaw = sdk.ftcPose.yaw;
        }

        double centerX =
                sdk.center != null
                        ? sdk.center.x
                        : 0;

        double centerY =
                sdk.center != null
                        ? sdk.center.y
                        : 0;

        return new AprilTagDetection(
                sdk.id,
                name,
                x,
                y,
                z,
                roll,
                pitch,
                yaw,
                centerX,
                centerY
        );
    }
}