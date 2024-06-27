package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.lib.Robot;

/**
 * TeleOp class template for building TeleOp modes.
 * Look at the example to build your own TeleOps.
 * This class will be your main TeleOp mode.
 * Put other OpModes in the opmodes folder.
 */
@TeleOp(name = "TeleOp", group = "Official TeleOp")
public class TeleOpMode extends OpMode {
    private final Robot robot = new Robot();

    /**
     * Runs the initialize method of all subsystems
     */
    @Override
    public void init() {
        robot.configGamepadManager(gamepad1, gamepad2);
        robot.init(hardwareMap, telemetry);
    }

    /**
     * Runs the start method of all subsystems
     */
    @Override
    public void start() {
        robot.start();
    }

    /**
     * Runs the execute method of all subsystems
     */
    @Override
    public void loop() {
        robot.loop();
    }

    /**
     * Runs the stop method of all subsystems
     */
    @Override
    public void stop() {
        robot.stop();
    }
}
