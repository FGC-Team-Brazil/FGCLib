package org.firstinspires.ftc.teamcode.robot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.lib.Robot;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.Pose2d;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.TrajectorySequence;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.core.lib.autonomousControl.TrajectorySequenceRunner;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.robot.RobotSubsystems;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "TeleOp 2", group = "TeleOp 2")
public class TeleOpMode2 extends OpMode {
    Robot robot = new Robot();
    private SmartGamepad driver;
    List<Subsystem> subsystemsDriver;
    TrajectorySequenceRunner runner = new TrajectorySequenceRunner();

    @Override
    public void init() {
        robot.configGamepadManager(gamepad1, gamepad2);
        robot.init(hardwareMap, telemetry); // Don't remove this line

        subsystemsDriver = RobotSubsystems.get();

        TrajectorySequence trajectorySequence= new TrajectorySequenceBuilder()
                .startTrajectoryCourse(new Pose2d(0,10,0))
                .addBasicCommand(()->{
                    telemetry.addData("boo","ahh!");
                })
                .addCourseSegment(new Pose2d(10,10,90))
                .buildCourse()
                .holdPositionForSeconds(2)
                .startTrajectoryCourse(new Pose2d(-10,-10,180))
                .buildCourse()
                .buildSequence();



        runner.setSequence(trajectorySequence);
    }

    @Override
    public void start() {
        //subsystemsDriver.forEach(Subsystem::start);
        telemetry.update();
    }

    @Override
    public void loop() {

        //subsystemsDriver.forEach(subsystem -> subsystem.execute( null));
        runner.execute(0,0);
        telemetry.update();
    }

    @Override
    public void stop() {
        subsystemsDriver.forEach(Subsystem::stop);
        telemetry.update();
    }

}