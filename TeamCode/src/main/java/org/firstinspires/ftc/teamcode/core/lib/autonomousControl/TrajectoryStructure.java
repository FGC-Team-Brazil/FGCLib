package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;

public interface TrajectoryStructure {
    enum StructureType{
        COURSE,
        TIME_STOP,
        COMMAND
    }
    StructureType getType();

    void start(double stTime);

    boolean execute(Pose2d botPosition, RobotMovementState currentBotState);
    Pose2d getLastPose2d();
}
