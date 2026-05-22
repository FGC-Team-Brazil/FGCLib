package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadController;
import org.firstinspires.ftc.teamcode.core.util.RobotContainerInternal;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.robot.subsystems.SubsystemExample;

import java.util.Arrays;

/**
 * RobotContainer class handle instance configurations.
 * All the subsystems listed in constructor here will be execute when the library classes run.
 */
public class RobotContainer extends RobotContainerInternal {

    private final Drivetrain drivetrain;
    private final SubsystemExample subsystemExample;

    public RobotContainer(Gamepad driver, Gamepad operator) {
        super(driver, operator,
                Drivetrain.getInstance(),
                SubsystemExample.getInstance()
                // Add more subsystems here.
        );

        drivetrain = Drivetrain.getInstance();
        subsystemExample = SubsystemExample.getInstance();
        // You need to add the subsystems here too.
    }

    @Override
    public void configureBindings() {
        GamepadController driver = getDriver();
        GamepadController operator = getOperator();

        // Driver controller
        driver.leftY()
              .or(driver.rightX())
              .whileActive(
                      () -> drivetrain.arcadeDrive(-driver.getLeftY(), driver.getRightX())
              );

        // Operator controller
        operator.y()
                .whenActive(() -> subsystemExample.setTargetAngle(90));

        operator.a()
                .whenActive(() -> subsystemExample.setTargetAngle(0));

        operator.on(subsystemExample::isLimitLeft)
                .or(operator.on(subsystemExample::isLimitRight))
                .or(operator.start().and(operator.back()))
                .whenActive(subsystemExample::resetEncoders);

        operator.y()
                .negate()
                .and(operator.a().negate())
                .whenActive(() -> subsystemExample.setPower(0));
    }
}
