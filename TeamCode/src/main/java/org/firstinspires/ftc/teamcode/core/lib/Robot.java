package org.firstinspires.ftc.teamcode.core.lib;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadConfig;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartController;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;

import java.util.ArrayList;


public class Robot {
    private ArrayList<Subsystem> subsystems;
    private SmartController driver;
    private SmartController operator;
    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    private GamepadConfig gamepadConfig;
    public Robot() {
    }

    public void gamepadConfig(@NonNull Gamepad driver, Gamepad operator) {
        gamepadConfig = GamepadConfig.use(driver, operator);
    }

    public void gamepadConfig(@NonNull Gamepad driver){
        gamepadConfig = GamepadConfig.use(driver);
    }

    public void init(@NonNull HardwareMap hardwareMap, @NonNull Telemetry telemetry, @NonNull ArrayList<Subsystem> subsystems) {
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
        subsystems.forEach(subsystem -> subsystem.execute(gamepadConfig));
        telemetry.addData("Driver", driver.getRightStickX());
        telemetry.addData("Operator", operator.getRightStickX());
        telemetry.update();
    }

    public void stop() {
        subsystems.forEach(Subsystem::stop);
        telemetry.update();
    }



}
