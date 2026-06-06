package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.core.lib.internal.RobotContainerInternal;
import org.firstinspires.ftc.teamcode.robot.subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.core.lib.vision.apriltag.AprilTagDetection;

public class RobotContainer extends RobotContainerInternal {

  private final SmartGamepad driver;

  private final VisionSubsystem vision;

  public RobotContainer(Gamepad driver, Gamepad operator) {

    super(
            VisionSubsystem.getInstance()
    );

    this.driver = new SmartGamepad(driver);

    vision = VisionSubsystem.getInstance();
  }

  @Override
  protected void configureBindings() {

    // Vibra quando encontra qualquer tag
    new Trigger(vision::hasTarget)
            .onTrue(() ->
                    driver.getHID().rumbleBlips(1)
            );

    // Desabilita visão
    driver.x()
            .onTrue(vision::disableVision);

    // Habilita visão
    driver.y()
            .onTrue(vision::enableVision);

    // Procura especificamente a tag ID 5
    driver.a()
            .onTrue(() -> {

              AprilTagDetection tag =
                      vision.getTagById(5);

              if (tag != null) {

                System.out.println("TAG 5 ENCONTRADA");

                System.out.println(
                        "Yaw = " + tag.getYaw()
                );

                System.out.println(
                        "Distance = " + tag.getZ()
                );
              }
            });

    // Mostra informações da primeira tag
    driver.b()
            .onTrue(() -> {

              if (!vision.hasTarget()) {
                return;
              }

              AprilTagDetection tag =
                      vision.getTarget();

              System.out.println("Primeira tag");

              System.out.println(
                      "ID = " + tag.getID()
              );

              System.out.println(
                      "Name = " + tag.getDisplayName()
              );

              System.out.println(
                      "X = " + tag.getX()
              );

              System.out.println(
                      "Y = " + tag.getY()
              );

              System.out.println(
                      "Z = " + tag.getZ()
              );
            });
  }
}