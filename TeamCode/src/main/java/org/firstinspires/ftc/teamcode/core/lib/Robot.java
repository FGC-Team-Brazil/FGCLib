package org.firstinspires.ftc.teamcode.core.lib;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadTriggerController;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

import java.util.List;

/**
 * Robot class is the main class of the FGCLib. It executes
 * all the subsystems created on code, and runs their main methods
 * from Subsystem Interface.
 * <br>
 * This class is used in the TeleOp mainly.
 */
public class Robot {
    private List<Subsystem> subsystems;
    private Telemetry telemetry;

    private final RobotContainer container;

    public Robot(Gamepad driver, Gamepad operator) {
        container = new RobotContainer(new GamepadTriggerController(new SmartGamepad(driver)), new GamepadTriggerController(new SmartGamepad(operator)));
    }

    /**
     * Run the init method from all subsystems
     * @param hardwareMap
     * @param telemetry
     */
    public void init(@NonNull HardwareMap hardwareMap, @NonNull Telemetry telemetry) {
        this.telemetry = telemetry;
        this.subsystems = container.get();

        subsystems.forEach(subsystem -> subsystem.initialize(hardwareMap, telemetry));

        telemetry.update();
    }

    /**
     * Run the start method from all subsystems
     */
    public void start() {
        subsystems.forEach(Subsystem::start);
        telemetry.update();
    }

    /**
     * Run the loop method from all subsystems
     */
    public void loop() {
        subsystems.forEach(Subsystem::execute);
        telemetry.update();
        container.update();
    }

    /**
     * Run the stop method from all subsystems
     */
    public void stop() {
        subsystems.forEach(Subsystem::stop);
        telemetry.update();
    }
}
