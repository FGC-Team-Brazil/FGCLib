package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

public class BasicCommand {
    public boolean isConditionMetYet(){
        return true;
    }
    public void executeCommand(Pose2d botPosition, RobotMovementState currentBotState) {
        runCommand(personalCommand);
    }




    public enum CommandType{
        BASIC,
        TIME_DELAY,
        MOVEMENT_DELAY
    }
    CommandType commandType = CommandType.BASIC;
    Runnable personalCommand;
    BasicCommand (Runnable runnable){
        personalCommand = runnable;
    }
    BasicCommand (CommandType type, Runnable runnable){
        commandType = type;
        personalCommand = runnable;
    }

    public void runCommand(Runnable runnable){
        personalCommand.run();
    }
}
