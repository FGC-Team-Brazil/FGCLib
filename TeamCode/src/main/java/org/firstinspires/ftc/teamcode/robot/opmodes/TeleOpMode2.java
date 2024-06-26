package org.firstinspires.ftc.teamcode.robot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;

/**
 * The opmodes folder is where all the opmodes
 * will be written. Just the main TeleOp mode is
 * outside this folder
 * <br><br>
 * This class is an example of opmode that we recommend you to use
 * as template
 */
@TeleOp(name = "TeleOp 2", group = "TeleOp 2")
public class TeleOpMode2 extends OpMode {

    private SmartGamepad driver;

    @Override
    public void init() {
       // robot.gamepadConfig(gamepad1, gamepad2);
        //robot.init(hardwareMap, telemetry); // Don't remove this line
    }

    @Override
    public void start() {
        //subsystemsDriver.forEach(Subsystem::start);
        //telemetry.update();
    }

    @Override
    public void loop() {
        //subsystemsDriver.forEach(subsystem -> subsystem.execute(driver, null));
        telemetry.update();
    }

    @Override
    public void stop() {
       // subsystemsDriver.forEach(Subsystem::stop);
        telemetry.update();
    }

}