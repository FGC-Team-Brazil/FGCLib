package org.firstinspires.ftc.teamcode.core.util;

import com.qualcomm.robotcore.hardware.Gamepad;

public class SmartController extends GamepadButton {
    public Gamepad gamepad;

    public SmartController(Gamepad gamepad) {
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
