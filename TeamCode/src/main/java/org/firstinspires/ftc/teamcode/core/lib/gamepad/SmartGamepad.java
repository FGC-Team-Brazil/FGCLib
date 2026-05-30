package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.function.BooleanSupplier;

/**
 * Wrapper around the FTC {@link Gamepad} that exposes each input as a {@link Trigger}.
 *
 * <p>This class is intended to make robot control bindings cleaner and more expressive by turning
 * buttons, triggers, and stick thresholds into reusable event conditions. It also provides direct
 * access to analog axes for drive and mechanism control.
 */
public class SmartGamepad {

  /** FTC gamepad instance being wrapped. */
  private final Gamepad gamepad;

  /** HID helper associated with this controller. */
  private final HIDController hid;

  /**
   * Creates a smart controller wrapper for the given FTC gamepad.
   *
   * @param gamepad FTC controller instance provided by the SDK
   */
  public SmartGamepad(Gamepad gamepad) {
    this.gamepad = gamepad;
    this.hid = new HIDController(gamepad);
  }

  /**
   * Returns the low-level HID controller wrapper.
   *
   * <p>This can be used when direct access to controller-specific behavior is needed beyond the
   * trigger-based API.
   *
   * @return the underlying HID controller wrapper
   */
  public HIDController getHID() {
    return hid;
  }

  /**
   * Creates a trigger from an arbitrary boolean condition.
   *
   * @param condition boolean condition to monitor
   * @return trigger bound to the provided condition
   */
  public Trigger on(BooleanSupplier condition) {
    return new Trigger(condition);
  }

  /**
   * Returns a trigger for the {@code A} button.
   *
   * @return trigger that becomes true while {@code A} is pressed
   */
  public Trigger a() {
    return on(() -> gamepad.a);
  }

  /**
   * Returns a trigger for the {@code B} button.
   *
   * @return trigger that becomes true while {@code B} is pressed
   */
  public Trigger b() {
    return on(() -> gamepad.b);
  }

  /**
   * Returns a trigger for the {@code X} button.
   *
   * @return trigger that becomes true while {@code X} is pressed
   */
  public Trigger x() {
    return on(() -> gamepad.x);
  }

  /**
   * Returns a trigger for the {@code Y} button.
   *
   * @return trigger that becomes true while {@code Y} is pressed
   */
  public Trigger y() {
    return on(() -> gamepad.y);
  }

  /**
   * Returns a trigger for the D-pad up button.
   *
   * @return trigger that becomes true while D-pad up is pressed
   */
  public Trigger dpadUp() {
    return on(() -> gamepad.dpad_up);
  }

  /**
   * Returns a trigger for the D-pad down button.
   *
   * @return trigger that becomes true while D-pad down is pressed
   */
  public Trigger dpadDown() {
    return on(() -> gamepad.dpad_down);
  }

  /**
   * Returns a trigger for the D-pad left button.
   *
   * @return trigger that becomes true while D-pad left is pressed
   */
  public Trigger dpadLeft() {
    return on(() -> gamepad.dpad_left);
  }

  /**
   * Returns a trigger for the D-pad right button.
   *
   * @return trigger that becomes true while D-pad right is pressed
   */
  public Trigger dpadRight() {
    return on(() -> gamepad.dpad_right);
  }

  /**
   * Returns a trigger for the left bumper.
   *
   * @return trigger that becomes true while the left bumper is pressed
   */
  public Trigger leftBumper() {
    return on(() -> gamepad.left_bumper);
  }

  /**
   * Returns a trigger for the right bumper.
   *
   * @return trigger that becomes true while the right bumper is pressed
   */
  public Trigger rightBumper() {
    return on(() -> gamepad.right_bumper);
  }

  /**
   * Returns a trigger for the left stick button.
   *
   * @return trigger that becomes true while the left stick button is pressed
   */
  public Trigger leftStick() {
    return on(() -> gamepad.left_stick_button);
  }

  /**
   * Returns a trigger for the right stick button.
   *
   * @return trigger that becomes true while the right stick button is pressed
   */
  public Trigger rightStick() {
    return on(() -> gamepad.right_stick_button);
  }

  /**
   * Returns a trigger for the {@code Start} button.
   *
   * @return trigger that becomes true while {@code Start} is pressed
   */
  public Trigger start() {
    return on(() -> gamepad.start);
  }

  /**
   * Returns a trigger for the {@code Back} button.
   *
   * @return trigger that becomes true while {@code Back} is pressed
   */
  public Trigger back() {
    return on(() -> gamepad.back);
  }

  /**
   * Returns a trigger for the guide/system button.
   *
   * @return trigger that becomes true while the guide button is pressed
   */
  public Trigger guide() {
    return on(() -> gamepad.guide);
  }

  /**
   * Returns a trigger for the left trigger using the default activation threshold.
   *
   * <p>This is useful for detecting deliberate trigger pulls instead of small accidental movements.
   *
   * @return trigger that becomes true while the left trigger exceeds the default threshold
   */
  public Trigger leftTrigger() {
    return on(() -> gamepad.left_trigger > ControllerConstants.TRIGGER_PRESSED_THRESHOLD_VALUE);
  }

  /**
   * Returns a trigger for the left trigger using a custom threshold.
   *
   * @param threshold minimum trigger value required to consider the trigger active
   * @return trigger that becomes true while the left trigger exceeds the threshold
   */
  public Trigger leftTrigger(double threshold) {
    return on(() -> gamepad.left_trigger > threshold);
  }

  /**
   * Returns a trigger for the right trigger using the default activation threshold.
   *
   * @return trigger that becomes true while the right trigger exceeds the default threshold
   */
  public Trigger rightTrigger() {
    return on(() -> gamepad.right_trigger > ControllerConstants.TRIGGER_PRESSED_THRESHOLD_VALUE);
  }

  /**
   * Returns a trigger for the right trigger using a custom threshold.
   *
   * @param threshold minimum trigger value required to consider the trigger active
   * @return trigger that becomes true while the right trigger exceeds the threshold
   */
  public Trigger rightTrigger(double threshold) {
    return on(() -> gamepad.right_trigger > threshold);
  }

  /**
   * Returns a trigger for the left stick X axis using the default threshold.
   *
   * <p>This is useful for detecting intentional horizontal stick movement.
   *
   * @return trigger that becomes true while the left stick X axis exceeds the default threshold
   */
  public Trigger leftX() {
    return leftX(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  /**
   * Returns a trigger for the left stick X axis using a custom threshold.
   *
   * @param threshold minimum absolute stick value required to consider the axis active
   * @return trigger that becomes true while the left stick X axis exceeds the threshold
   */
  public Trigger leftX(double threshold) {
    return on(() -> Math.abs(gamepad.left_stick_x) > threshold);
  }

  /**
   * Returns a trigger for the left stick Y axis using the default threshold.
   *
   * @return trigger that becomes true while the left stick Y axis exceeds the default threshold
   */
  public Trigger leftY() {
    return leftY(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  /**
   * Returns a trigger for the left stick Y axis using a custom threshold.
   *
   * @param threshold minimum absolute stick value required to consider the axis active
   * @return trigger that becomes true while the left stick Y axis exceeds the threshold
   */
  public Trigger leftY(double threshold) {
    return on(() -> Math.abs(gamepad.left_stick_y) > threshold);
  }

  /**
   * Returns a trigger for the right stick X axis using the default threshold.
   *
   * @return trigger that becomes true while the right stick X axis exceeds the default threshold
   */
  public Trigger rightX() {
    return rightX(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  /**
   * Returns a trigger for the right stick X axis using a custom threshold.
   *
   * @param threshold minimum absolute stick value required to consider the axis active
   * @return trigger that becomes true while the right stick X axis exceeds the threshold
   */
  public Trigger rightX(double threshold) {
    return on(() -> Math.abs(gamepad.right_stick_x) > threshold);
  }

  /**
   * Returns a trigger for the right stick Y axis using the default threshold.
   *
   * @return trigger that becomes true while the right stick Y axis exceeds the default threshold
   */
  public Trigger rightY() {
    return rightY(ControllerConstants.STICK_PRESSED_THRESHOLD_VALUE);
  }

  /**
   * Returns a trigger for the right stick Y axis using a custom threshold.
   *
   * @param threshold minimum absolute stick value required to consider the axis active
   * @return trigger that becomes true while the right stick Y axis exceeds the threshold
   */
  public Trigger rightY(double threshold) {
    return on(() -> Math.abs(gamepad.right_stick_y) > threshold);
  }

  /**
   * Returns the current value of the left stick X axis.
   *
   * @return raw left stick X input in the range {@code [-1.0, 1.0]}
   */
  public double getLeftX() {
    return gamepad.left_stick_x;
  }

  /**
   * Returns the current value of the left stick Y axis.
   *
   * @return raw left stick Y input in the range {@code [-1.0, 1.0]}
   */
  public double getLeftY() {
    return gamepad.left_stick_y;
  }

  /**
   * Returns the current value of the right stick X axis.
   *
   * @return raw right stick X input in the range {@code [-1.0, 1.0]}
   */
  public double getRightX() {
    return gamepad.right_stick_x;
  }

  /**
   * Returns the current value of the right stick Y axis.
   *
   * @return raw right stick Y input in the range {@code [-1.0, 1.0]}
   */
  public double getRightY() {
    return gamepad.right_stick_y;
  }

  /**
   * Returns the current value of the left trigger axis.
   *
   * @return raw left trigger input in the range {@code [0.0, 1.0]}
   */
  public double getLeftTriggerAxis() {
    return gamepad.left_trigger;
  }

  /**
   * Returns the current value of the right trigger axis.
   *
   * @return raw right trigger input in the range {@code [0.0, 1.0]}
   */
  public double getRightTriggerAxis() {
    return gamepad.right_trigger;
  }

  /**
   * Returns the wrapped FTC gamepad instance.
   *
   * @return underlying FTC gamepad
   */
  public Gamepad getGamepad() {
    return gamepad;
  }
}
