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

    public void rumbleTimer(int milliseconds) {
        gamepad.rumble(1, 1, milliseconds);
    }

    public void rumbleBlips(int counts) {
        gamepad.rumbleBlips(counts);
    }

    public void rumbleEnable(){
        gamepad.rumble(1, 1, Gamepad.RUMBLE_DURATION_CONTINUOUS);
    }

    public void rumbleStop() {
        gamepad.stopRumble();
    }
}
