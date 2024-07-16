package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;


import java.util.ArrayList;

public class TrajectorySequence {
    /*
    trajectory sequences are what unites multiple trajectory courses and external commands into
    a single object executable with a loop.

    when making a trajectory sequence, one actually calls the trajectory sequence builder
     */

    ArrayList<TrajectoryStructure> TrajectoryStructureList = new ArrayList<>();
    ArrayList<AutonomousCommand> CommandList = new ArrayList<>();
}
