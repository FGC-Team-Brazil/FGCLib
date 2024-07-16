package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.firstinspires.ftc.teamcode.robot.constants.AutonomousConstants;

public class RobotMovementState {
    //i think this whole class could be eliminated, but i have to develop
    //the trajectoryCourseRunner first to see if that is the case

    double VX;
    double VY;
    double AX;
    double AY;
    double elapsedTime;
    double deaccelerationDistance;

    public RobotMovementState(double startVX, double startVY){
        VX = startVX;
        VY = startVY;
        AX = 0;
        AY = 0;
        elapsedTime = 0;
        deaccelerationDistance = -(Math.pow(AutonomousConstants.MAXACCELERATION,2)+Math.pow(AutonomousConstants.MAXSPEED,2))/(2*AutonomousConstants.MAXACCELERATION);
    }

    public RobotMovementState update(double ax, double ay,double timePassed){
        elapsedTime+=timePassed;
        VX += ax*timePassed;
        VY += ay*timePassed;
        return this;
    }
}
