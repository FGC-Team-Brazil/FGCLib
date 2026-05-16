package org.firstinspires.ftc.teamcode.robot;

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

    public RobotContainer() {}

    @Override
    public void configureBindings() {
        driver.leftY(
                Constants.Globals.Controller.CONTROLLER_DEADBAND)
              .or(driver.rightX(Constants.Globals.Controller.CONTROLLER_DEADBAND))
              .whileActive(
                      () -> drivetrain.arcadeDrive(-driver.getLeftY(), driver.getRightX())
              );
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
