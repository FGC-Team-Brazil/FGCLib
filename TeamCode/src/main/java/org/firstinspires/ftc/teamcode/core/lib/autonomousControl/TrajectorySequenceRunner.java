package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;

public class TrajectorySequenceRunner {
    /*
    this will run the lists of trajectory courses and external actions that
    any sequence possesses in a callable loop.

    the commands are triggered by a change in the current trajectory course segment id followed by additional logic to wait for the robot to travel a number of centimeter,
    wait some time or just execute the lambda associated to it
     */
    TrajectorySequence sequence;
    int currentStructureID=0;
    int currentStructureSegmentID=0;
    boolean structureStarted=false;
    RobotMovementState movementState = new RobotMovementState(0,0);
    boolean structureDone = false;
    public void setSequence(TrajectorySequence newSequence){
        sequence=newSequence;
    }
    public void execute(double currentTime,double previousTime) {

        if (!structureStarted) {
            sequence.TrajectoryStructureList.get(currentStructureID).start(0);
        } else {
            structureDone = sequence.TrajectoryStructureList.get(currentStructureID).execute(
                    DrivetrainBuilder.getInstance().getCurrentPose(),
                    movementState.update(
                            DrivetrainBuilder.getInstance().movementState.AX,
                            DrivetrainBuilder.getInstance().movementState.AY,
                            currentTime-previousTime)
                            ,currentTime-previousTime);
            currentStructureSegmentID = sequence.TrajectoryStructureList.get(currentStructureID).getSegmentID();
        }

        if (structureDone){
            currentStructureID++;
            currentStructureSegmentID =0;
        }

            for (AutonomousCommand autonomousCommand : sequence.CommandList) {
                boolean conditionMet = autonomousCommand.runConditionMet(currentStructureID, currentStructureSegmentID);
                if (conditionMet) {
                    autonomousCommand.executeCommand();
                    sequence.CommandList.remove(autonomousCommand);
                }
            }

        //the thing that controls robot position after ending does'nt exist and i don't think it needs to because deacceleration is done at the end of each Structure
    }
}
