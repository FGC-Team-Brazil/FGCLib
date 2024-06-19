package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.lib.Robot;
import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.constants.DrivetrainBuilderConstants;

import java.util.ArrayList;
import java.util.Arrays;

@TeleOp(name = "TeleOp", group = "Official TeleOp")
public class TeleOpMode extends OpMode {
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
