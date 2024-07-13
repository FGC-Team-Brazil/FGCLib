package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;
//this is done
public class TargetVelocityData {
    double XVel;
    double YVel;
    double XV_YV_alpha;
    double heading;
    double index;
    TargetVelocityData(double xVelocity, double yVelocity, double alpha,double desiredHeading,int CourseIndex){
        /*
        xPosition means the X axis position relative to the field
        yPosition means the Y axis position relative to the field
        heading means the orientation of the robot in degrees relative to the field
         */
        XVel = xVelocity;
        YVel = yVelocity;
        XV_YV_alpha = alpha;
        heading = desiredHeading;
        index = CourseIndex;
    }

    public double getXV(){
        return XVel;
    }
    public double getYV(){
        return YVel;
    }
    public double getH(){return heading;}
    public double getAlphaRadians(){
        return XV_YV_alpha;
    }
    public double getAlphaDegrees(){
        return Math.toDegrees(XV_YV_alpha);
    }


    public void updateVelocityData(double newXV, double newYV, double newAlpha,int newID){
        XVel = newXV;
        YVel = newYV;
        XV_YV_alpha = newAlpha;
        index = newID;
    }

}
