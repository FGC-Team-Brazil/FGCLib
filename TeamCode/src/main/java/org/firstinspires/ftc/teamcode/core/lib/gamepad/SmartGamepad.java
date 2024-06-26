package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * SmartController class is a extension of the GamepadButton class.
 * It contains other functions of the gamepads, as rumble.
 * <br>
 * Always use this class instead of the GamepadButton.
 */
public class SmartGamepad extends GamepadButton {
    public Gamepad gamepad;

    public SmartGamepad(Gamepad gamepad) {
        super(gamepad);
        this.gamepad = gamepad;
    }

    public void rumble(double leftRumble, double rightRumble, int milliseconds) {
        gamepad.rumble(leftRumble, rightRumble, milliseconds);
    }

    public void rumble(double rumble, int milliseconds) {
        gamepad.rumble(rumble, rumble, 0);
    }

    public void rumbleBlips(int counts) {
        gamepad.rumbleBlips(counts);
    }

}
