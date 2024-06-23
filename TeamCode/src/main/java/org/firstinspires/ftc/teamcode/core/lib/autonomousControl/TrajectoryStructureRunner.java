package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;

public class TrajectoryStructureRunner {

    //public int getCurrentSegmentID()
    /*
    this returns the numerical id of the current segment and is used by the trajectory sequence runner to know when to do certain commands
     */
    private int LastIndex =0;
    private int CurrentIndex=0;
    int currentSegmentId;
    TrajectoryStructure currentStructure;
    TrajectoryStructure.StructureType currentStructureType = null;

    public void setCurrentStructure(TrajectoryStructure newStructure){
        currentStructure =newStructure;
        currentStructureType=currentStructure.getType();
    }

    public void startStructure(double startTime){
        currentStructure.start(startTime);
    }

    public boolean execute(Pose2d botPosition,RobotMovementState currentBotState){
        return currentStructure.execute(botPosition,currentBotState); //returns wether or not the drivetrain is busy with the structure
    }
    //gatos não são mais os soberanos, mas nós humanos estamos aqui e
    //as coisas estão dando mais ou menos certo conosco
}
