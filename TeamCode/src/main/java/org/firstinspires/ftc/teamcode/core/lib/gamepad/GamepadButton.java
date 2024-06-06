package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.constants.GlobalConstants;

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
        this.buttonA = new ButtonListener(gamepad.a);
        this.buttonB = new ButtonListener(gamepad.b);
        this.buttonY = new ButtonListener(gamepad.y);
        this.buttonX = new ButtonListener(gamepad.x);
        this.buttonDPadUp = new ButtonListener(gamepad.dpad_up);
        this.buttonDPadDown = new ButtonListener(gamepad.dpad_down);
        this.buttonDPadLeft = new ButtonListener(gamepad.dpad_left);
        this.buttonDPadRight = new ButtonListener(gamepad.dpad_right);
        this.buttonLeftBumper = new ButtonListener(gamepad.left_bumper);
        this.buttonRightBumper = new ButtonListener(gamepad.right_bumper);
        this.buttonLeftStickButton = new ButtonListener(gamepad.left_stick_button);
        this.buttonRightStickButton = new ButtonListener(gamepad.right_stick_button);
        this.buttonStart = new ButtonListener(gamepad.start);
        this.buttonBack = new ButtonListener(gamepad.back);
        this.buttonGuide = new ButtonListener(gamepad.guide);
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
        return buttonA.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonB() {
        return buttonB.whileTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonA() {
        return buttonA.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonB() {
        return buttonB.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonX() {
        return buttonX.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonY() {
        return buttonY.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonDPadUp() {
        return buttonDPadUp.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonDPadDown() {
        return buttonDPadDown.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonDPadLeft() {
        return buttonDPadLeft.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonDPadRight() {
        return buttonDPadRight.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonLeftBumper() {
        return buttonLeftBumper.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonRightBumper() {
        return buttonRightBumper.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonLeftStickButton() {
        return buttonLeftStickButton.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonRightStickButton() {
        return buttonRightStickButton.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonStart() {
        return buttonStart.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonBack() {
        return buttonBack.whileTrue();
    }

    public ButtonListener.ButtonBuilder whileButtonGuide() {
        return buttonGuide.whileTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonX() {
        return buttonX.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonY() {
        return buttonY.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonDPadUp() {
        return buttonDPadUp.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonDPadDown() {
        return buttonDPadDown.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonDPadLeft() {
        return buttonDPadLeft.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonDPadRight() {
        return buttonDPadRight.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonLeftBumper() {
        return buttonLeftBumper.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonRightBumper() {
        return buttonRightBumper.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonLeftStickButton() {
        return buttonLeftStickButton.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonRightStickButton() {
        return buttonRightStickButton.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonStart() {
        return buttonStart.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonBack() {
        return buttonBack.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnButtonGuide() {
        return buttonGuide.toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder whileLeftTriggerPressed() {
        return new ButtonListener(gamepad.left_trigger > GlobalConstants.Controller.TRIGGER_PRESSED_THRESHOLD_VALUE).whileTrue();
    }

    public ButtonListener.ButtonBuilder whileRightTriggerPressed() {
        return new ButtonListener(gamepad.right_trigger > GlobalConstants.Controller.TRIGGER_PRESSED_THRESHOLD_VALUE).whileTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnLeftTriggerPressed() {
        return new ButtonListener(gamepad.left_trigger > GlobalConstants.Controller.TRIGGER_PRESSED_THRESHOLD_VALUE).toggleOnTrue();
    }

    public ButtonListener.ButtonBuilder toggleOnRightTriggerPressed() {
        return new ButtonListener(gamepad.right_trigger > GlobalConstants.Controller.TRIGGER_PRESSED_THRESHOLD_VALUE).toggleOnTrue();
    }

}
