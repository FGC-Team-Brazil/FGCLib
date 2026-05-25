package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.function.BooleanSupplier;

public class SmartGamepad {

  private final Gamepad gamepad;
  private final HIDController hid;

  public SmartGamepad(Gamepad gamepad) {
    this.gamepad = gamepad;
    this.hid = new HIDController(gamepad);
  }

  public HIDController getHID() {
    return hid;
  }

  public Trigger on(BooleanSupplier condition) {
    return new Trigger(condition);
  }

  public Trigger a() {
    return on(() -> gamepad.a);
  }

  public Trigger b() {
    return on(() -> gamepad.b);
  }

  public Trigger x() {
    return on(() -> gamepad.x);
  }

  public Trigger y() {
    return on(() -> gamepad.y);
  }

  public Trigger dpadUp() {
    return on(() -> gamepad.dpad_up);
  }

  public Trigger dpadDown() {
    return on(() -> gamepad.dpad_down);
  }

  public Trigger dpadLeft() {
    return on(() -> gamepad.dpad_left);
  }

  public Trigger dpadRight() {
    return on(() -> gamepad.dpad_right);
  }

  public Trigger leftBumper() {
    return on(() -> gamepad.left_bumper);
  }

  public Trigger rightBumper() {
    return on(() -> gamepad.right_bumper);
  }

  public Trigger leftStick() {
    return on(() -> gamepad.left_stick_button);
  }

  public Trigger rightStick() {
    return on(() -> gamepad.right_stick_button);
  }

  public Trigger start() {
    return on(() -> gamepad.start);
  }

  public Trigger back() {
    return on(() -> gamepad.back);
  }

  public Trigger guide() {
    return on(() -> gamepad.guide);
  }

  public Trigger leftTrigger() {
    return on(() -> gamepad.left_trigger > ControllerConstants.TRIGGER_PRESSED_THRESHOLD_VALUE);
  }

  public Trigger leftTrigger(double threshold) {
    return on(() -> gamepad.left_trigger > threshold);
  }

  public Trigger rightTrigger() {
    return on(() -> gamepad.right_trigger > ControllerConstants.TRIGGER_PRESSED_THRESHOLD_VALUE);
  }

  public Trigger rightTrigger(double threshold) {
    return on(() -> gamepad.right_trigger > threshold);
  }

  public Trigger leftX() {
    return leftX(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  public Trigger leftX(double threshold) {
    return on(() -> Math.abs(gamepad.left_stick_x) > threshold);
  }

  public Trigger leftY() {
    return leftY(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  public Trigger leftY(double threshold) {
    return on(() -> Math.abs(gamepad.left_stick_y) > threshold);
  }

  public Trigger rightX() {
    return rightX(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  public Trigger rightX(double threshold) {
    return on(() -> Math.abs(gamepad.right_stick_x) > threshold);
  }

  public Trigger rightY() {
    return rightY(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  public Trigger rightY(double threshold) {
    return on(() -> Math.abs(gamepad.right_stick_y) > threshold);
  }

  public double getLeftX() {
    return gamepad.left_stick_x;
  }

  public double getLeftY() {
    return gamepad.left_stick_y;
  }

  public double getRightX() {
    return gamepad.right_stick_x;
  }

  public double getRightY() {
    return gamepad.right_stick_y;
  }

  public double getLeftTriggerAxis() {
    return gamepad.left_trigger;
  }

  public double getRightTriggerAxis() {
    return gamepad.right_trigger;
  }

  public Gamepad getGamepad() {
    return gamepad;
  }
}
