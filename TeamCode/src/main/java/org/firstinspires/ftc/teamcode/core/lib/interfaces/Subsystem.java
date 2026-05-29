package org.firstinspires.ftc.teamcode.core.lib.interfaces;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Standard interface used to build subsystems.
 *
 * <p>Contains 3 essential methods: Initialize: instantiates the subsystem hardware; Execute: runs
 * all the commands executed by the subsystem; Stop: deactivates the subsystem hardware.
 */
public interface Subsystem {
  /**
   * Initialize: instantiates the subsystem hardware here;
   *
   * @param hardwareMap
   */
  void initialize(HardwareMap hardwareMap);

  /** Start method is supposed to run when the start button is pressed. */
  void start();

  /**
   * Stop method is supposed to run when the stop button is pressed. Stop all the subsystems here.
   */
  void stop();

  /** execute is supposed to run when the start button is pressed. */
  void execute();
}
