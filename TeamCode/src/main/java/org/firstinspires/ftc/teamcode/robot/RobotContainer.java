package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadTriggerController;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.util.RobotContainerInternal;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.robot.subsystems.SubsystemExample;

import java.util.Arrays;
import java.util.List;

/**
 * RobotSubsystems class lists all the existing subsystems.
 * All the subsystems listed here will be execute when the Robot class run.
 * Put your subsystems here always after you create a new one
 */
public class RobotContainer extends RobotContainerInternal {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final SubsystemExample subsystemExample = SubsystemExample.getInstance();

    // Add more subsystems here and to the array.
    public RobotContainer(GamepadTriggerController driver, GamepadTriggerController operator) {
        super(driver, operator);
    }

    @Override
    public void configureBindings() {

        // Driver controller
        GamepadTriggerController driver = getDriver();
        driver.leftY()
              .or(driver.rightX())
              .whileActive(
                      () -> drivetrain.arcadeDrive(-driver.getLeftY(), driver.getRightX())
              );

        // Operator controller
        GamepadTriggerController operator = getOperator();

        operator.y().whenActive(() -> subsystemExample.setTargetAngle(90));
        operator.a().whenActive(() -> subsystemExample.setTargetAngle(0));

        operator.on(subsystemExample::isLimitLeft)
                .or(operator.on(subsystemExample::isLimitRight))
                .or(operator.start().and(operator.back()))
                .whenActive(subsystemExample::resetEncoders);

        operator.y().negate().and(operator.a().negate())
                .whenActive(() -> subsystemExample.setPower(0));
    }

    /**
     * Get all the subsystem instances
     * <p>puts all subsystems in this little array to be used by
     * the Robot class to run everything in an organized manner</p>
     * @return list with all subsystems
     */
    public List<Subsystem> get() {
        // Add your subsystems here:
        return Arrays.asList(drivetrain, subsystemExample);
    }
}
