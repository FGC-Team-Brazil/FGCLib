package org.firstinspires.ftc.teamcode.core.util.cameraProcessors;
import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MyAprilTagProcessor implements VisionProcessor {
    public static AprilTagProcessor aprilTagDetect;
    void initAprilTag() {
            // Create the AprilTag processor.
                aprilTagDetect = new AprilTagProcessor.Builder()
                        // The following default settings are available to comment/un-comment and edit as needed.
                        .setDrawAxes(false)
                        .setDrawCubeProjection(true)
                        .setDrawTagOutline(false)
                        .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                        // .setTagLibrary()
                        .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                        // .setCameraPose(CameraConstants.CameraPosition, CameraConstants.CameraOrientation)

                        // == CAMERA CALIBRATION ==
                        // If you do not manually specify calibration parameters, the SDK will attempt
                        // to load a predefined calibration for your camera.
                        //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                        // ... these parameters are fx, fy, cx, cy.
                        .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.

    }


    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        initAprilTag();
        aprilTagDetect.init(width,height,calibration);
        aprilTagDetect.setDecimation(1);
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        aprilTagDetect.processFrame(frame,captureTimeNanos);
        Point[] point = aprilTagDetect.getDetections().get(0).corners;
        Mat temp = new Mat();
        frame.copyTo(temp);
        Imgproc.line(temp, point[0], point[1],new Scalar(0,255,0), 5);
        return temp;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}
