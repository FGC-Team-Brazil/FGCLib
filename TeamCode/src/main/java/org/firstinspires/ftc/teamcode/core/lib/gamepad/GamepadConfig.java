package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadConfig {
    private static Gamepad driver;
    private static Gamepad operator;

    public static GamepadConfig use(@NonNull Gamepad driver, @NonNull Gamepad operator) {
        GamepadConfig.driver = driver;
        GamepadConfig.operator = operator;
        return new GamepadConfig();
    }

    public static GamepadConfig use(@NonNull Gamepad driver) {
        GamepadConfig.driver = driver;
        GamepadConfig.operator = driver;
        return new GamepadConfig();
    }

    private GamepadConfig() {
    }

    public Gamepad getDriver() {
        return driver;
    }

    public Gamepad getOperator() {
        return operator;
    }

    public void setDriver(Gamepad driver) {
        GamepadConfig.driver = driver;
    }

    public void setOperator(Gamepad operator) {
        GamepadConfig.operator = operator;
    }
}
