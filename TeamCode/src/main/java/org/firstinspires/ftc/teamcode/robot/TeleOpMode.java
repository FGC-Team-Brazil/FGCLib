package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * TeleOp class template for building TeleOp modes. Look at the example to build your own TeleOps.
 * This class will be your main TeleOp mode. Put other OpModes in the opmodes folder.
 */
@TeleOp(name = "TeleOp", group = "Official TeleOp")
public class TeleOpMode extends OpMode {
  private RobotContainer robot;

  @Override
  public void init() {
    robot = new RobotContainer(gamepad1, gamepad2);
    robot.init(hardwareMap, telemetry);
  }

  @Override
  public void start() {
    robot.start();
  }

  @Override
  public void loop() {
    robot.loop();
  }

  @Override
  public void stop() {
    robot.stop();
  }
}
