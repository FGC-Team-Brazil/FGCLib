package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.lib.Robot;

import Ori.Coval.Logging.AutoLogManager;
import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp(name = "TeleOp", group = "Official TeleOp")
public class TeleOpMode extends OpMode {
    private final Robot robot = new Robot();

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        robot.configGamepadManager(gamepad1, gamepad2);
        robot.init(hardwareMap, telemetry);
        KoalaLog.setup(hardwareMap);
    }

    @Override
    public void start() {
        robot.start();
        KoalaLog.start();
    }

    @Override
    public void loop() {
        robot.loop();
        AutoLogManager.periodic();
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.stop();
        KoalaLog.stop();
    }
}