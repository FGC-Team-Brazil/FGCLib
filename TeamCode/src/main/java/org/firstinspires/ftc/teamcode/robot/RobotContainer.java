package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.util.RobotContainerInternal;
import org.firstinspires.ftc.teamcode.robot.subsystems.SubsystemExample;

/**
 * RobotContainer class handle instance configurations. All the subsystems listed in constructor
 * here will be execute when the library classes run.
 */
public class RobotContainer extends RobotContainerInternal {

  private final SubsystemExample subsystemExample;
  private final DrivetrainBuilder drivetrain;

  public RobotContainer(Gamepad driver, Gamepad operator) {
    super(
        driver, operator, DrivetrainBuilder.getInstance(), SubsystemExample.getInstance()
        // Add more subsystems here.
        );

    drivetrain =
        DrivetrainBuilder.build(
            Constants.DrivetrainBuilderConstants.MOTOR_RIGHT,
            Constants.DrivetrainBuilderConstants.MOTOR_LEFT,
            Constants.DrivetrainBuilderConstants.MOTOR_RIGHT_INVERTED,
            Constants.DrivetrainBuilderConstants.MOTOR_LEFT_INVERTED);
    subsystemExample = SubsystemExample.getInstance();
    // You need to add the subsystems here too.
  }

  @Override
  public void configureBindings() {
    SmartGamepad driver = getDriver();
    SmartGamepad operator = getOperator();

    // Driver controller
    driver
        .leftY()
        .or(driver.rightX())
        .whileTrue(() -> drivetrain.arcadeDrive(-driver.getLeftY(), driver.getRightX()));

    // Operator controller
    operator.y().onTrue(() -> subsystemExample.setTargetAngle(90));

    operator.a().onTrue(() -> subsystemExample.setTargetAngle(0));

    operator.on(subsystemExample::isLimitLeft).onTrue(subsystemExample::resetEncoders);

    operator.on(subsystemExample::isLimitRight).onTrue(subsystemExample::resetEncoders);

    operator.start().and(operator.back()).onTrue(subsystemExample::resetEncoders);

    operator.y().negate().and(operator.a().negate()).onTrue(() -> subsystemExample.setPower(0));
  }
}
