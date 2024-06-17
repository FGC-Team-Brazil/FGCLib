package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.lib.Robot;
import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadConfig;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;

import java.util.ArrayList;

@TeleOp(name = "TeleOp", group = "Official TeleOp")
public class TeleOpMode extends OpMode {
    private ArrayList<Subsystem> subsystems = new ArrayList<Subsystem>();
    private final Robot robot = new Robot();

    @Override
    public void init() {
        GamepadConfig gamepadConfig = GamepadConfig.use(gamepad1);

        DrivetrainBuilder drivetrainBuilder = DrivetrainBuilder.configure("drivetrain_motorRight", "drivetrain_motorLeft", false, true);

        this.subsystems.add(drivetrainBuilder);

        // Don't remove this line
        robot.init(hardwareMap, telemetry, gamepadConfig, subsystems);
    }

    @Override
    public void start() {
        // Don't remove this line
        robot.start();

    }

    @Override
    public void loop() {
        // Don't remove this line
        robot.loop();

    }

    @Override
    public void stop() {
        // Don't remove this line
        robot.stop();

    }

}
