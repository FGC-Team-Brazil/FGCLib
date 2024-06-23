package org.firstinspires.ftc.teamcode.core.lib;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.RobotSubsystems;

import java.util.ArrayList;
import java.util.List;


public class Robot {
    private List<Subsystem> subsystems;
    private Telemetry telemetry;
    private GamepadManager gamepadManager;

    public Robot() {
    }

    public void configGamepadManager(@NonNull Gamepad driver, @NonNull Gamepad operator) {
        gamepadManager = GamepadManager.use(driver, operator);
    }

    public void configGamepadManager(@NonNull Gamepad driver) {
        gamepadManager = GamepadManager.use(driver);
    }

    public void init(@NonNull HardwareMap hardwareMap, @NonNull Telemetry telemetry) {
        this.telemetry = telemetry;
        this.subsystems = RobotSubsystems.get();

        subsystems.forEach(subsystem -> subsystem.initialize(hardwareMap, telemetry));

        telemetry.update();
    }

    public void start() {
        subsystems.forEach(Subsystem::start);
        telemetry.update();
    }

    public void loop() {
        subsystems.forEach(subsystem -> subsystem.execute(gamepadManager));
        telemetry.update();
    }

    public void stop() {
        subsystems.forEach(Subsystem::stop);
        telemetry.update();
    }


}
