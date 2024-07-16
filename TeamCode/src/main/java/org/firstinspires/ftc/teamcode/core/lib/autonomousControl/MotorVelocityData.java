package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.firstinspires.ftc.teamcode.robot.constants.AutonomousConstants;

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
        if (Math.abs(Vheading) > AutonomousConstants.MAX_HEADING_VELOCITY){
            Vheading = Math.signum(Vheading)*AutonomousConstants.MAX_HEADING_VELOCITY;
        }
        velocityFrontLeft = botVX + botVY +Vheading;
        velocityFrontRight= botVX - botVY -Vheading;
        velocityBackLeft=   botVX - botVY +Vheading;
        velocityBackRight=  botVX + botVY -Vheading;

        return this;
    }
    public MotorVelocityData normalize (){
        double divider = Math.max(Math.max(Math.max(Math.abs(velocityBackLeft),Math.abs(velocityFrontLeft)),
                Math.max(Math.abs(velocityBackRight),Math.abs(velocityFrontRight))),1);
        velocityFrontRight/=divider;
        velocityBackRight/=divider;
        velocityFrontLeft/=divider;
        velocityBackLeft/=divider;
        return this;
    }

}
