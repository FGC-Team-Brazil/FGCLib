package org.firstinspires.ftc.teamcode.core.lib.vision.camera;

/**
 * Stores configuration values used by the vision camera system.
 *
 * <p>
 * This class defines:
 * </p>
 *
 * <ul>
 *     <li>Camera resolution</li>
 *     <li>Live stream behavior</li>
 *     <li>AprilTag decimation value</li>
 * </ul>
 *
 * <p>
 * Use {@link #defaultConfig()} for the recommended default settings,
 * or {@link #build(int, int, float, boolean, boolean)}
 * to create a custom configuration.
 * </p>
 */
public class CameraConfig {

    /** Camera stream width in pixels. */
    public final int resolutionWidth;

    /** Camera stream height in pixels. */
    public final int resolutionHeight;

    /** Enables the camera live stream preview. */
    public final boolean streamEnabled;

    /** Automatically stops the live view when not needed. */
    public final boolean autoStopLiveView;

    /**
     * AprilTag decimation value.
     *
     * <p>
     * Higher values improve performance
     * but reduce detection accuracy at long distances.
     * </p>
     */
    public final float decimation;

    /**
     * Creates a new immutable camera configuration.
     *
     * @param resolutionWidth camera width in pixels.
     * @param resolutionHeight camera height in pixels.
     * @param streamEnabled enables live camera preview.
     * @param autoStopLiveView automatically stops live view.
     * @param decimation AprilTag decimation value.
     */
    private CameraConfig(int resolutionWidth,
                         int resolutionHeight,
                         boolean streamEnabled,
                         boolean autoStopLiveView,
                         float decimation) {

        this.resolutionWidth  = resolutionWidth;
        this.resolutionHeight = resolutionHeight;
        this.streamEnabled    = streamEnabled;
        this.autoStopLiveView = autoStopLiveView;
        this.decimation       = decimation;
    }

    /**
     * Returns the recommended default camera configuration.
     *
     * <p>
     * Default values:
     * </p>
     *
     * <ul>
     *     <li>Resolution: 1280x720</li>
     *     <li>Live stream enabled</li>
     *     <li>Auto stop live view enabled</li>
     *     <li>Decimation: 2</li>
     * </ul>
     *
     * @return default camera configuration.
     */
    public static CameraConfig defaultConfig() {

        return new CameraConfig(
                1280,
                720,
                true,
                true,
                2
        );
    }

    /**
     * Creates a custom camera configuration.
     *
     * @param width camera width in pixels.
     * @param height camera height in pixels.
     * @param decimation AprilTag decimation value.
     * @param streamEnabled enables live camera preview.
     * @param autoStopLiveView automatically stops live view.
     *
     * @return custom camera configuration.
     */
    public static CameraConfig build(int width,
                                     int height,
                                     float decimation,
                                     boolean streamEnabled,
                                     boolean autoStopLiveView) {

        return new CameraConfig(
                width,
                height,
                streamEnabled,
                autoStopLiveView,
                decimation
        );
    }
}