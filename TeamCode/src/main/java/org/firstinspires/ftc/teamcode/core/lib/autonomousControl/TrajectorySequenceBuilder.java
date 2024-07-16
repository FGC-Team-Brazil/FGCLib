package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;

public class TrajectorySequenceBuilder {
    /*
    this will add the trajectory sequences and external action triggers to the
    actual trajectory sequence being created
     */
    TrajectorySequence sequenceUnderConstruction;
    TrajectoryCourseBuilder courseUnderConstruction;
    int currentStructureID=0;
    int currentStructureSegmentID=0;
    Pose2d lastPose2d = DrivetrainBuilder.getInstance().getCurrentPose();
    public TrajectorySequenceBuilder(){
        sequenceUnderConstruction = new TrajectorySequence();
    }
    public TrajectorySequenceBuilder startTrajectoryCourse(Pose2d end,double startTangent,double endTangent){
        courseUnderConstruction = new TrajectoryCourseBuilder();
        courseUnderConstruction = courseUnderConstruction
                                    .startTrajectory(lastPose2d,startTangent,endTangent)
                                    .addSegment(end,endTangent);
        return this;
    }
    public TrajectorySequenceBuilder addCourseSegment(Pose2d end,double endTangent){
        courseUnderConstruction = courseUnderConstruction.addSegment(end,endTangent);
        lastPose2d = end;
        currentStructureSegmentID++;
        return this;
    }


    public TrajectorySequenceBuilder holdPositionForSeconds(double seconds){
        sequenceUnderConstruction.TrajectoryStructureList.add(new TimeStop(seconds,lastPose2d));
        /*
        adds to the sequence some time that the robot spends stationary at the end of a trajectory course and stops the robot for the said
         amount of time

         must be used after a built trajectory course
        */
        return this;
    }
    public TrajectorySequenceBuilder addBasicCommand(Runnable runnable){
        sequenceUnderConstruction.CommandList.add(new BasicCommand(runnable,currentStructureID,currentStructureSegmentID));
        return this;
    }

    //public TrajectorySequenceBuilder addDisplacementCommand(double travelDistance,Runnable runnable){
        //sequenceUnderConstruction.TrajectoryStructureList make actual command list to handle this
            //return this;
    //}



    public TrajectorySequenceBuilder buildCourse(){
        sequenceUnderConstruction.TrajectoryStructureList.add(courseUnderConstruction.build());
        currentStructureID++;
        currentStructureSegmentID=0;
        return this;
    }

    public TrajectorySequence buildSequence() {
        return sequenceUnderConstruction;
    }

    /*
    this will probably be broken up in time and position commands, but the purpose will be to make the robot do stuff while it is still moving in autonomous

    how these comands will work :
    first a command is assigned to a n*100 index on the Trajectory course itself, once the pure pursuit target reaches that point the Trajectory Sequence Runner knows it (because
    the trajectory sequence runner will say it) and will wait until the robot travels certain cms or waits a certain number of seconds (according to which type od command it is)
    and then runs the assigned lambda to the command, moving the arm or whatever the command does.

    there will also be a basic type of command that just executes the lambda as soon as its assigned id is reached because that is very useful to have

     */


}
