package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

public class TrajectorySequenceBuilder {
    /*
    this will add the trajectory sequences and external action triggers to the
    actual trajectory sequence being created
     */

    //public TrajectoryCourseBuilder startCourse(Pose2d start,Pose2d end)
    /*
    creates a new trajectory course builder and returns it in order to add segments to it, commands or to finish the process of building it
     */

    //public void stopSeconds()
    /*
    adds to the sequence some time that the robot spends stationary at the end of a trajectory course and stops the robot for the said amount of time
     */

    //public TrajectoryCourseBuilder addMidCourseCommand(command)
    /*
    this will probably be broken up in time and position commands, but the purpose will be to make the robot do stuff while it is still moving in autonomous

    how these comands will work :
    first a command is assigned to a n*100 index on the Trajectory course itself, once the pure pursuit target reaches that point the Trajectory Sequence Runner knows it (because
    the trajectory sequence runner will say it) and will wait until the robot travels certain cms or waits a certain number of seconds (according to which type od command it is)
    and then runs the assigned lambda to the command, moving the arm or whatever the command does.

    there will also be a basic type of command that just executes the lambda as soon as its assigned id is reached because that is very useful to have

     */


}
