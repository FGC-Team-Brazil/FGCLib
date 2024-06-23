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

    RobotMovementState(Pose2d RobotPosition,
                       double startVX,double startVY){
        double VX = startVX;
        double VY = startVY;
        double AX = 0;
        double AY = 0;
        double elapsedTime = 0;
        double deaccelerationDistance = -(Math.pow(AutonomousConstants.MAXACCELERATION,2)+Math.pow(AutonomousConstants.MAXSPEED,2))/(2*AutonomousConstants.MAXACCELERATION);
    }

    public void update(double ax, double ay,double timePassed){
        elapsedTime+=timePassed;
        VX += ax*timePassed;
        VY += ay*timePassed;

    }
}
