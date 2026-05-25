package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.core.util.RobotContainerInternal;
import org.firstinspires.ftc.teamcode.robot.subsystems.SubsystemExample;

/**
 * RobotContainer class handle instance configurations. All the subsystems listed in constructor
 * here will be execute when the library classes run.
 */
public class RobotContainer extends RobotContainerInternal {

  private final SmartGamepad driver;
  private final SmartGamepad operator;

  private final SubsystemExample subsystemExample;
  private final DrivetrainBuilder drivetrain;

  public RobotContainer(Gamepad driver, Gamepad operator) {
    super(
        DrivetrainBuilder.getInstance(), SubsystemExample.getInstance()
        // Add more subsystems here.
        );

    this.driver = new SmartGamepad(driver);
    this.operator = new SmartGamepad(operator);

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

    // Driver controller
    driver
        .leftY()
        .or(driver.rightX())
        .whileTrue(() -> drivetrain.arcadeDrive(-driver.getLeftY(), driver.getRightX()))
        .onFalse(drivetrain::stop);

    // Operator controller
    operator.y().onTrue(() -> subsystemExample.setTargetAngle(90));

    operator.a().onTrue(() -> subsystemExample.setTargetAngle(0));

    new Trigger(subsystemExample::isLimitLeft).onTrue(subsystemExample::resetEncoders);

    new Trigger(subsystemExample::isLimitRight).onTrue(subsystemExample::resetEncoders);

    operator.start().and(operator.back()).onTrue(subsystemExample::resetEncoders);

    operator.y().negate().and(operator.a().negate()).onTrue(() -> subsystemExample.setPower(0));
  }
}
