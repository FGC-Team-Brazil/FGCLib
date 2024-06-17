package org.firstinspires.ftc.teamcode.core.lib.interfaces;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartController;

/**
 * md
 * Standard interface used to build subsystems.
 * Contains 3 essential methods:
 * Initialize: instantiates the subsystem hardware;
 * Execute: runs all the commands executed by the subsystem;
 * Stop: deactivates the subsystem hardware.
 */
public interface Subsystem {
    void initialize(HardwareMap hardwareMap, Telemetry telemetry);
    void start();
    void stop();
    void execute(SmartController driver, SmartController operator);

}
