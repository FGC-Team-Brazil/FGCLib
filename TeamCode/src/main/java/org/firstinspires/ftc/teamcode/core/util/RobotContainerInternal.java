package org.firstinspires.ftc.teamcode.core.util;

import androidx.annotation.NonNull;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;

public class RobotContainerInternal {

  private final SmartGamepad driver;
  private final SmartGamepad operator;

  private Telemetry telemetry;

  private final List<Subsystem> subsystems;

  public RobotContainerInternal(Gamepad driver, Gamepad operator, Subsystem... subsystems) {
    this.driver = new SmartGamepad(driver);
    this.operator = new SmartGamepad(operator);
    this.subsystems = List.of(subsystems);
  }

  /**
   * Run the init method from all subsystems
   *
   * @param hardwareMap
   * @param telemetry
   */
  public void init(@NonNull HardwareMap hardwareMap, @NonNull Telemetry telemetry) {
    this.telemetry = telemetry;

    subsystems.forEach(subsystem -> subsystem.initialize(hardwareMap, telemetry));

    telemetry.update();
  }

  /** Run the start method from all subsystems */
  public void start() {
    subsystems.forEach(Subsystem::start);
    telemetry.update();
    configureBindings();
  }

  /** Run the loop method from all subsystems */
  public void loop() {
    subsystems.forEach(Subsystem::execute);
    telemetry.update();
    driver.update();
    operator.update();
  }

  /** Run the stop method from all subsystems */
  public void stop() {
    subsystems.forEach(Subsystem::stop);
    telemetry.update();
  }

  protected void configureBindings() {}

  protected SmartGamepad getDriver() {
    return driver;
  }

  protected SmartGamepad getOperator() {
    return operator;
  }
}
