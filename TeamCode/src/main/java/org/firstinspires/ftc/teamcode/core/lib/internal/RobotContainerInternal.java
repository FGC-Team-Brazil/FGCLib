package org.firstinspires.ftc.teamcode.core.lib.internal;

import Ori.Coval.Logging.AutoLogManager;
import Ori.Coval.Logging.Logger.KoalaLog;
import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;

/**
 * Base container responsible for managing the robot lifecycle.
 *
 * <p>This class centralizes subsystem initialization, execution, shutdown, trigger processing,
 * telemetry forwarding, and logging integration. User-defined robot containers should extend this
 * class and override {@link #configureBindings()} to register controller bindings and triggers.
 */
public class RobotContainerInternal {

  private Telemetry telemetry;

  private final List<Subsystem> subsystems;

  /**
   * Creates a new container with the provided subsystems.
   *
   * @param subsystems subsystems that will participate in the robot lifecycle
   */
  public RobotContainerInternal(Subsystem... subsystems) {
    this.subsystems = List.of(subsystems);
  }

  /**
   * Initializes all registered subsystems and prepares telemetry, triggers, and logging services.
   *
   * @param hardwareMap FTC hardware map used for device initialization
   * @param telemetry telemetry instance used to report robot data
   */
  public void init(@NonNull HardwareMap hardwareMap, @NonNull Telemetry telemetry) {
    this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    subsystems.forEach(subsystem -> subsystem.initialize(hardwareMap));

    Trigger.clearAll();
    KoalaLog.setup(hardwareMap);
  }

  /**
   * Starts all registered subsystems and configures operator bindings.
   *
   * <p>This method should be called once when the OpMode transitions from initialization to
   * execution.
   */
  public void start() {
    subsystems.forEach(Subsystem::start);

    telemetry.update();
    configureBindings();

    KoalaLog.start();
  }

  /**
   * Executes one iteration of the robot control loop.
   *
   * <p>Updates trigger states, executes all registered subsystems, refreshes telemetry, and
   * processes automatic logging.
   */
  public void loop() {
    Trigger.updateAll();

    subsystems.forEach(Subsystem::execute);

    telemetry.update();
    AutoLogManager.periodic();
  }

  /**
   * Stops all registered subsystems and finalizes active logging sessions.
   *
   * <p>This method should be called when the OpMode is terminated to ensure motors, actuators, and
   * services are properly shut down.
   */
  public void stop() {
    subsystems.forEach(Subsystem::stop);

    telemetry.update();
    KoalaLog.stop();
  }

  /**
   * Registers controller bindings, triggers, and robot actions.
   *
   * <p>Override this method in the user robot container to configure button mappings and
   * event-driven behaviors.
   */
  protected void configureBindings() {}
}
