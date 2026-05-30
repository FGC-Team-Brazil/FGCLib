package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Provides access to the gamepad Human Interface Device (HID) features such as controller rumble
 * and LED feedback.
 *
 * <p>This class allows robot code to communicate status information to drivers through haptic
 * feedback and controller lighting, improving driver awareness without requiring visual telemetry.
 */
public class HIDController {

  /** Predefined LED colors that can be applied to the controller. */
  public enum LedColor {
    RED(1.0, 0.0, 0.0),

    GREEN(0.0, 1.0, 0.0),

    BLUE(0.0, 0.0, 1.0),

    WHITE(1.0, 1.0, 1.0),

    YELLOW(1.0, 1.0, 0.0),

    CYAN(0.0, 1.0, 1.0),

    MAGENTA(1.0, 0.0, 1.0),

    ORANGE(1.0, 0.5, 0.0),

    PURPLE(0.5, 0.0, 0.5),

    OFF(0.0, 0.0, 0.0);

    final double r;
    final double g;
    final double b;

    LedColor(double r, double g, double b) {
      this.r = r;
      this.g = g;
      this.b = b;
    }
  }

  private final Gamepad gamepad;

  /**
   * Creates a HID controller wrapper for a FTC gamepad.
   *
   * @param gamepad FTC gamepad instance used to control rumble and LED features
   */
  HIDController(Gamepad gamepad) {
    this.gamepad = gamepad;
  }

  /**
   * Activates vibration on both rumble motors for a fixed duration.
   *
   * <p>Useful for notifying drivers about events such as scoring, reaching a target position, or
   * low battery warnings.
   *
   * @param durationMs vibration duration in milliseconds
   */
  public void rumble(int durationMs) {
    gamepad.rumble(durationMs);
  }

  /**
   * Activates vibration with independent intensity for each rumble motor.
   *
   * @param motor1 intensity of the first rumble motor in the range {@code [0, 1]}
   * @param motor2 intensity of the second rumble motor in the range {@code [0, 1]}
   * @param durationMs vibration duration in milliseconds
   */
  public void rumble(double motor1, double motor2, int durationMs) {
    gamepad.rumble(motor1, motor2, durationMs);
  }

  /** Starts continuous vibration until {@link #rumbleStop()} is called. */
  public void rumbleContinuous() {
    gamepad.rumble(1.0, 1.0, Gamepad.RUMBLE_DURATION_CONTINUOUS);
  }

  /**
   * Plays a predefined sequence of rumble pulses.
   *
   * @param count number of vibration pulses
   */
  public void rumbleBlips(int count) {
    gamepad.rumbleBlips(count);
  }

  /** Stops any active rumble effect running on the controller. */
  public void rumbleStop() {
    gamepad.stopRumble();
  }

  /**
   * Indicates whether the controller is currently vibrating.
   *
   * @return {@code true} if a rumble effect is active
   */
  public boolean isRumbling() {
    return gamepad.isRumbling();
  }

  /**
   * Executes a custom rumble pattern.
   *
   * @param effect rumble effect created using {@link Gamepad.RumbleEffect}
   */
  public void runRumbleEffect(Gamepad.RumbleEffect effect) {
    gamepad.runRumbleEffect(effect);
  }

  /**
   * Sets the controller LED to a predefined color continuously.
   *
   * @param color LED color to display
   */
  public void setLed(LedColor color) {
    gamepad.setLedColor(color.r, color.g, color.b, Gamepad.LED_DURATION_CONTINUOUS);
  }

  /**
   * Sets the controller LED to a predefined color for a fixed duration.
   *
   * @param color LED color to display
   * @param durationMs duration in milliseconds
   */
  public void setLed(LedColor color, int durationMs) {
    gamepad.setLedColor(color.r, color.g, color.b, durationMs);
  }

  /**
   * Sets the controller LED using custom RGB values continuously.
   *
   * @param r red intensity in the range {@code [0, 1]}
   * @param g green intensity in the range {@code [0, 1]}
   * @param b blue intensity in the range {@code [0, 1]}
   */
  public void setLed(double r, double g, double b) {
    gamepad.setLedColor(r, g, b, Gamepad.LED_DURATION_CONTINUOUS);
  }

  /**
   * Sets the controller LED using custom RGB values for a fixed duration.
   *
   * @param r red intensity in the range {@code [0, 1]}
   * @param g green intensity in the range {@code [0, 1]}
   * @param b blue intensity in the range {@code [0, 1]}
   * @param durationMs duration in milliseconds
   */
  public void setLed(double r, double g, double b, int durationMs) {
    gamepad.setLedColor(r, g, b, durationMs);
  }

  /**
   * Executes a custom LED animation pattern.
   *
   * @param effect LED effect created using {@link Gamepad.LedEffect}
   */
  public void runLedEffect(Gamepad.LedEffect effect) {
    gamepad.runLedEffect(effect);
  }
}
