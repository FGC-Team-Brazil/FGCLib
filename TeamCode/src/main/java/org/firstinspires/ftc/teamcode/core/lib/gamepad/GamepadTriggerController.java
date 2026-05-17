package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class GamepadTriggerController {
    private final List<TriggerBinding> bindings = new ArrayList<>();
    private final SmartGamepad smartGamepad;

    public GamepadTriggerController(SmartGamepad smartGamepad) {
        this.smartGamepad = smartGamepad;
    }

    public SmartGamepad getHID() {
        return smartGamepad;
    }

    public TriggerBinding on(BooleanSupplier condition) {
        TriggerBinding binding = new TriggerBinding(condition);
        bindings.add(binding);
        return binding;
    }

    public TriggerBinding a() { return on(smartGamepad::isButtonA); }
    public TriggerBinding b() { return on(smartGamepad::isButtonB); }
    public TriggerBinding x() { return on(smartGamepad::isButtonX); }
    public TriggerBinding y() { return on(smartGamepad::isButtonY); }

    public TriggerBinding dpadUp() { return on(smartGamepad::isButtonDPadUp); }
    public TriggerBinding dpadDown() { return on(smartGamepad::isButtonDPadDown); }
    public TriggerBinding dpadLeft() { return on(smartGamepad::isButtonDPadLeft); }
    public TriggerBinding dpadRight() { return on(smartGamepad::isButtonDPadRight); }

    public TriggerBinding leftBumper() { return on(smartGamepad::isButtonLeftBumper); }
    public TriggerBinding rightBumper() { return on(smartGamepad::isButtonRightBumper); }

    public TriggerBinding start() { return on(smartGamepad::isButtonStart); }
    public TriggerBinding back() { return on(smartGamepad::isButtonBack); }
    public TriggerBinding guide() { return on(smartGamepad::isButtonGuide); }

    public TriggerBinding leftStick() { return on(smartGamepad::isButtonLeftStickButton); }
    public TriggerBinding rightStick() { return on(smartGamepad::isButtonRightStickButton); }

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

    public double getLeftX() { return smartGamepad.getLeftStickX(); }
    public double getLeftY() { return smartGamepad.getLeftStickY(); }
    public double getRightX() { return smartGamepad.getRightStickX(); }
    public double getRightY() { return smartGamepad.getRightStickY(); }
    public double getLeftTriggerAxis() { return smartGamepad.getLeftTrigger(); }
    public double getRightTriggerAxis() { return smartGamepad.getRightTrigger(); }

    public void update() {
        for (TriggerBinding binding : bindings) {
            binding.poll();
        }
    }

    public class TriggerBinding {
        private final BooleanSupplier condition;
        private Runnable activeAction, risingAction, fallingAction;
        private boolean lastState = false;

        public TriggerBinding(BooleanSupplier condition) {
            this.condition = condition;
        }

        public TriggerBinding or(TriggerBinding other) {
            return on(() -> this.condition.getAsBoolean() || other.getCondition().getAsBoolean());
        }

        public TriggerBinding and(TriggerBinding other) {
            return on(() -> this.condition.getAsBoolean() && other.getCondition().getAsBoolean());
        }

        public TriggerBinding negate() {
            return on(() -> !this.condition.getAsBoolean());
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
}