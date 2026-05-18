package org.firstinspires.ftc.teamcode.core.util;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadTriggerController;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;

public class RobotContainerInternal {
     private final GamepadTriggerController driver;
     private final GamepadTriggerController operator;

    public RobotContainerInternal(GamepadTriggerController driverGamepad, GamepadTriggerController operatorGamepad) {
        this.driver = driverGamepad;
        this.operator = operatorGamepad;

        configureBindings();
    }

    public void update() {
        driver.update();
        operator.update();
    }

    protected void configureBindings() {}

    protected GamepadTriggerController getDriver() {
        return driver;
    }

    protected GamepadTriggerController getOperator() {
        return operator;
    }
}