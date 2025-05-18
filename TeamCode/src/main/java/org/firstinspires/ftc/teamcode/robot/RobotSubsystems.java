package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.constants.DrivetrainBuilderConstants;

import java.util.Arrays;
import java.util.List;

/**
 * RobotSubsystems class lists all the existing subsystems.
 * All the subsystems listed here will be execute when the Robot class run.
 * Put your subsystems here always after you create a new one
 */
public class RobotSubsystems {
    private static final Subsystem[] subsystems = {
            DrivetrainBuilder.build(
                    DrivetrainBuilderConstants.MOTOR_RIGHT,
                    DrivetrainBuilderConstants.MOTOR_LEFT,
                    DrivetrainBuilderConstants.MOTOR_RIGHT_INVERTED,
                    DrivetrainBuilderConstants.MOTOR_LEFT_INVERTED
            ),
            // Add more subsystems here
    };

    /**
     * Get all the subsystem instances
     * <p>puts all subsystems in a neat little array to be used by
     * the Robot class to run everything in an organized manner</p>
     * @return list with all subsystems
     */
    public static List<Subsystem> get() {
        return Arrays.asList(subsystems);
    }
}
