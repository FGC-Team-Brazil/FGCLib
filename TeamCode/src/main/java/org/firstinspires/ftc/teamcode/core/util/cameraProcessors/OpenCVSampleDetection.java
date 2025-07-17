package org.firstinspires.ftc.teamcode.core.util.cameraProcessors;
import android.graphics.Canvas;
import android.icu.text.Transliterator;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;

public class OpenCVSampleDetection implements VisionProcessor {
    Telemetry telemetry;
    public Scalar lowerHSV = new Scalar(6.0, 60.0, 85.0, 0.0);
    public Scalar upperHSV = new Scalar(69.0, 252.0, 255.0, 0.0);
    private Mat hsvMat = new Mat();
    private Mat hsvBinaryMat = new Mat();

    public int erodeValue = ((int) (9));
    public int dilateValue = ((int) (9));
    private Mat element = null;
    private Mat hsvBinaryMatErodedDilated = new Mat();

    private ArrayList<MatOfPoint> contours = new ArrayList<>();
    private Mat hierarchy = new Mat();

    public Scalar lineColor = new Scalar(255.0, 0.0, 255.0, 0.0);
    public int lineThickness = 3;

    private Mat inputContours = new Mat();

    private MatOfPoint biggestContour = null;
    private Point centerOfLargestContour= null;
    @Override
    public Mat processFrame(Mat input, long captureTimeNanos) {
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);
        Core.inRange(hsvMat, lowerHSV, upperHSV, hsvBinaryMat);

        hsvBinaryMat.copyTo(hsvBinaryMatErodedDilated);
        if(erodeValue > 0) {
            this.element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(erodeValue, erodeValue));
            Imgproc.erode(hsvBinaryMatErodedDilated, hsvBinaryMatErodedDilated, element);

            element.release();
        }

        if(dilateValue > 0) {
            this.element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(dilateValue, dilateValue));
            Imgproc.dilate(hsvBinaryMatErodedDilated, hsvBinaryMatErodedDilated, element);

            element.release();
        }

        contours.clear();
        hierarchy.release();
        Imgproc.findContours(hsvBinaryMatErodedDilated, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        this.biggestContour = null;
        for(MatOfPoint contour : contours) {
            if((biggestContour == null) || (Imgproc.contourArea(contour) > Imgproc.contourArea(biggestContour))) {
                this.biggestContour = contour;
            }
        }

        if(biggestContour != null) {
            Rect boundingRect = Imgproc.boundingRect(biggestContour);

            double centroidX = (boundingRect.tl().x + boundingRect.br().x) / 2;
            double centroidY = (boundingRect.tl().y + boundingRect.br().y) / 2;

            Point centroid = new Point(centroidX, centroidY);
            double contourArea = Imgproc.contourArea(biggestContour);

            Scalar crosshairCol = new Scalar(0.0, 255.0, 0.0);
            Imgproc.drawContours(input,contours,-1,lineColor,lineThickness);
            Imgproc.line(input, new Point(centroidX - 10, centroidY), new Point(centroidX + 10, centroidY), crosshairCol, 5);
            Imgproc.line(input, new Point(centroidX, centroidY - 10), new Point(centroidX, centroidY + 10), crosshairCol, 5);
            centerOfLargestContour = new Point(centroidX,centroidY);
        }

        return input;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    public Point getCenterOfLargestContour(){
        return centerOfLargestContour;
    }
}
