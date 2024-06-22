package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ButtonListener is a utility class that listens for a button press and runs a Runnable if the button is pressed
 * or a Runnable if the button is not pressed.
 * <p>
 * It can also listen for multiple buttons and do comparisons.
 */
public class ButtonListener {

    private final List<Boolean> buttonList;
    private boolean button;

    private ButtonListener() {
        this.button = false;
        this.buttonList = new ArrayList<>();
    }

    /**
     * Creates a new ButtonBuilder instance with the given boolean value button.
     * <p>
     * ButtonBuilder is a builder class that allows for chaining of conditions.
     *
     * @param button the button to listen for
     * @return a new ButtonBuilder instance
     */
    public static ButtonBuilder whileTrue(boolean button) {
        return new ButtonBuilder().whileTrue(button);
    }

    public static class ButtonBuilder {
        private final ButtonListener ButtonListenerInstance;

        private ButtonBuilder() {
            ButtonListenerInstance = new ButtonListener();
        }

        public ButtonBuilder whileTrue(boolean button) {
            ButtonListenerInstance.buttonList.add(button);
            ButtonListenerInstance.button = button;
            return this;
        }

        /**
         * Adds a condition to the button listener.
         *
         * @param condition the condition to add
         * @return the ButtonBuilder instance
         */
        public ButtonBuilder and(boolean condition) {
            ButtonListenerInstance.buttonList.add(condition);
            ButtonListenerInstance.button = ButtonListenerInstance.buttonList.stream().allMatch(Boolean::booleanValue);
            return this;
        }

        /**
         * Adds a condition to the button listener.
         *
         * @param condition the condition to add
         * @return the ButtonBuilder instance
         */
        public ButtonBuilder andNot(boolean condition) {
            ButtonListenerInstance.buttonList.add(!condition);
            ButtonListenerInstance.button = ButtonListenerInstance.buttonList.stream().allMatch(Boolean::booleanValue);
            return this;
        }

        /**
         * Adds a condition to the button listener.
         *
         * @param condition the condition to add
         * @return the ButtonBuilder instance
         */
        public ButtonBuilder or(boolean condition) {
            ButtonListenerInstance.buttonList.add(condition);
            ButtonListenerInstance.button = ButtonListenerInstance.buttonList.stream().anyMatch(Boolean::booleanValue);
            return this;
        }

        /**
         * Runs the given Runnable.
         * <p>
         * Its possible pass a lambda expression or a method reference.
         * @param runnable the Runnable to run
         */
        public void run(Runnable runnable) {
            if (ButtonListenerInstance.button) {
                runnable.run();
            }

            ButtonListenerInstance.buttonList.clear();
            ButtonListenerInstance.button = false;
        }

        /**
         * Runs the given Runnable if the conditions are true, otherwise runs the elseRunnable.
         * <p>
         * Its possible pass a lambda expression or a method reference.
         * @param runnable the Runnable to run if the conditions are true
         * @param elseRunnable the Runnable to run if the conditions are false
         */
        public void run(Runnable runnable, Runnable elseRunnable) {
            if (ButtonListenerInstance.button) {
                runnable.run();
            } else {
                elseRunnable.run();
            }

            ButtonListenerInstance.buttonList.clear();
            ButtonListenerInstance.button = false;
        }
    }
}