package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.constants.GlobalConstants;

/**
 * GamepadButton is a utility class that intermediates the access to the gamepads.
 * The class maps all the buttons of the gamepads, so other classes can use them.
 * <br>
 * It uses the ButtonListener utility class to control the interactions
 * with the buttons
 * Always use the SmartController class instead of this class
 */
public class GamepadButton {
    public Gamepad gamepad;
    public ButtonListener buttonA;
    public ButtonListener buttonB;
    public ButtonListener buttonY;
    public ButtonListener buttonX;
    public ButtonListener buttonDPadUp;
    public ButtonListener buttonDPadDown;
    public ButtonListener buttonDPadLeft;
    public ButtonListener buttonDPadRight;
    public ButtonListener buttonLeftBumper;
    public ButtonListener buttonRightBumper;
    public ButtonListener buttonLeftStickButton;
    public ButtonListener buttonRightStickButton;
    public ButtonListener buttonStart;
    public ButtonListener buttonBack;
    public ButtonListener buttonGuide;

    public GamepadButton(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public double getLeftStickY() {
        return gamepad.left_stick_y;
    }

    public double getLeftStickX() {
        return gamepad.left_stick_x;
    }

    public double getRightStickX() {
        return gamepad.right_stick_x;
    }

    public double getRightStickY() {
        return gamepad.right_stick_y;
    }

    public boolean isButtonA() {
        return gamepad.a;
    }

    public boolean isButtonB() {
        return gamepad.b;
    }

    public boolean isButtonX() {
        return gamepad.x;
    }

    public boolean isButtonY() {
        return gamepad.y;
    }

    public boolean isButtonDPadUp() {
        return gamepad.dpad_up;
    }

    public boolean isButtonDPadDown() {
        return gamepad.dpad_down;
    }

    public boolean isButtonDPadLeft() {
        return gamepad.dpad_left;
    }

    public boolean isButtonDPadRight() {
        return gamepad.dpad_right;
    }

    public boolean isButtonLeftBumper() {
        return gamepad.left_bumper;
    }

    public boolean isButtonRightBumper() {
        return gamepad.right_bumper;
    }

    public boolean isButtonLeftStickButton() {
        return gamepad.left_stick_button;
    }

    public boolean isButtonRightStickButton() {
        return gamepad.right_stick_button;
    }

    public double getLeftTrigger() {
        return gamepad.left_trigger;
    }

    public double getRightTrigger() {
        return gamepad.right_trigger;
    }

    public boolean isLeftTriggerPressed() {
        return gamepad.left_trigger > GlobalConstants.Controller.TRIGGER_PRESSED_THRESHOLD_VALUE;
    }

    public boolean isRightTriggerPressed() {
        return gamepad.right_trigger > GlobalConstants.Controller.TRIGGER_PRESSED_THRESHOLD_VALUE;
    }

    public boolean isButtonStart() {
        return gamepad.start;
    }

    public boolean isButtonBack() {
        return gamepad.back;
    }

    public boolean isButtonGuide() {
        return gamepad.guide;
    }

    public ButtonListener.ButtonBuilder whileButtonA() {
        return ButtonListener.whileTrue(isButtonA());
    }

    public ButtonListener.ButtonBuilder whileButtonB() {
        return ButtonListener.whileTrue(isButtonB());
    }

    public ButtonListener.ButtonBuilder whileButtonRightBumper() {
        return ButtonListener.whileTrue(isButtonRightBumper());
    }

    public ButtonListener.ButtonBuilder whileButtonLeftBumper() {
        return ButtonListener.whileTrue(isButtonLeftBumper());
    }

    public ButtonListener.ButtonBuilder whileButtonY() {
        return ButtonListener.whileTrue(isButtonY());
    }

    public ButtonListener.ButtonBuilder whileButtonX() {
        return ButtonListener.whileTrue(isButtonX());
    }

    public ButtonListener.ButtonBuilder whileButtonDPadUp() {
        return ButtonListener.whileTrue(isButtonDPadUp());
    }

    public ButtonListener.ButtonBuilder whileButtonDPadDown() {
        return ButtonListener.whileTrue(isButtonDPadDown());
    }

    public ButtonListener.ButtonBuilder whileButtonDPadLeft() {
        return ButtonListener.whileTrue(isButtonDPadLeft());
    }

    public ButtonListener.ButtonBuilder whileButtonDPadRight() {
        return ButtonListener.whileTrue(isButtonDPadRight());
    }

    public ButtonListener.ButtonBuilder whileButtonLeftStickButton() {
        return ButtonListener.whileTrue(isButtonLeftStickButton());
    }

    public ButtonListener.ButtonBuilder whileButtonRightStickButton() {
        return ButtonListener.whileTrue(isButtonRightStickButton());
    }

    public ButtonListener.ButtonBuilder whileButtonStart() {
        return ButtonListener.whileTrue(isButtonStart());
    }

    public ButtonListener.ButtonBuilder whileButtonBack() {
        return ButtonListener.whileTrue(isButtonBack());
    }

    public ButtonListener.ButtonBuilder whileButtonGuide() {
        return ButtonListener.whileTrue(isButtonGuide());
    }

    public ButtonListener.ButtonBuilder whileLeftTriggerPressed() {
        return ButtonListener.whileTrue(isLeftTriggerPressed());
    }

    public ButtonListener.ButtonBuilder whileRightTriggerPressed() {
        return ButtonListener.whileTrue(isRightTriggerPressed());
    }
}
