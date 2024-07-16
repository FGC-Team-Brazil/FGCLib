package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

public interface TrajectoryStructure {
    enum StructureType{
        COURSE,
        TIME_STOP
    }
    StructureType getType();
    public int getSegmentID();

    void start(double stTime);


    boolean execute(Pose2d currentBotPosition, RobotMovementState currentBotState, double elapsedTime);

    Pose2d getLastPose2d();
}
