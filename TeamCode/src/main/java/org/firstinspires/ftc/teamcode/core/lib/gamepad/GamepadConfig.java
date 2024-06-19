package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadConfig {
    private static SmartController driver;
    private static SmartController operator;

    public static GamepadConfig use(@NonNull Gamepad driver, @NonNull Gamepad operator) {
        GamepadConfig.driver = new SmartController(driver);
        GamepadConfig.operator = new SmartController(operator);
        return new GamepadConfig();
    }

    public static GamepadConfig use(@NonNull Gamepad driver) {
        GamepadConfig.driver = new SmartController(driver);
        GamepadConfig.operator = new SmartController(driver);
        return new GamepadConfig();
    }

    private GamepadConfig() {
    }

    public SmartController driver() {
        return driver;
    }

    public SmartController operator() {
        return operator;
    }
}
