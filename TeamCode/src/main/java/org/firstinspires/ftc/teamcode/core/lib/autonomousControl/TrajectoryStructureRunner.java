package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
@Deprecated
public class TrajectoryStructureRunner {
    /*

    public int getCurrentSegmentID(){
        return currentStructure.getSegmentID();
    }
    /*
    this returns the numerical id of the current segment and is used by the trajectory sequence runner to know when to do certain commands
     *//*
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

    public boolean execute(){

        return currentStructure.execute(DrivetrainBuilder.getInstance().getCurrentPose(),
                DrivetrainBuilder.getInstance().movementState,
                ); //returns wether or not the drivetrain is busy with the structure

    }
    //gatos não são mais os soberanos, mas nós humanos estamos aqui e
    //as coisas estão dando mais ou menos certo conosco
    */
}
