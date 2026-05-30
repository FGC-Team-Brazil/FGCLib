package org.firstinspires.ftc.teamcode.core.lib.vision;

import android.util.Size;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.teamcode.core.lib.vision.camera.CameraConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

/**
 * VisionSystem is the main vision subsystem of FGCLib.
 *
 * <p>Follows the same singleton pattern as DrivetrainBuilder. Use {@link #build(String)} to
 * configure and register it in RobotSubsystems, and {@link #getInstance()} to access detections
 * from OpModes.
 *
 * <p>Basic usage in RobotSubsystems:
 *
 * <pre>
 *     VisionSystem.build(CameraConstants.WEBCAM_NAME)
 *             .withAprilTags()
 * </pre>
 *
 * <p>Usage with camera offset (recommended for accurate pose):
 *
 * <pre>
 *     VisionSystem.build(CameraConstants.WEBCAM_NAME)
 *             .withCameraTransform(new AprilTagDetection.Transform3D(0.15, 0.10, 0.20))
 *             .withAprilTags()
 * </pre>
 *
 * <p>Accessing detections in OpModes:
 *
 * <pre>
 *     VisionSystem.getInstance().getAprilTagDetections()
 * </pre>
 *
 * @author FGC Team Brazil
 */
public class VisionSystem implements Subsystem {

  private static VisionSystem instance;

  private String webcamName;
  private CameraConfig config;
  private AprilTagLibrary tagLibrary;
  private AprilTagDetection.Transform3D cameraTransform;
  private List<VisionProcessor> extraProcessors;

  private VisionPortal portal;
  private AprilTagProcessor aprilTagProcessor;
  private Telemetry telemetry;
  private boolean aprilTagEnabled;

  private VisionSystem() {}

  /**
   * Configures and returns the VisionSystem instance.
   *
   * <p>Chain optional methods before adding to RobotSubsystems:
   *
   * <pre>
   *     VisionSystem.build(CameraConstants.WEBCAM_NAME)
   *             .withCameraTransform(new AprilTagDetection.Transform3D(0.15, 0.10, 0.20))
   *             .withAprilTags()
   * </pre>
   *
   * @param webcamName webcam name in the HardwareMap.
   * @return configured instance.
   */
  public static VisionSystem build(String webcamName) {
    getInstance();
    instance.webcamName = webcamName;
    instance.config = CameraConfig.defaultConfig();
    instance.tagLibrary = null;
    instance.cameraTransform = new AprilTagDetection.Transform3D(0, 0, 0);
    instance.extraProcessors = new ArrayList<>();
    instance.aprilTagEnabled = true;
    return instance;
  }

  /**
   * Sets the camera position relative to the robot center.
   *
   * <p>Use this when the camera is not mounted at the robot center. The offset is applied to all
   * detection coordinates so that positions are relative to the robot center instead of the camera.
   *
   * <p>Example — camera mounted 15cm to the right, 10cm forward and 20cm above the robot center:
   *
   * <pre>
   *     .withCameraTransform(new AprilTagDetection.Transform3D(0.15, 0.10, 0.20))
   * </pre>
   *
   * @param transform 3D offset of the camera relative to robot center.
   * @return this instance for chaining.
   */
  public VisionSystem withCameraTransform(AprilTagDetection.Transform3D transform) {
    this.cameraTransform = transform;
    return this;
  }

  /**
   * Enables AprilTag detection with a custom tag library. Improves pose accuracy by providing
   * physical tag sizes.
   *
   * @param library custom AprilTag library.
   * @return this instance for chaining.
   */
  public VisionSystem withAprilTags(AprilTagLibrary library) {
    this.tagLibrary = library;
    return this;
  }

  /**
   * Sets a custom camera configuration. If not called, uses {@link CameraConfig#defaultConfig()}.
   *
   * @param config camera configuration.
   * @return this instance for chaining.
   */
  public VisionSystem withConfig(CameraConfig config) {
    this.config = config;
    return this;
  }

  /**
   * Adds an extra OpenCV vision processor.
   *
   * @param processor processor to register in the VisionPortal.
   * @return this instance for chaining.
   */
  public VisionSystem withProcessor(VisionProcessor processor) {
    this.extraProcessors.add(processor);
    return this;
  }

  @Override
  public void initialize(HardwareMap hardwareMap) {
    AprilTagProcessor.Builder aprilTagBuilder =
        new AprilTagProcessor.Builder()
            .setDrawAxes(false)
            .setDrawCubeProjection(true)
            .setDrawTagOutline(false);

    if (tagLibrary != null) {
      aprilTagBuilder.setTagLibrary(tagLibrary);
    }

    aprilTagProcessor = aprilTagBuilder.build();
    aprilTagProcessor.setDecimation(config.decimation);

    VisionPortal.Builder portalBuilder =
        new VisionPortal.Builder()
            .setCamera(hardwareMap.get(WebcamName.class, webcamName))
            .setCameraResolution(new Size(config.resolutionWidth, config.resolutionHeight))
            .setStreamFormat(VisionPortal.StreamFormat.YUY2)
            .enableLiveView(config.streamEnabled)
            .setAutoStopLiveView(config.autoStopLiveView)
            .addProcessor(aprilTagProcessor);

    for (VisionProcessor extra : extraProcessors) {
      portalBuilder.addProcessor(extra);
    }

    portal = portalBuilder.build();

    while (portal.getCameraState() != VisionPortal.CameraState.STREAMING) {
      Thread.yield();
    }
  }

  @Override
  public void start() {}

  @Override
  public void stop() {
    if (portal != null) {
      portal.close();
    }
  }

  @Override
  public void execute() {
    List<AprilTagDetection> detections = getAprilTagDetections();
    if (detections.isEmpty()) {
      telemetry.addLine("[Vision] No AprilTag detected");
    } else {
      for (AprilTagDetection d : detections) {
        telemetry.addData(
            "[Vision] " + d.getDisplayName(),
            "x=%.2f y=%.2f z=%.2f yaw=%.1f°",
            d.getX(),
            d.getY(),
            d.getZ(),
            d.getYaw());
      }
    }
  }

  /**
   * Returns all AprilTags detected in the current frame. Never returns null — returns empty list if
   * no detections.
   *
   * @return immutable list of detections.
   */
  public List<AprilTagDetection> getAprilTagDetections() {
    if (aprilTagProcessor == null) return Collections.emptyList();

    List<org.firstinspires.ftc.vision.apriltag.AprilTagDetection> raw =
        aprilTagProcessor.getDetections();

    if (raw == null || raw.isEmpty()) return Collections.emptyList();

    List<AprilTagDetection> result = new ArrayList<>(raw.size());
    for (org.firstinspires.ftc.vision.apriltag.AprilTagDetection sdk : raw) {
      result.add(convertDetection(sdk));
    }
    return Collections.unmodifiableList(result);
  }

  /**
   * Returns the first detected AprilTag, or null if none.
   *
   * @return first detection or null.
   */
  public AprilTagDetection getFirstAprilTag() {
    List<AprilTagDetection> list = getAprilTagDetections();
    return list.isEmpty() ? null : list.get(0);
  }

  /**
   * Searches for an AprilTag by ID.
   *
   * @param id tag ID to search for.
   * @return detection with the given ID, or null if not found.
   */
  public AprilTagDetection getAprilTagById(int id) {
    for (AprilTagDetection d : getAprilTagDetections()) {
      if (d.getID() == id) return d;
    }
    return null;
  }

  /**
   * Returns true if at least one AprilTag is currently detected.
   *
   * @return true if detections are available.
   */
  public boolean hasAprilTagDetections() {
    return !getAprilTagDetections().isEmpty();
  }

  public void enableAprilTags() {
    if (portal != null && aprilTagProcessor != null) {
      portal.setProcessorEnabled(aprilTagProcessor, true);
      aprilTagEnabled = true;
    }
  }

  public void disableAprilTags() {
    if (portal != null && aprilTagProcessor != null) {
      portal.setProcessorEnabled(aprilTagProcessor, false);
      aprilTagEnabled = false;
    }
  }

  public boolean isAprilTagEnabled() {
    return aprilTagEnabled;
  }

  /**
   * Enables or disables an extra OpenCV processor by index, in the order it was added via {@link
   * #withProcessor}.
   *
   * @param processorIndex index of the processor.
   * @param enabled true to enable, false to disable.
   */
  public void setProcessorEnabled(int processorIndex, boolean enabled) {
    if (portal != null && processorIndex < extraProcessors.size()) {
      portal.setProcessorEnabled(extraProcessors.get(processorIndex), enabled);
    }
  }

  /**
   * Returns a direct reference to an extra OpenCV processor by index.
   *
   * @param index processor index.
   * @return the VisionProcessor at the given index.
   */
  public VisionProcessor getProcessor(int index) {
    return extraProcessors.get(index);
  }

  /**
   * Returns the singleton instance of VisionSystem.
   *
   * <p>Use this in OpModes to access detections:
   *
   * <pre>
   *     VisionSystem.getInstance().getAprilTagDetections()
   * </pre>
   *
   * @return singleton instance.
   */
  public static VisionSystem getInstance() {
    if (instance == null) {
      synchronized (VisionSystem.class) {
        if (instance == null) {
          instance = new VisionSystem();
        }
      }
    }
    return instance;
  }

  private AprilTagDetection convertDetection(
      org.firstinspires.ftc.vision.apriltag.AprilTagDetection sdk) {

    String name = sdk.metadata != null ? sdk.metadata.name : "unknown";

    double x = 0, y = 0, z = 0, roll = 0, pitch = 0, yaw = 0;
    if (sdk.ftcPose != null) {
      x = sdk.ftcPose.x; // lateral
      y = sdk.ftcPose.y; // forward
      z = sdk.ftcPose.z; // vertical (height)
      roll = sdk.ftcPose.roll;
      pitch = sdk.ftcPose.pitch;
      yaw = sdk.ftcPose.yaw;
    }

    double cx = sdk.center != null ? sdk.center.x : 0;
    double cy = sdk.center != null ? sdk.center.y : 0;

    return new AprilTagDetection(sdk.id, name, x, y, z, roll, pitch, yaw, cx, cy, cameraTransform);
  }
}
