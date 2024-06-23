package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;


import java.util.ArrayList;

public class TrajectorySequence {
    /*
    trajectory sequences are what unites multiple trajectory courses and external commands into
    a single object executable with a loop. This is yet to be implemented, as it will end up using lambda
    functions and position / time based triggers to start executing them when the robot reaches the desired position
    and I have no clue how to do this (me ajudem pfv kkk)

    when making a trajectory sequence, one actually calls the trajectory sequence builder
     */

    ArrayList<TrajectoryStructure> TrajectoryStructureList = new ArrayList<>();

    //ArrayList<AutonomousCommand> CommandList = new ArrayList<AutonomousCommand>();

    /*
    I do not know how this could be done in java exactly, but each of these commands must have the following values:

    trajectory course id
    trajectory course segment id
    timeToWait or cmToWait (depending if it is a time based command or a centimeter based one)
    lambda with action to do when the command is triggered
     */


}
