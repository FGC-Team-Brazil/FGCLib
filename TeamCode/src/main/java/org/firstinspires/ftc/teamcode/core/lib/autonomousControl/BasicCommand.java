package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

public class BasicCommand implements AutonomousCommand{
    int CourseID = 0;
    int CourseSegmentID =0;
    boolean conditionMet = false;
    public boolean isConditionMetYet(){
        return conditionMet;
    }



    public enum CommandType{
        BASIC,
        TIME_DELAY,
        MOVEMENT_DELAY
    }
    CommandType commandType = CommandType.BASIC;
    Runnable personalCommand;
    BasicCommand (Runnable runnable,int courseID,int segmentID)
    {
        personalCommand = runnable;
        CourseID =courseID;
        CourseSegmentID = segmentID;
    }
    public void executeCommand(){
        if(conditionMet){
            personalCommand.run();
        }
    }

    @Override
    public boolean runConditionMet(int currentCourseID,int currentCourseSegmentID) {
        conditionMet = (CourseID == currentCourseID &&CourseSegmentID == currentCourseSegmentID);
        return conditionMet;
    }
}
