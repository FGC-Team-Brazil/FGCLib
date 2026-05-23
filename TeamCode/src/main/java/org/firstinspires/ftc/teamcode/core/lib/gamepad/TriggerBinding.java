package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import java.util.function.BooleanSupplier;

public class TriggerBinding {
  private final GamepadController controller;
  private final BooleanSupplier condition;
  private Runnable activeAction, risingAction, fallingAction;
  private boolean lastState = false;

  public TriggerBinding(GamepadController controller, BooleanSupplier condition) {
    this.controller = controller;
    this.condition = condition;
  }

  public TriggerBinding or(TriggerBinding other) {
    return controller.on(
        () -> this.condition.getAsBoolean() || other.getCondition().getAsBoolean());
  }

  public TriggerBinding and(TriggerBinding other) {
    return controller.on(
        () -> this.condition.getAsBoolean() && other.getCondition().getAsBoolean());
  }

  public TriggerBinding negate() {
    return controller.on(() -> !this.condition.getAsBoolean());
  }

  public BooleanSupplier getCondition() {
    return condition;
  }

  public TriggerBinding whileActive(Runnable action) {
    this.activeAction = action;
    return this;
  }

  public TriggerBinding whenActive(Runnable action) {
    this.risingAction = action;
    return this;
  }

  public TriggerBinding whenInactive(Runnable action) {
    this.fallingAction = action;
    return this;
  }

  public void poll() {
    boolean currentState = condition.getAsBoolean();
    if (currentState) {
      if (activeAction != null) activeAction.run();
      if (!lastState && risingAction != null) risingAction.run();
    } else if (lastState && fallingAction != null) {
      fallingAction.run();
    }
    lastState = currentState;
  }
}
