package org.firstinspires.ftc.teamcode.robot.constants;

/**
 * GlobalConstants class is a utility class to store constant values.
 * This class is for global constants that will be used in many files
 * and/or subsystems.
 * <P>
 *     Constants unique to only one subsystem should not be put in this class, instead those should be put on their own constants file
 * </P>
 */
public class GlobalConstants {
    public static final int HD_HEX_TICKS_PER_REVOLUTION = 22;
    public static final int CORE_HEX_TICKS_PER_REVOLUTION = 288;

    public static class Controller {
        public static final double TRIGGER_PRESSED_THRESHOLD_VALUE = 0.9;
    }
}
