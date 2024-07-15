package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.checkerframework.checker.units.qual.A;
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

    ArrayList<Pose2d> pose2dList = new ArrayList<Pose2d>();
    ArrayList<Double> tangentList = new ArrayList<>();
    ArrayList<Double> xPointList = new ArrayList<Double>();
    ArrayList<Double> yPointList = new ArrayList<Double>();
    //ArrayList<Double> timeAtIndexList = new ArrayList<Double>();
    //ArrayList<Double> DistanceTraveledAtIndexList = new ArrayList<Double>();
    //double currentCouseTime = 0;
    //double currentDistanceTraveled =0;
    private final double bezierControlConstant = Math.E;
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
        double segmentLengh = Math.hypot(Math.pow(end.getX() - start.getX(),2)
                                        , Math.pow(end.getY() - start.getY(),2));
        Vector2d ControlPoint1 = new Vector2d(
                segmentLengh/bezierControlConstant*Math.cos(Math.toRadians(startTangent))+start.getX(),
                segmentLengh/bezierControlConstant*Math.sin(Math.toRadians(startTangent))+start.getY()
        );
        Vector2d ControlPoint2 = new Vector2d(
                segmentLengh/bezierControlConstant*Math.cos(Math.toRadians(endTangent))+end.getX(),
                segmentLengh/bezierControlConstant*Math.sin(Math.toRadians(endTangent))+end.getY()
        );
        if (!xPointList.isEmpty()){
            //this prevents calculating the same point twice at the beginning and end of two consecutive segments
            startT=1;

        } else{
            startT = 0;
        }

        for (int t = startT; t<=100; t++){
            /*
            in here we have the fully expanded code for ease of comprehension, it may be a good idea to minify this to
            reduce computation time and make it a bit faster at calculating, expanded equation can be left as a comment
            in other words: the reduction of this math stuff is left as an exercise for the reader :)
             */

            //the variable t was used instead of the usual i in the for loop because this is the norm for parametric functions

            //main points are calculated according to t
            Vector2d linearPoint1 = new Vector2d(
                    (ControlPoint1.getX()-start.getX())*t/100+start.getX(),
                    (ControlPoint1.getY()-start.getY())*t/100+start.getY());
            Vector2d linearPoint2 = new Vector2d(
                    (ControlPoint2.getX()-ControlPoint1.getX())*t/100+ControlPoint1.getX(),
                    (ControlPoint2.getY()-ControlPoint1.getY())*t/100+ControlPoint1.getY());
            Vector2d linearPoint3 = new Vector2d(
                    (end.getX()-ControlPoint2.getX())*t/100+ControlPoint2.getX(),
                    (end.getY()-ControlPoint2.getY())*t/100+ControlPoint2.getY());

            //then secondary points are calculated taking into account the linear points
            Vector2d quadraticPoint1 = new Vector2d(
                    (linearPoint2.getX()-linearPoint1.getX())*t/100+linearPoint1.getX(),
                    (linearPoint2.getY()-linearPoint1.getY())*t/100+linearPoint1.getY());
            Vector2d quadraticPoint2 = new Vector2d(
                    (linearPoint3.getX()-linearPoint2.getX())*t/100+linearPoint2.getX(),
                    (linearPoint3.getY()-linearPoint2.getY())*t/100+linearPoint2.getY());

            //here we calculate the point that actually belongs to the spline segment
            Vector2d cubicPoint = new Vector2d(
                    quadraticPoint2.getX()-quadraticPoint1.getX()*t/100+ quadraticPoint1.getX(),
                    quadraticPoint2.getY()-quadraticPoint1.getY()*t/100+ quadraticPoint1.getY());

            //if (!xPointList.isEmpty()){
                //currentDistanceTraveled += Math.hypot(cubicPoint.getX()-xPointList.get(xPointList.size()-1),cubicPoint.getY()-yPointList.get(yPointList.size()-1));
                //for use with the

                //currentCouseTime += currentDistanceTraveled / AutonomousConstants.MAXSPEED*0.6; //guarantees that the course finishes even if the robot gets stuck
            //}

            xPointList.add(cubicPoint.getX());
            yPointList.add(cubicPoint.getY());
            //DistanceTraveledAtIndexList.add(currentDistanceTraveled);
            //timeAtIndexList.add(currentCouseTime);

        }
    }

    public void updateCurrentIndex(Pose2d currentBotPosition, RobotMovementState currentBotState){
        double distanceToNextIndex=0;
        double distanceToThisIndex=0;

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
            if (CurrentIndex+1>yPointList.size()){break;}
            CurrentIndex++;
        }

    }
    public int getSegmentID(){
        return (int) Math.round(((double) vData.index-50)/100);
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
        double a = Math.PI;
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
