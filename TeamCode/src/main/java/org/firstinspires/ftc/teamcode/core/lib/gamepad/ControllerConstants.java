package org.firstinspires.ftc.teamcode.core.lib.gamepad;

/**
 * Centralized threshold values used by {@link SmartGamepad} to determine when analog controller
 * inputs should be considered active.
 */
public class ControllerConstants {

  /** Minimum absolute joystick axis value required for a stick movement to be considered active. */
  public static final double STICK_PRESSED_THRESHOLD_VALUE = 0.1;

  /** Minimum trigger position required for a trigger input to be considered pressed. */
  public static final double TRIGGER_PRESSED_THRESHOLD_VALUE = 0.9;
}
