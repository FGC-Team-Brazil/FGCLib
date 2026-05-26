package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Trigger {

  private static final List<Trigger> activeTriggers = new ArrayList<>();

  public static void updateAll() {
    for (Trigger trigger : activeTriggers) {
      trigger.poll();
    }
  }

  public static void clearAll() {
    activeTriggers.clear();
  }

  private final BooleanSupplier condition;

  private Runnable whileTrueAction;
  private Runnable whileFalseAction;
  private Runnable onTrueAction;
  private Runnable onFalseAction;

  private boolean lastState = false;

  public Trigger(BooleanSupplier condition) {
    this.condition = condition;
    activeTriggers.add(this);
  }

  public boolean isTrue() {
    return condition.getAsBoolean();
  }

  public boolean isFalse() {
    return !condition.getAsBoolean();
  }

  public Trigger and(Trigger other) {
    return new Trigger(() -> condition.getAsBoolean() && other.condition.getAsBoolean());
  }

  public Trigger or(Trigger other) {
    return new Trigger(() -> condition.getAsBoolean() || other.condition.getAsBoolean());
  }

  public Trigger negate() {
    return new Trigger(() -> !condition.getAsBoolean());
  }

  public Trigger whileTrue(Runnable action) {
    this.whileTrueAction = action;
    return this;
  }

  public Trigger whileFalse(Runnable action) {
    this.whileFalseAction = action;
    return this;
  }

  public Trigger onTrue(Runnable action) {
    this.onTrueAction = action;
    return this;
  }

  public Trigger onFalse(Runnable action) {
    this.onFalseAction = action;
    return this;
  }

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
