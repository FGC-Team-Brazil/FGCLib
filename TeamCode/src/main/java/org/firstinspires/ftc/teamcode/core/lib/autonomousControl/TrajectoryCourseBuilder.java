package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;
//i think this is done
public class TrajectoryCourseBuilder {
    TrajectoryCourse trajectoryCourseUnderConstruction;
    public TrajectoryCourseBuilder startTrajectory(Pose2d lastPose,double startTangent,double endTangent){
        trajectoryCourseUnderConstruction = new TrajectoryCourse(lastPose,startTangent,endTangent);
        return this;
    }
    public TrajectoryCourseBuilder addSegment(Pose2d end,double endTangent){
        trajectoryCourseUnderConstruction.addPose2d(end,endTangent);
        return this;
    }
    public TrajectoryCourse build(){
        return trajectoryCourseUnderConstruction;
    }
    /*
    adds a segment to a trajectory course builder using the TrajectoryCourse.addPose2d, keeping it stored as Pose2d values that are only actually converted
    to the position lists when the runner calls the TrajectoryCourse.calculateSegment() method for each segment of a specific Trajetory Course
     */
}
