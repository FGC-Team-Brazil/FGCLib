package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;
//this is done
public class Pose2d {
    double XPos;
    double YPos;
    double head;
    public Pose2d(double xPosition, double yPosition, double heading){
        /*
        xPosition means the X axis position relative to the field
        yPosition means the Y axis position relative to the field
        heading means the orientation of the robot in degrees relative to the field
         */
        XPos = xPosition;
        YPos = yPosition;
        head = heading;
    }

    public double getX(){
        return XPos;
    }
    public double getY(){
        return YPos;
    }
    public double getHeadingRadians(){
        return head;
    }
    public double getHeadingDegrees(){
        return Math.toDegrees(head);
    }

    public void updatePose(double newX, double newY, double newHead){
        XPos = newX;
        YPos = newY;
        head = Math.toRadians(newHead);
    }

}
