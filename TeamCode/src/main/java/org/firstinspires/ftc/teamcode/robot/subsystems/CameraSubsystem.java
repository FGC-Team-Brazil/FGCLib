package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.util.cameraProcessors.MyAprilTagProcessor;
import org.firstinspires.ftc.teamcode.core.util.cameraProcessors.OpenCVSampleDetection;
import org.firstinspires.ftc.teamcode.robot.constants.CameraConstants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessorImpl;
import org.opencv.core.Mat;

import android.graphics.Canvas;
import android.util.Size;
public class CameraSubsystem implements Subsystem {
    CameraSubsystem(){

    }
    static CameraSubsystem instance;
    VisionPortal.Builder myVisionPortalBuilder;
    VisionPortal myVisionPortal;
    MyAprilTagProcessor normalATprocessor;
    OpenCVSampleDetection openCVDetection;

    void initOpenCV(){
        openCVDetection = new OpenCVSampleDetection();
    }
    AprilTagProcessor initAprilTag() {
        AprilTagProcessor myAprilTagProcessor;
        // Create the AprilTag processor.
        myAprilTagProcessor = new AprilTagProcessor.Builder()
                // The following default settings are available to comment/un-comment and edit as needed.
                .setDrawAxes(false)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(false)
                // .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                // .setTagLibrary()
                // .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
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
        myAprilTagProcessor.setDecimation(3);
        return myAprilTagProcessor;
    }
    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        normalATprocessor = new MyAprilTagProcessor();
        initOpenCV();
        // Create a new VisionPortal Builder object.
        myVisionPortalBuilder = new VisionPortal.Builder();

        // Specify the camera to be used for this VisionPortal.
        myVisionPortalBuilder.setCamera(hardwareMap.get(WebcamName.class, CameraConstants.WebcamName));      // Other choices are: RC phone camera and "switchable camera name".

        // Add the AprilTag Processor to the VisionPortal Builder.
        myVisionPortalBuilder.addProcessor(normalATprocessor);//Adds April Tag detection processor
        myVisionPortalBuilder.addProcessor(openCVDetection);//Adds Basic OpenCVSampleDetection detection processor
        //myVisionPortalBuilder.addProcessor(initAprilTag()); see if other works first, otherwise use this
        // Optional: set other custom features of the VisionPortal (4 are shown here).
        myVisionPortalBuilder.setCameraResolution(new Size(640, 480));  // Each resolution, for each camera model, needs calibration values for good pose estimation.
        myVisionPortalBuilder.setStreamFormat(VisionPortal.StreamFormat.YUY2);  // MJPEG format uses less bandwidth than the default YUY2.
        myVisionPortalBuilder.enableLiveView(true);   // Enable LiveView (RC preview).
        myVisionPortalBuilder.setAutoStopLiveView(true);     // Automatically stop LiveView (RC preview) when all vision processors are disabled.

        // Create a VisionPortal by calling build()
        myVisionPortal = myVisionPortalBuilder.build();

        myVisionPortal.setProcessorEnabled(openCVDetection, false);
        myVisionPortal.setProcessorEnabled(normalATprocessor, false);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        //myVisionPortal.setProcessorEnabled(openCVDetection, false);
        //myVisionPortal.setProcessorEnabled(myAprilTagProcessor, false);
        myVisionPortal.close();
    }

    @Override
    public void execute(GamepadManager gamepadManager) {

    }
    public static CameraSubsystem getInstance() {
        if (instance == null) {
            instance = new CameraSubsystem();
        }
        return instance;
    }

    public void loopAprilTags(){

    }
}
