package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Represents a boolean condition that can be bound to robot actions.
 *
 * <p>This class is used to model driver inputs, sensor states, and logical
 * combinations of conditions in an event-driven control flow. It supports
 * continuous actions while a condition remains true or false, as well as
 * edge-triggered actions when the condition changes state.
 *
 * <p>Typical uses include button presses, limit switches, sensor thresholds,
 * and combined driver control logic.
 */
public class Trigger {

  /** All currently active triggers updated by {@link #updateAll()}. */
  private static final List<Trigger> activeTriggers = new ArrayList<>();

  /**
   * Polls every active trigger and evaluates its bound actions.
   *
   * <p>This should be called once per robot loop so all registered trigger
   * conditions can react to input changes and maintain continuous actions.
   */
  public static void updateAll() {
    for (Trigger trigger : activeTriggers) {
      trigger.poll();
    }
  }

  /**
   * Clears all registered triggers from the active trigger list.
   *
   * <p>This is typically called when a new OpMode begins to avoid keeping
   * bindings from a previous robot session.
   */
  public static void clearAll() {
    activeTriggers.clear();
  }

  /** Condition used to determine whether this trigger is active. */
  private final BooleanSupplier condition;

  /** Action executed continuously while the condition remains true. */
  private Runnable whileTrueAction;

  /** Action executed continuously while the condition remains false. */
  private Runnable whileFalseAction;

  /** Action executed once when the condition transitions from false to true. */
  private Runnable onTrueAction;

  /** Action executed once when the condition transitions from true to false. */
  private Runnable onFalseAction;

  /** Previous evaluated state of the condition. */
  private boolean lastState = false;

  /**
   * Creates a new trigger from a boolean condition.
   *
   * <p>The trigger is automatically added to the active trigger list so it
   * can be evaluated by {@link #updateAll()}.
   *
   * @param condition boolean condition that defines this trigger
   */
  public Trigger(BooleanSupplier condition) {
    this.condition = condition;
    activeTriggers.add(this);
  }

  /**
   * Returns whether the trigger condition is currently true.
   *
   * @return {@code true} when the condition evaluates to true
   */
  public boolean isTrue() {
    return condition.getAsBoolean();
  }

  /**
   * Returns whether the trigger condition is currently false.
   *
   * @return {@code true} when the condition evaluates to false
   */
  public boolean isFalse() {
    return !condition.getAsBoolean();
  }

  /**
   * Creates a new trigger that is active only when both triggers are true.
   *
   * @param other another trigger to combine with this one
   * @return a new trigger representing the logical AND of both conditions
   */
  public Trigger and(Trigger other) {
    return new Trigger(() -> condition.getAsBoolean() && other.condition.getAsBoolean());
  }

  /**
   * Creates a new trigger that is active when either trigger is true.
   *
   * @param other another trigger to combine with this one
   * @return a new trigger representing the logical OR of both conditions
   */
  public Trigger or(Trigger other) {
    return new Trigger(() -> condition.getAsBoolean() || other.condition.getAsBoolean());
  }

  /**
   * Creates a new trigger with the inverse of this trigger condition.
   *
   * @return a new trigger representing the logical negation of this condition
   */
  public Trigger negate() {
    return new Trigger(() -> !condition.getAsBoolean());
  }

  /**
   * Registers an action that runs continuously while the trigger remains true.
   *
   * <p>This is useful for mechanisms that should keep receiving power or
   * repeated commands while a control input stays active.
   *
   * @param action action to run while the condition is true
   * @return this trigger instance for chaining
   */
  public Trigger whileTrue(Runnable action) {
    this.whileTrueAction = action;
    return this;
  }

  /**
   * Registers an action that runs continuously while the trigger remains false.
   *
   * <p>This is useful for fallback behavior when a control input is not active.
   *
   * @param action action to run while the condition is false
   * @return this trigger instance for chaining
   */
  public Trigger whileFalse(Runnable action) {
    this.whileFalseAction = action;
    return this;
  }

  /**
   * Registers an action that runs once when the trigger transitions to true.
   *
   * <p>This is typically used for event-driven robot actions such as toggles,
   * setpoints, and one-shot commands.
   *
   * @param action action to run on the rising edge
   * @return this trigger instance for chaining
   */
  public Trigger onTrue(Runnable action) {
    this.onTrueAction = action;
    return this;
  }

  /**
   * Registers an action that runs once when the trigger transitions to false.
   *
   * <p>This is typically used to stop motors, cancel commands, or reset state
   * when a control input is released.
   *
   * @param action action to run on the falling edge
   * @return this trigger instance for chaining
   */
  public Trigger onFalse(Runnable action) {
    this.onFalseAction = action;
    return this;
  }

  /**
   * Evaluates the trigger condition and executes the appropriate actions.
   *
   * <p>This method handles both continuous actions and edge-triggered actions
   * based on the current and previous state of the condition.
   */
  void poll() {
    boolean current = condition.getAsBoolean();

    if (current) {
      if (whileTrueAction != null) whileTrueAction.run();
      if (!lastState && onTrueAction != null) onTrueAction.run();
    } else {
      if (whileFalseAction != null) whileFalseAction.run();
      if (lastState && onFalseAction != null) onFalseAction.run();
    }

    lastState = current;
  }
}