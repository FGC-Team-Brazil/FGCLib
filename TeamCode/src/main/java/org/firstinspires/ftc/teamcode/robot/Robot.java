package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartController;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;

import java.util.ArrayList;


public class Robot {
    private ArrayList<Subsystem> subsystems;
    private SmartController driver;
    private SmartController operator;
    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    public Robot() {
    }

    public void init(HardwareMap hardwareMap, Telemetry telemetry, Gamepad driver, Gamepad operator, ArrayList<Subsystem> subsystems) {
        this.driver = new SmartController(driver);
        this.operator = new SmartController(operator);
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.subsystems = subsystems;

        subsystems.forEach(subsystem -> subsystem.initialize(hardwareMap, telemetry));

        telemetry.update();
    }

    public void start() {
        subsystems.forEach(Subsystem::start);
        telemetry.update();
    }

    public void loop() {
        subsystems.forEach(subsystem -> subsystem.execute(driver, operator));
        telemetry.update();
    }

    public void stop() {
        subsystems.forEach(Subsystem::stop);
        telemetry.update();
    }



}
