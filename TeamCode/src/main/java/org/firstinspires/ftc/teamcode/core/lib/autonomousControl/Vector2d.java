package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

public class Vector2d {
    /*
    a vector in this case is pretty much just a point, it is something used
    in the control nodes for the making of a trajectory couse
     */
    double XPos;
    double YPos;

    Vector2d(double xPosition, double yPosition){
        /*
        xPosition means the X axis position relative to the field
        yPosition means the Y axis position relative to the field
        heading means the orientation of the robot in degrees relative to the field
         */
        XPos = xPosition;
        YPos = yPosition;

    }

    public double getX(){
        return XPos;
    }
    public double getY(){
        return YPos;
    }


}
