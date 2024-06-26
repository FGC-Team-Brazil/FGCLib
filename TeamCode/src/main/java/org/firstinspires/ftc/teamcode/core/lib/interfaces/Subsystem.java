package org.firstinspires.ftc.teamcode.core.lib.interfaces;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadManager;

/**
 * md
 * Standard interface used to build subsystems.
 * Contains 3 essential methods:
 * Initialize: instantiates the subsystem hardware;
 * Execute: runs all the commands executed by the subsystem;
 * Stop: deactivates the subsystem hardware.
 */
public interface Subsystem {
    /**
     * Initialize: instantiates the subsystem hardware here;
     * @param hardwareMap
     * @param telemetry
     */
    void initialize(HardwareMap hardwareMap, Telemetry telemetry);

    /**
     * Start method is supposed to run when the start button is pressed.
     */
    void start();

    /**
     * Stop method is supposed to run when the stop button is pressed.
     * Stop all the subsystems here.
     */
    void stop();

    /**
     * execute is supposed to run when the start button is pressed.
     * It loops till the end of the opMode.
     * @param gamepadManager
     */
    void execute(GamepadManager gamepadManager);

}
