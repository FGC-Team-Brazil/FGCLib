package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.util.cameraProcessors.OpenCVSampleDetection;
import org.firstinspires.ftc.teamcode.robot.constants.CameraConstants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import android.util.Size;
public class CameraSubsystemExample implements Subsystem {

    CameraSubsystemExample(){

    }
    private Telemetry telemetry;
    static CameraSubsystemExample instance;
    VisionPortal.Builder myVisionPortalBuilder;
    VisionPortal myVisionPortal;
    OpenCVSampleDetection openCVDetection;
    AprilTagProcessor myAprilTagProcessor;
    /* The original file is OpenCVSampleDetection and serves as an example of color
    detection pipeline.

     */
    void initOpenCV(){
        openCVDetection = new OpenCVSampleDetection();
    }
    /*
    This section is all the code needed to initialize the AprilTag Processor
    Is does not need a separate file because the FTC SDK comes with a AprilTagProcessorBuilder
    that already does most of the work needed
     */
    AprilTagProcessor initAprilTag() {
        // Create the AprilTag processor.
        myAprilTagProcessor = new AprilTagProcessor.Builder()
                // The following default settings are available to comment/un-comment and edit as needed.
                .setDrawAxes(false)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(false)
                .setTagLibrary(CameraConstants.getEcoEquilibriumTagLibrary())
                // .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                // .setCameraPose(CameraConstants.CameraPosition, CameraConstants.CameraOrientation) //the specific camera position can be set on the robot with this command

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
        myAprilTagProcessor.setDecimation(2);
        return myAprilTagProcessor;
    }
    /*
    On initialize() we set up the Vision Portal, which allows us to control all
    active processors at the same time, when building a vision portal, pass all your
    vision processors through the .addProcessor(Vision Processor) method to add them to the vision portal
     */
    @Override
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        initOpenCV();
        // Create a new VisionPortal Builder object.
        myVisionPortalBuilder = new VisionPortal.Builder();

        // Specify the camera to be used for this VisionPortal.
        myVisionPortalBuilder.setCamera(hardwareMap.get(WebcamName.class, CameraConstants.WebcamName));      // Other choices are: RC phone camera and "switchable camera name".

        // Add the AprilTag Processor to the VisionPortal Builder.
        myVisionPortalBuilder.addProcessor(openCVDetection);//Adds Basic OpenCVSampleDetection detection processor
        myVisionPortalBuilder.addProcessor(initAprilTag());
        // Optional: set other custom features of the VisionPortal (4 are shown here).
        myVisionPortalBuilder.setCameraResolution(new Size(640, 480));  // Each resolution, for each camera model, needs calibration values for good pose estimation.
        myVisionPortalBuilder.setStreamFormat(VisionPortal.StreamFormat.YUY2);  // MJPEG format uses less bandwidth than the default YUY2.
        myVisionPortalBuilder.enableLiveView(true);   // Enable LiveView (RC preview).
        myVisionPortalBuilder.setAutoStopLiveView(true);     // Automatically stop LiveView (RC preview) when all vision processors are disabled.

        // Create a VisionPortal by calling build()
        myVisionPortal = myVisionPortalBuilder.build();

        myVisionPortal.setProcessorEnabled(openCVDetection, false);
        myVisionPortal.setProcessorEnabled(myAprilTagProcessor, true);
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
    /*
    Here we have a simple example of how data can be colected from the vision processors during run time
     */
    @Override
    public void execute(GamepadManager gamepadManager) {
        if (openCVDetection.getCenterOfLargestContour() != null){
            telemetry.addData("center of largest detection", "X = " + openCVDetection.getCenterOfLargestContour().x + "Y= " + openCVDetection.getCenterOfLargestContour().y);
        } else {
            telemetry.addLine("No openCV detection :(");
        }
        if (!myAprilTagProcessor.getDetections().isEmpty()){
            telemetry.addData("detection of center of first april tag", "X = "+myAprilTagProcessor.getDetections().get(0).center.x,"Y = "+myAprilTagProcessor.getDetections().get(0).center.y);
        } else {
            telemetry.addLine("No april tag on frame");
        }
    }
    public static CameraSubsystemExample getInstance() {
        if (instance == null) {
            instance = new CameraSubsystemExample();
        }
        return instance;
    }
    //The following methods are how we can enable and disable processors by making calls from inside or outside the subsystem
    public void enableAprilTags(){
        myVisionPortal.setProcessorEnabled(myAprilTagProcessor,true);
    }
    public void disableAprilTags(){
        myVisionPortal.setProcessorEnabled(myAprilTagProcessor,false);
    }
    public void enableOpenCV(){
        myVisionPortal.setProcessorEnabled(openCVDetection,true);
    }
    public void disableOpenCV(){
        myVisionPortal.setProcessorEnabled(openCVDetection,false);
    }


}
