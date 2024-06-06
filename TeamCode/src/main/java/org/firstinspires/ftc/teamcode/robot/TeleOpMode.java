package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.core.util.SmartController;
import org.firstinspires.ftc.teamcode.robot.subsystems.SubsystemExample;

import java.util.ArrayList;

@TeleOp(name = "TeleOp", group = "Official TeleOp")
public class TeleOpMode extends OpMode {

    private ArrayList<Subsystem> subsystemsDriver;
    private ArrayList<Subsystem> subsystemsOperator;
    private SmartController driver;
    private SmartController operator;

    @Override
    public void init() {
        this.driver = new SmartController(gamepad1);
        this.operator = new SmartController(gamepad2);

        this.subsystemsDriver = new ArrayList<Subsystem>();
        this.subsystemsOperator = new ArrayList<Subsystem>();

        this.subsystemsDriver.add(Drivetrain.getInstance());
        this.subsystemsOperator.add(SubsystemExample.getInstance());

        subsystemsDriver.forEach(subsystem -> subsystem.initialize(hardwareMap, telemetry));
        subsystemsOperator.forEach(subsystem -> subsystem.initialize(hardwareMap, telemetry));
        telemetry.update();
    }

    @Override
    public void start() {
        subsystemsDriver.forEach(Subsystem::start);
        subsystemsOperator.forEach(Subsystem::start);
        telemetry.update();
    }

    @Override
    public void loop() {
        subsystemsDriver.forEach(subsystem -> subsystem.execute(driver));
        subsystemsOperator.forEach(subsystem -> subsystem.execute(operator));
        telemetry.update();
    }

    @Override
    public void stop() {
        subsystemsDriver.forEach(Subsystem::stop);
        subsystemsOperator.forEach(Subsystem::stop);
        telemetry.update();
    }

}
