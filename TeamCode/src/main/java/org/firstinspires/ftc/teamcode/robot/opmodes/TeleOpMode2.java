package org.firstinspires.ftc.teamcode.robot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartController;

import java.util.ArrayList;

@TeleOp(name = "TeleOp 2", group = "TeleOp 2")
public class TeleOpMode2 extends OpMode {

    private ArrayList<Subsystem> subsystemsDriver;
    private SmartController driver;

    @Override
    public void init() {
        this.driver = new SmartController(gamepad1);

        this.subsystemsDriver = new ArrayList<Subsystem>();

        this.subsystemsDriver.add(Drivetrain.getInstance());

        subsystemsDriver.forEach(subsystem -> subsystem.initialize(hardwareMap, telemetry));
        telemetry.update();
    }

    @Override
    public void start() {
        subsystemsDriver.forEach(Subsystem::start);
        telemetry.update();
    }

    @Override
    public void loop() {
        subsystemsDriver.forEach(subsystem -> subsystem.execute(driver, null));
        telemetry.update();
    }

    @Override
    public void stop() {
        subsystemsDriver.forEach(Subsystem::stop);
        telemetry.update();
    }

}