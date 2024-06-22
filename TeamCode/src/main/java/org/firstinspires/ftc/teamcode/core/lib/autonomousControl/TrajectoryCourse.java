package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import java.util.ArrayList;

public class TrajectoryCourse {
    /*
    A trajectory course is the way in which our lib stores the path the robot takes between two stops,
    it contains a list of coordinates between these two stops given by a parametric bezier curve.

    The list contains only the current trajectory segment, this is done to reduce calculation times before the
    robot actually begins movement and spread it across the entire autonomous. This way we don't suffer from the same
    time to load problem faced by RoadRunner, which can take a couple seconds to start the movement.

    As a result this lib is more applicable to real time movement recalculation and webcam-using odometry
     */
    ArrayList<Pose2d> pose2dList = new ArrayList<Pose2d>();
    ArrayList<Double> xPointList = new ArrayList<Double>();
    ArrayList<Double> yPointList = new ArrayList<Double>();
    ArrayList<Double> timeAtPointList = new ArrayList<Double>();
    double currentCouseTime = 0;
    private final double bezierControlConstant = Math.E;
    TrajectoryCourse addPose2d(Pose2d newPose2d){
        pose2dList.add(newPose2d);
        return this;
    }

    TrajectoryCourse calculateSegment(Pose2d start, Pose2d end){
        int startT;
        double segmentLengh = Math.hypot(Math.pow(end.getX() - start.getX(),2)
                                        , Math.pow(end.getY() - start.getY(),2));
        Vector2d ControlPoint1 = new Vector2d(
                segmentLengh/bezierControlConstant*Math.cos(start.getHeadingRadians())+start.getX(),
                segmentLengh/bezierControlConstant*Math.sin(start.getHeadingRadians())+start.getY()
        );
        Vector2d ControlPoint2 = new Vector2d(
                segmentLengh/bezierControlConstant*Math.cos(end.getHeadingRadians())+end.getX(),
                segmentLengh/bezierControlConstant*Math.sin(end.getHeadingRadians())+end.getY()
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

            if (!xPointList.isEmpty()){
                currentCouseTime += Math.hypot(cubicPoint.getX()-xPointList.get(xPointList.size()-1),cubicPoint.getY()-yPointList.get(yPointList.size()-1));
            }
            //todo add distance traveled list for use in making distance traveled from point based commands as calculations are already done to make the time list
            xPointList.add(cubicPoint.getX());
            yPointList.add(cubicPoint.getY());
            timeAtPointList.add(currentCouseTime);

        }
        return this;
    }


}
