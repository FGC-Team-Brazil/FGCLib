package org.firstinspires.ftc.teamcode.core.lib.gamepad;

/**
 * Centralized threshold values used by {@link SmartGamepad} to determine
 * when analog controller inputs should be considered active.
 *
 * <p>These values help prevent unintended activations caused by joystick drift
 * and define the minimum trigger travel required to generate a {@link Trigger}.
 */
public class ControllerConstants {

  /**
   * Minimum absolute joystick axis value required for a stick movement to be
   * considered active.
   *
   * <p>Used to filter small variations caused by sensor noise or joystick
   * centering imperfections.
   */
  public static final double STICK_PRESSED_THRESHOLD_VALUE = 0.1;

  /**
   * Minimum trigger position required for a trigger input to be considered
   * pressed.
   *
   * <p>A value close to {@code 1.0} ensures the trigger must be intentionally
   * pressed before activating commands.
   */
  public static final double TRIGGER_PRESSED_THRESHOLD_VALUE = 0.9;
}