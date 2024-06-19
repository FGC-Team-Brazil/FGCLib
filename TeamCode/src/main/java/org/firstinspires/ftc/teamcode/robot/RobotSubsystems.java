package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.constants.DrivetrainBuilderConstants;

import java.util.Arrays;
import java.util.List;

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

    public static List<Subsystem> get() {
        return Arrays.asList(subsystems);
    }

    private RobotSubsystems() {
    }
}
