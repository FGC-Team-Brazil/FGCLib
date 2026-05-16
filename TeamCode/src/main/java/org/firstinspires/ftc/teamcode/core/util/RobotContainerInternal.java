package org.firstinspires.ftc.teamcode.core.util;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.GamepadTriggerController;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;

public class RobotContainerInternal {
     public GamepadTriggerController driver;
     public GamepadTriggerController operator;

    public void setup(SmartGamepad driverGamepad, SmartGamepad operatorGamepad) {
        if (driverGamepad != null) {
            this.driver = new GamepadTriggerController(driverGamepad);
        }

        if (operatorGamepad != null) {
            this.operator = new GamepadTriggerController(operatorGamepad);
        }

        if (driverGamepad != null && operatorGamepad != null) {
            configureBindings();
        }
    }

    public void update() {
        if (driver != null) {
            driver.update();
        }
        if (operator != null) {
            operator.update();
        }
    }

    protected void configureBindings() {}
}