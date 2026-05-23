package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class GamepadController {
  private final List<TriggerBinding> bindings = new ArrayList<>();
  private final SmartGamepad smartGamepad;

  public GamepadController(SmartGamepad smartGamepad) {
    this.smartGamepad = smartGamepad;
  }

  public SmartGamepad getHID() {
    return smartGamepad;
  }

  public TriggerBinding on(BooleanSupplier condition) {
    var binding = new TriggerBinding(this, condition);
    bindings.add(binding);
    return binding;
  }

  public TriggerBinding a() {
    return on(smartGamepad::isButtonA);
  }

  public TriggerBinding b() {
    return on(smartGamepad::isButtonB);
  }

  public TriggerBinding x() {
    return on(smartGamepad::isButtonX);
  }

  public TriggerBinding y() {
    return on(smartGamepad::isButtonY);
  }

  public TriggerBinding dpadUp() {
    return on(smartGamepad::isButtonDPadUp);
  }

  public TriggerBinding dpadDown() {
    return on(smartGamepad::isButtonDPadDown);
  }

  public TriggerBinding dpadLeft() {
    return on(smartGamepad::isButtonDPadLeft);
  }

  public TriggerBinding dpadRight() {
    return on(smartGamepad::isButtonDPadRight);
  }

  public TriggerBinding leftBumper() {
    return on(smartGamepad::isButtonLeftBumper);
  }

  public TriggerBinding rightBumper() {
    return on(smartGamepad::isButtonRightBumper);
  }

  public TriggerBinding start() {
    return on(smartGamepad::isButtonStart);
  }

  public TriggerBinding back() {
    return on(smartGamepad::isButtonBack);
  }

  public TriggerBinding guide() {
    return on(smartGamepad::isButtonGuide);
  }

  public TriggerBinding leftStick() {
    return on(smartGamepad::isButtonLeftStickButton);
  }

  public TriggerBinding rightStick() {
    return on(smartGamepad::isButtonRightStickButton);
  }

  public TriggerBinding leftX() {
    return leftX(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  public TriggerBinding leftX(double threshold) {
    return on(() -> Math.abs(smartGamepad.getLeftStickX()) > threshold);
  }

  public TriggerBinding leftY() {
    return leftY(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  public TriggerBinding leftY(double threshold) {
    return on(() -> Math.abs(smartGamepad.getLeftStickY()) > threshold);
  }

  public TriggerBinding rightX() {
    return rightX(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  public TriggerBinding rightX(double threshold) {
    return on(() -> Math.abs(smartGamepad.getRightStickX()) > threshold);
  }

  public TriggerBinding rightY() {
    return rightY(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  public TriggerBinding rightY(double threshold) {
    return on(() -> Math.abs(smartGamepad.getRightStickY()) > threshold);
  }

  public TriggerBinding leftTrigger() {
    return on(smartGamepad::isLeftTriggerPressed);
  }

  public TriggerBinding leftTrigger(double threshold) {
    return on(() -> smartGamepad.getLeftTrigger() > threshold);
  }

  public TriggerBinding rightTrigger() {
    return on(smartGamepad::isRightTriggerPressed);
  }

  public TriggerBinding rightTrigger(double threshold) {
    return on(() -> smartGamepad.getRightTrigger() > threshold);
  }

  public double getLeftX() {
    return smartGamepad.getLeftStickX();
  }

  public double getLeftY() {
    return smartGamepad.getLeftStickY();
  }

  public double getRightX() {
    return smartGamepad.getRightStickX();
  }

  public double getRightY() {
    return smartGamepad.getRightStickY();
  }

  public double getLeftTriggerAxis() {
    return smartGamepad.getLeftTrigger();
  }

  public double getRightTriggerAxis() {
    return smartGamepad.getRightTrigger();
  }

  public void update() {
    for (var binding : bindings) {
      binding.poll();
    }
  }
}
