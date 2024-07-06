package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

public class MotorVelocityData {
    public double velocityFrontLeft;
    public double velocityFrontRight;
    public double velocityBackLeft;
    public double velocityBackRight;
    public MotorVelocityData(){

    }
    public MotorVelocityData(double vFL,double vFR, double vBL,double vBR){
        velocityFrontLeft = vFL;
        velocityFrontRight = vFR;
        velocityBackLeft = vBL;
        velocityBackRight = vBR;
    }
    public MotorVelocityData updateAppliedVelocities (Pose2d botRelativeVelocitiy){
        double botVX = botRelativeVelocitiy.getX();
        double botVY = botRelativeVelocitiy.getY();
        double Vheading = botRelativeVelocitiy.getHeadingRadians();
        velocityFrontLeft = botVX + botVY +Vheading;
        velocityFrontRight= botVX - botVY -Vheading;
        velocityBackLeft=   botVX - botVY +Vheading;
        velocityBackRight=  botVX + botVY -Vheading;

        return this;
    }

}
