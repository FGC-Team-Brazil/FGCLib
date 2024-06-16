package org.firstinspires.ftc.teamcode.core.lib.interfaces;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartController;

/**
 * Subsystem is a utility interface used to build subsystems.
 * Always implements it on subsystems classes.
 * Contains 4 essential methods: initialize, start, execute and stop.
 * Each method is supposed to run when you press the init, start and stop buttons
 * on the ftc driversStation.
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
     * @param controller
     */
    void execute(SmartController controller);
}
