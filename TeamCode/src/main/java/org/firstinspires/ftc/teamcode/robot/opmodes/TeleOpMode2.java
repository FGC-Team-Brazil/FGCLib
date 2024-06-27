package org.firstinspires.ftc.teamcode.robot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.lib.Robot;

/**
 * The opmodes folder is where all the opmodes
 * will be written. Just the main TeleOp mode is
 * outside this folder
 * <br><br>
 * This class is an example of opmode that we recommend you to use
 * as template
 */
@TeleOp(name = "TeleOp 2", group = "TeleOp 2")
public class TeleOpMode2 extends OpMode {
    private final Robot robot = new Robot();

    @Override
    public void init() {
        robot.configGamepadManager(gamepad1, gamepad2);
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