package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Main TeleOp entry point for the robot.
 *
 * <p>This OpMode is responsible for creating and managing the {@link RobotContainer}, which handles
 * subsystem initialization, controller bindings, and the execution lifecycle of the robot.
 *
 * <p>The FTC SDK lifecycle methods are delegated directly to the {@code RobotContainer}, keeping
 * the OpMode clean and focused on framework integration.
 */
@TeleOp(name = "TeleOp", group = "Official TeleOp")
public class TeleOpMode extends OpMode {

  /** Central robot container responsible for subsystem and control management. */
  private RobotContainer robot;

  /**
   * Initializes the robot and all registered subsystems.
   *
   * <p>This method creates the {@link RobotContainer}, provides access to the FTC {@code
   * hardwareMap} and {@code telemetry}, and prepares the robot for operation.
   */
  @Override
  public void init() {
    robot = new RobotContainer(gamepad1, gamepad2);
    robot.init(hardwareMap, telemetry);
  }

  /**
   * Starts the robot lifecycle.
   *
   * <p>Called once when the driver presses the start button. This triggers the start routine for
   * all registered subsystems and configures control bindings.
   */
  @Override
  public void start() {
    robot.start();
  }

  /**
   * Executes the robot periodic loop.
   *
   * <p>Called repeatedly while the OpMode is running. This updates triggers, executes subsystem
   * logic, processes logging, and updates telemetry.
   */
  @Override
  public void loop() {
    robot.loop();
  }

  /**
   * Stops the robot and all registered subsystems.
   *
   * <p>Called once when the OpMode ends to ensure motors and actuators are safely disabled.
   */
  @Override
  public void stop() {
    robot.stop();
  }
}
