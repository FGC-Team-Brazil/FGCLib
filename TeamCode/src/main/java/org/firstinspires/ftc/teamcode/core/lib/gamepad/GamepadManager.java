package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadManager {
    private static SmartGamepad driver;
    private static SmartGamepad operator;

    public static GamepadManager use(@NonNull Gamepad driver, @NonNull Gamepad operator) {
        GamepadManager.driver = new SmartGamepad(driver);
        GamepadManager.operator = new SmartGamepad(operator);
        return new GamepadManager();
    }

    public static GamepadManager use(@NonNull Gamepad driver) {
        GamepadManager.driver = new SmartGamepad(driver);
        GamepadManager.operator = new SmartGamepad(driver);
        return new GamepadManager();
    }

    private GamepadManager() {
    }

    public SmartGamepad getDriver() {
        return driver;
    }

    public SmartGamepad getOperator() {
        return operator;
    }
}
