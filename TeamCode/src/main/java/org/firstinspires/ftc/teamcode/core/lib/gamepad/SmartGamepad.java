package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

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
