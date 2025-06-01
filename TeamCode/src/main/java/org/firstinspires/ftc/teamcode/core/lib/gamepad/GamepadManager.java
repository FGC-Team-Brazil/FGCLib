package org.firstinspires.ftc.teamcode.core.lib.gamepad;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * GamepadManager is a utility class that controls the instances
 * of the gamepads on the Lib. It grants that will be existing only
 * two instances of the GamePad class
 */
public class GamepadManager {
    private static SmartGamepad driver;
    private static SmartGamepad operator;

    /**
     *  This method is called upon configuration and initialization
     *  of the Robot class to assign each gamepad to each corresponding
     *  function (Eg: gamepad1 -> driver; gamepad2 ->operator)
     * @param driver (gamepad that corresponds to driver)
     * @param operator (gamepad that corresponds to driver)
     * @return gamepad manager instance
     */
    public static GamepadManager use(@NonNull Gamepad driver, @NonNull Gamepad operator) {
        GamepadManager.driver = new SmartGamepad(driver);
        GamepadManager.operator = new SmartGamepad(operator);
        return new GamepadManager();
    }

    /**
     *  This method is can be used when only one driver is controling the robot.
     *  It isn't used by default, but can be enabled by just using one argument
     *  instead of two on robot.configGamepadManager() when creating an opmode
     * @param driver (gamepad that corresponds to driver)
     * @return gamepad manager instance
     */
    public static GamepadManager use(@NonNull Gamepad driver) {
        GamepadManager.driver = new SmartGamepad(driver);
        GamepadManager.operator = new SmartGamepad(driver);
        return new GamepadManager();
    }

    private GamepadManager() {
    }

    /** Returns the SmartGamepad instance assigned to the driver
     *
     * @return SmartGamepad assingned to the Driver
     */
    public SmartGamepad getDriver() {
        return driver;
    }
    /** Returns the basic Gamepad instance assigned to the driver
     *
     * @return Gamepad assingned to the Driver
     */
    public Gamepad getDriverBasic() {
        return driver.gamepad;
    }
    /** Returns the SmartGamepad instance assigned to the operator
     *
     * @return SmartGamepad assingned to the operator
     */
    public SmartGamepad getOperator() {
        return operator;
    }
    /** Returns the basic Gamepad instance assigned to the operator
     *
     * @return Gamepad assingned to the operator
     */
    public Gamepad getOperatorBasic() {
        return operator.gamepad;
    }
}
