package org.firstinspires.ftc.teamcode.core.lib.vision.camera;

import org.firstinspires.ftc.teamcode.robot.constants.CameraConstants;

public class CameraConfig {

    public final int     resolutionWidth;
    public final int     resolutionHeight;
    public final boolean streamEnabled;
    public final boolean autoStopLiveView;
    public final float   decimation;

    private CameraConfig(int resolutionWidth, int resolutionHeight,
                         boolean streamEnabled, boolean autoStopLiveView,
                         float decimation) {
        this.resolutionWidth  = resolutionWidth;
        this.resolutionHeight = resolutionHeight;
        this.streamEnabled    = streamEnabled;
        this.autoStopLiveView = autoStopLiveView;
        this.decimation       = decimation;
    }

    public static CameraConfig defaultConfig() {
        return new CameraConfig(
                CameraConstants.DEFAULT_WIDTH,
                CameraConstants.DEFAULT_HEIGHT,
                true,
                true,
                CameraConstants.DEFAULT_DECIMATION
        );
    }

    public static CameraConfig build(int width, int height, float decimation,
                                     boolean streamEnabled, boolean autoStopLiveView) {
        return new CameraConfig(width, height, streamEnabled, autoStopLiveView, decimation);
    }
}