package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

public class HIDController {

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

  HIDController(Gamepad gamepad) {
    this.gamepad = gamepad;
  }

  public void rumble(int durationMs) {
    gamepad.rumble(durationMs);
  }

  public void rumble(double motor1, double motor2, int durationMs) {
    gamepad.rumble(motor1, motor2, durationMs);
  }

  public void rumbleContinuous() {
    gamepad.rumble(1.0, 1.0, Gamepad.RUMBLE_DURATION_CONTINUOUS);
  }

  public void rumbleBlips(int count) {
    gamepad.rumbleBlips(count);
  }

  public void rumbleStop() {
    gamepad.stopRumble();
  }

  public boolean isRumbling() {
    return gamepad.isRumbling();
  }

  public void runRumbleEffect(Gamepad.RumbleEffect effect) {
    gamepad.runRumbleEffect(effect);
  }

  public void setLed(LedColor color) {
    gamepad.setLedColor(color.r, color.g, color.b, Gamepad.LED_DURATION_CONTINUOUS);
  }

  public void setLed(LedColor color, int durationMs) {
    gamepad.setLedColor(color.r, color.g, color.b, durationMs);
  }

  public void setLed(double r, double g, double b) {
    gamepad.setLedColor(r, g, b, Gamepad.LED_DURATION_CONTINUOUS);
  }

  public void setLed(double r, double g, double b, int durationMs) {
    gamepad.setLedColor(r, g, b, durationMs);
  }

  public void runLedEffect(Gamepad.LedEffect effect) {
    gamepad.runLedEffect(effect);
  }
}
