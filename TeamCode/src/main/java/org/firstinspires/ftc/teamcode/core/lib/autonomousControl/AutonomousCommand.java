package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

public interface AutonomousCommand {

    void executeCommand();
    boolean runConditionMet(int currentCourseID,int currentCourseSegmentID);
}
