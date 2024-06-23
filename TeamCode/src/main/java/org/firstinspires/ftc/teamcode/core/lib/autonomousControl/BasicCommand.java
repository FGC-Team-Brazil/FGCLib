package org.firstinspires.ftc.teamcode.core.lib.autonomousControl;

public class BasicCommand {
    @Override
    public StructureType getType() {
        return null;
    }

    @Override
    public void start(double startTime) {

    }

    @Override
    public boolean execute(Pose2d botPosition, RobotMovementState currentBotState) {
        runCommand(personalCommand);
        return true;
    }

    @Override
    public Pose2d getLastPose2d() {
        return null;
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
