package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.robot.constants.AutonomousConstants;

import java.util.ArrayList;
//this is done
public class TrajectoryCourse implements TrajectoryStructure{
    /*
    A trajectory course is the way in which our lib stores the path the robot takes between two stops,
    it contains a list of coordinates between these two stops given by a parametric bezier curve.

    The list contains only the current trajectory segment, this is done to reduce calculation times before the
    robot actually begins movement and spread it across the entire autonomous. This way we don't suffer from the same
    time to load problem faced by RoadRunner, which can take a couple seconds to start the movement.

    As a result this lib is more applicable to real time movement recalculation and webcam-using odometry
     */
    private int LastIndex =0;
    private int CurrentIndex=0;
    int currentSegmentId;
    TargetVelocityData vData = new TargetVelocityData(0,0,0,0,0 );

    @Override
    public StructureType getType(){
        return StructureType.COURSE;
    }

    ArrayList<Pose2d> pose2dList = new ArrayList<>();
    ArrayList<Double> tangentList = new ArrayList<>();
    ArrayList<Double> xPointList = new ArrayList<>();
    ArrayList<Double> yPointList = new ArrayList<>();

    public void addPose2d(Pose2d newPose2d,double endTangent){
        pose2dList.add(newPose2d);
        tangentList.add(endTangent);
    }
    TrajectoryCourse(Pose2d startPose,double startTangent,double endTangent){
        pose2dList.add(startPose);
        tangentList.add(startTangent);
        tangentList.add(endTangent);
    }

    public void calculateSegment(Pose2d start, Pose2d end,double startTangent,double endTangent){
        int startT;
        double bezierControlConstant = Math.E;
        double segmentLength = Math.hypot(Math.pow(end.getX() - start.getX(),2)
                                        , Math.pow(end.getY() - start.getY(),2));
        /*
        functions that could be future added idk

        //ArrayList<Double> timeAtIndexList = new ArrayList<Double>();
        //ArrayList<Double> DistanceTraveledAtIndexList = new ArrayList<Double>();
        //double currentCourseTime = 0;
        //double currentDistanceTraveled =0;

         */
        double x1 = start.getX();
        double y1 = start.getY();
        double x2 = segmentLength/ bezierControlConstant *Math.cos(Math.toRadians(startTangent))+start.getX();
        double y2 = segmentLength/ bezierControlConstant *Math.sin(Math.toRadians(startTangent))+start.getY();
        double x3 = segmentLength/ bezierControlConstant *Math.cos(Math.toRadians(endTangent))+end.getX();
        double y3 = segmentLength/ bezierControlConstant *Math.sin(Math.toRadians(endTangent))+end.getY();
        double x4 = end.getX();
        double y4 = end.getY();

        if (!xPointList.isEmpty()){
            //this prevents calculating the same point twice at the beginning and end of two consecutive segments
            startT=1;
        } else{
            startT = 0;
        }

        for (int at = startT; at<=100; at++){
            double t = at/100.0;
            double xxx = t*t*t*(x4+3*(x2-x3)-x1)+t*t*(3*(x3-2*x2+x1))+3*t*(x2-x1)+x1;
            double yyy = t*t*t*(y4+3*(y2-y3)-y1)+t*t*(3*(y3-2*y2+y1))+3*t*(y2-y1)+y1;

            xPointList.add(xxx);
            yPointList.add(yyy);
            //DistanceTraveledAtIndexList.add(currentDistanceTraveled);
            //timeAtIndexList.add(currentCourseTime);

        }
    }

    public void updateCurrentIndex(Pose2d currentBotPosition, RobotMovementState currentBotState){
        double distanceToNextIndex;
        double distanceToThisIndex;

        distanceToThisIndex = Math.hypot(currentBotPosition.getX()-xPointList.get(CurrentIndex)
                ,currentBotPosition.getY()-yPointList.get(CurrentIndex));

        while (true){
            distanceToNextIndex = Math.hypot(currentBotPosition.getX()-xPointList.get(CurrentIndex)
                    ,currentBotPosition.getY()-yPointList.get(CurrentIndex));

            if(distanceToNextIndex>distanceToThisIndex) {break;}

            if (CurrentIndex+1<yPointList.size()){CurrentIndex++;}

            distanceToThisIndex = distanceToNextIndex;
        }
        LastIndex =CurrentIndex;
        double lookForwardDistance = AutonomousConstants.SPEEDPROPORTIONALGAIN * Math.hypot(currentBotState.VX, currentBotState.VY) +
                AutonomousConstants.LOOKFORWARDCONSTANT;

        while (lookForwardDistance >distanceToNextIndex){
            distanceToNextIndex = Math.hypot(currentBotPosition.getX()-xPointList.get(CurrentIndex)
                    ,currentBotPosition.getY()-yPointList.get(CurrentIndex));
            if (CurrentIndex+1>yPointList.size()){break;}
            CurrentIndex++;
        }

    }
    public int getSegmentID(){
        return (int) Math.round((vData.index-50)/100.0);
    }
    public int segmentIDatIndex(int index){
        return (int) Math.round(((double)index-50)/100);
    }


    public TargetVelocityData targetSpeedControl(Pose2d currentBotPosition, RobotMovementState currentBotState){
        double xVelocity = 0;
        double yVelocity = 0;
        LastIndex = CurrentIndex;
        updateCurrentIndex(currentBotPosition,currentBotState);

        if(LastIndex>CurrentIndex){
            CurrentIndex=LastIndex; //prevents robot going back the trajectory
        }
        // calculates desired X and Y speed
        double alpha = Math.atan2(yPointList.get(CurrentIndex)-currentBotPosition.YPos,
        xPointList.get(CurrentIndex)-currentBotPosition.XPos);
        if (segmentIDatIndex(CurrentIndex)==segmentIDatIndex(yPointList.size())){
            double distanceToCourseEnd = Math.hypot(xPointList.get(CurrentIndex)-currentBotPosition.getX(),
                    yPointList.get(CurrentIndex)-currentBotPosition.getY());
            if(distanceToCourseEnd<currentBotState.deaccelerationDistance){
                xVelocity =Math.cos(alpha)*AutonomousConstants.MAXSPEED*(distanceToCourseEnd/currentBotState.deaccelerationDistance)+Math.cos(alpha)*AutonomousConstants.MAXACCELERATION;
                yVelocity =Math.sin(alpha)*AutonomousConstants.MAXSPEED*(distanceToCourseEnd/currentBotState.deaccelerationDistance)+Math.sin(alpha)*AutonomousConstants.MAXACCELERATION;
            }
        } else {
            xVelocity =Math.cos(alpha)*AutonomousConstants.MAXSPEED;
            yVelocity =Math.sin(alpha)*AutonomousConstants.MAXSPEED;
        }
        double desiredHeading = (CurrentIndex%100)*(pose2dList.get(getSegmentID()).getHeadingRadians()-pose2dList.get(getSegmentID()+1).getHeadingRadians())+pose2dList.get(getSegmentID()).getHeadingRadians();
        return new TargetVelocityData(xVelocity,yVelocity,alpha,desiredHeading,CurrentIndex);

    }

    @Override
    public void start(double startTime) {
        for (int i=0;i<pose2dList.size() - 1;i++){
            calculateSegment(pose2dList.get(i),pose2dList.get(i+1),tangentList.get(i),tangentList.get(i+1));
        }
        CurrentIndex = 0;
        LastIndex = 0;
    }
    @Override
    public boolean execute(Pose2d currentBotPosition,RobotMovementState currentBotState,double elapsedTime){
        currentSegmentId = (int) Math.round(((double) CurrentIndex-50)/100);
        vData = targetSpeedControl(currentBotPosition,currentBotState);

        DrivetrainBuilder.getInstance().controlBasedOnVelocity(vData,elapsedTime);

        return (CurrentIndex==xPointList.size()-1)&&
                (Math.abs(DrivetrainBuilder.getInstance().getCurrentPose().getHeadingDegrees() - pose2dList.get(getSegmentID()).getHeadingDegrees())) <= AutonomousConstants.TOLERATED_HEADING_ERROR;
                //this checks whether or not the Structure is done being followed
    }
    @Override
    public Pose2d getLastPose2d(){
        return pose2dList.get(pose2dList.size()-1);
    }
}
