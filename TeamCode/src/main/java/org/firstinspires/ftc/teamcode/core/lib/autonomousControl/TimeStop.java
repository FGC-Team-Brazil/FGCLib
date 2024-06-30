package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;

public class TimeStop implements TrajectoryStructure{
    Pose2d position;
    double waitTime;
    double startTime;
    TimeStop(double seconds,Pose2d positionToHold){
        waitTime=seconds;
        position = positionToHold;
    }
    @Override
    public StructureType getType() {
        return StructureType.TIME_STOP;
    }

    @Override
    public int getSegmentID() {
        return 0;
    }

    @Override
    public void start(double stTime) {
        startTime =stTime;
    }

    @Override
    public boolean execute(Pose2d botPosition,RobotMovementState currentBotState,double elapsedTime) {

        //DrivetrainBuilder.controlBasedOnVelocity();
        // todo make this return an appropriate velocity data as a paramether to the drivebase that keeps robot stationary
        return currentBotState.elapsedTime - startTime >= waitTime;
    }

    @Override
    public Pose2d getLastPose2d() {
        return position;
    }
}
