package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartController;
import org.firstinspires.ftc.teamcode.robot.subsystems.SubsystemExample;

import java.util.ArrayList;

@TeleOp(name = "TeleOp", group = "Official TeleOp")
public class TeleOpMode extends OpMode {


    private SmartController driver;
    private SmartController operator;
    private ArrayList<Subsystem> subsystems = new ArrayList<Subsystem>();
    private final Robot robot = new Robot();

    @Override
    public void init() {

        DrivetrainBuilder drivetrainBuilder = DrivetrainBuilder.configure("drivetrain_motorRight", "drivetrain_motorLeft", false, true);

        this.subsystems.add(drivetrainBuilder);

        // Don't remove this line
        robot.init(hardwareMap, telemetry, gamepad1, gamepad2, subsystems);
    }

    @Override
    public void start() {
        // Don't remove this line
        robot.start();

    }

    @Override
    public void loop() {
        // Don't remove this line
        robot.loop();

    }

    @Override
    public void stop() {
        // Don't remove this line
        robot.stop();

    }

}
