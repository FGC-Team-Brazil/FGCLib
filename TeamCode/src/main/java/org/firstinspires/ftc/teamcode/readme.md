# FGCLib — TeamCode Module

Welcome to **FGCLib**, a lightweight command-based framework designed for **FIRST Tech Challenge (FTC)** and **FIRST Global Challenge (FGC)** robots.

FGCLib helps teams write cleaner, more organized, and more scalable robot code through a structured architecture inspired by command-based paradigms — without the overhead of full frameworks.

---

## Features

- **Subsystem-based architecture** — encapsulate each robot mechanism into its own class
- **RobotContainer** — centralized control configuration and binding management
- **Trigger-driven controls** — reactive, event-driven gamepad input via `SmartGamepad`
- **`@AutoLog` annotation** — automatic subsystem state publishing with zero boilerplate
- **Built-in PIDF controller** — with motion profiling and voltage compensation
- **KoalaLog integration** — structured subsystem logging, managed automatically by `RobotContainerInternal`
- **FTC Dashboard support** — real-time tuning and live diagnostics via `MultipleTelemetry`
- **HID Controller** — advanced controller feedback including rumble effects and LED indicators
- **Hardware abstraction helpers** — clean, centralized hardware configuration

---

## Project Structure

```text
TeamCode/
│
├── robot/
│   ├── Constants.java
│   ├── RobotContainer.java
│   └── TeleOpMode.java
│
├── robot/subsystems/
│   ├── Drivetrain.java
│   ├── Shooter.java
│   ├── Arm.java
│   └── ...
│
├── opmodes/
│   ├── AutonomousExample.java
│   └── ...
│
└── core/lib/
    └── (FGCLib internals)
```

---

## Getting Started

### Creating Your Own OpModes

The easiest way to get started is to copy a Sample OpMode and adapt it to your needs.

Sample OpModes are located inside the `FtcRobotController` module:

```
FtcRobotController/java/org.firstinspires.ftc.robotcontroller/external/samples
```

To copy a sample into your TeamCode module:

1. Locate the desired sample class in the **Project/Android** tree.
2. Right-click the sample class and select **Copy**.
3. Expand the `TeamCode/java` folder.
4. Right-click `org.firstinspires.ftc.teamcode` and select **Paste**.
5. Choose a meaningful class name starting with a capital letter.

### Enabling an OpMode

Each sample begins with annotations like:

```java
@TeleOp(name = "Template: Linear OpMode", group = "Linear Opmode")
@Disabled
```

- Change `name="..."` to set the label shown on the Driver Station.
- Use `group="..."` to organize OpModes in the list.
- **Delete or comment out `@Disabled`** to make the OpMode visible on the Driver Station.

### Sample Naming Conventions

| Prefix | Description |
|--------|-------------|
| `Basic` | Minimal skeleton to illustrate a specific OpMode structure |
| `Sensor` | Demonstrates reading and displaying a specific sensor |
| `Robot` | Assumes a two-motor differential drive; used for navigation concepts |
| `Concept` | Demonstrates a single function or concept; may not produce a drivable robot |

- **Sensor** class names: `Sensor - Company - Type`
- **Robot** class names: `Robot - Mode - Action - OpModeType`
- **Concept** class names: `Concept - Topic - OpModeType`

---

## Core Concepts

### Subsystems

Subsystems are the primary building blocks of the robot. Every mechanism — drivetrain, shooter, arm, elevator, intake, vision — should have its own `Subsystem` class.

Each subsystem follows the singleton pattern and implements the `Subsystem` interface. When combined with `@AutoLog` (see [AutoLog](#autolog)), the framework generates a logged variant automatically, and the singleton type becomes the generated `<ClassName>AutoLogged` class.

```java
package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.core.lib.logging.AutoLog;

@AutoLog
public class Shooter implements Subsystem {

    private static ShooterAutoLogged instance;
    private DcMotorEx motor;

    protected Shooter() {}

    public static synchronized ShooterAutoLogged getInstance() {
        if (instance == null) {
            instance = new ShooterAutoLogged();
        }
        return instance;
    }

    @Override
    public void initialize(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, "shooter");
    }

    @Override
    public void start() {}

    @Override
    public void execute() {}

    @Override
    public void stop() {
        motor.setPower(0);
    }

    public void runMotorPower(double power) {
        motor.setPower(power);
    }

    public void stopMotor() {
        motor.setPower(0);
    }
}
```

> **Note:** The singleton instance type is the generated `ShooterAutoLogged` — not `Shooter` directly. See [AutoLog](#autolog) for details.

---

### RobotContainer

`RobotContainer` is the central configuration class of the robot. It extends `RobotContainerInternal`, which automatically manages the full robot lifecycle — no manual setup required for the items below:

| Responsibility | Managed by `RobotContainerInternal` |
|---|---|
| `KoalaLog.setup(hardwareMap)` | ✅ called on `init()` |
| `KoalaLog.start()` | ✅ called on `start()` |
| `KoalaLog.stop()` | ✅ called on `stop()` |
| `Trigger.updateAll()` | ✅ called on every `loop()` cycle |
| `AutoLogManager.periodic()` | ✅ called on every `loop()` cycle |
| FTC Dashboard telemetry | ✅ forwarded automatically |

All subsystems passed to `super(...)` are registered and driven by this lifecycle.

```java
package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.core.lib.internal.RobotContainerInternal;
import org.firstinspires.ftc.teamcode.robot.subsystems.SubsystemExample;

/**
 * RobotContainer handles instance configuration. All subsystems listed in the
 * constructor super() call will be executed when the library lifecycle runs.
 */
public class RobotContainer extends RobotContainerInternal {

    private final SmartGamepad driver;
    private final SmartGamepad operator;

    private final SubsystemExample subsystemExample;
    private final DrivetrainBuilder drivetrain;

    public RobotContainer(Gamepad driver, Gamepad operator) {
        super(
            DrivetrainBuilder.getInstance(),
            SubsystemExample.getInstance()
            // Add more subsystems here
        );

        this.driver   = new SmartGamepad(driver);
        this.operator = new SmartGamepad(operator);

        drivetrain = DrivetrainBuilder.build(
            Constants.DrivetrainBuilderConstants.MOTOR_RIGHT,
            Constants.DrivetrainBuilderConstants.MOTOR_LEFT,
            Constants.DrivetrainBuilderConstants.MOTOR_RIGHT_INVERTED,
            Constants.DrivetrainBuilderConstants.MOTOR_LEFT_INVERTED
        );

        subsystemExample = SubsystemExample.getInstance();
        // Register additional subsystems here as well
    }

    @Override
    public void configureBindings() {

        // --- Driver ---
        driver
            .leftY()
            .or(driver.rightX())
            .whileTrue(() -> drivetrain.arcadeDrive(-driver.getLeftY(), driver.getRightX()))
            .onFalse(drivetrain::stop);

        // --- Operator ---
        operator.y().onTrue(() -> subsystemExample.setTargetAngle(90));
        operator.a().onTrue(() -> subsystemExample.setTargetAngle(0));

        // Stop when neither Y nor A is held
        operator.y().negate()
            .and(operator.a().negate())
            .onTrue(() -> subsystemExample.setPower(0));

        operator.start()
            .and(operator.back())
            .onTrue(subsystemExample::resetEncoders);

        // --- Sensor-driven Triggers ---
        new Trigger(subsystemExample::isLimitLeft).onTrue(subsystemExample::resetEncoders);
        new Trigger(subsystemExample::isLimitRight).onTrue(subsystemExample::resetEncoders);
    }
}
```

> **Tip:** Standalone `Trigger` objects — created directly from a boolean supplier — let you react to hardware events such as limit switches or sensor thresholds using the exact same API as gamepad buttons.

---

### SmartGamepad

`SmartGamepad` is a trigger-based wrapper over the standard FTC `Gamepad`. It replaces manual polling with a clean, reactive API.

Instead of:

```java
if (gamepad1.a) {
    // ...
}
```

Use:

```java
driver.a().onTrue(...);
```

#### Available Button Triggers

```java
driver.a()             driver.b()
driver.x()             driver.y()

driver.dpadUp()        driver.dpadDown()
driver.dpadLeft()      driver.dpadRight()

driver.leftBumper()    driver.rightBumper()
driver.leftStick()     driver.rightStick()

driver.start()         driver.back()
driver.guide()

driver.leftTrigger()   driver.rightTrigger()
```

#### Available Axis Triggers

```java
driver.leftX()    driver.leftY()
driver.rightX()   driver.rightY()
```

#### Combining Triggers

Triggers can be composed for more advanced logic:

```java
// Both buttons pressed simultaneously
driver.start()
    .and(driver.back())
    .onTrue(...);

// Button A NOT held while B is pressed
driver.a()
    .negate()
    .and(driver.b())
    .onTrue(...);

// Either axis input triggers continuous drive
driver.leftY()
    .or(driver.rightX())
    .whileTrue(...);

// Action fires when neither Y nor A is held
operator.y().negate()
    .and(operator.a().negate())
    .onTrue(() -> subsystem.setPower(0));
```

#### Standalone Triggers

`Trigger` can also wrap any boolean supplier — hardware events, sensor readings, or computed conditions — and supports the same action API as gamepad triggers:

```java
new Trigger(subsystem::isLimitLeft).onTrue(subsystem::resetEncoders);
new Trigger(subsystem::isAtTarget).onTrue(subsystem::lock);
```

#### Trigger Actions

| Action | Behavior |
|--------|----------|
| `onTrue(Runnable)` | Runs **once** when the condition becomes `true` |
| `onFalse(Runnable)` | Runs **once** when the condition becomes `false` |
| `whileTrue(Runnable)` | Runs **continuously** while the condition is `true` |
| `whileFalse(Runnable)` | Runs **continuously** while the condition is `false` |

---

### TeleOpMode

The main `TeleOpMode` class is only responsible for bridging the FTC lifecycle into `RobotContainer`:

```java
package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp", group = "Official TeleOp")
public class TeleOpMode extends OpMode {

    private RobotContainer robot;

    @Override
    public void init() {
        robot = new RobotContainer(gamepad1, gamepad2);
        robot.init(hardwareMap, telemetry);
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {
        robot.loop();
    }

    @Override
    public void stop() {
        robot.stop();
    }
}
```

---

### Constants

Hardware names and configuration values should be centralized in `Constants.java` to avoid hardcoded strings spread across the codebase:

```java
package org.firstinspires.ftc.teamcode.robot;

public class Constants {

    public static class DrivetrainBuilderConstants {
        public static final String  MOTOR_RIGHT          = "right_drive";
        public static final String  MOTOR_LEFT           = "left_drive";
        public static final boolean MOTOR_RIGHT_INVERTED = false;
        public static final boolean MOTOR_LEFT_INVERTED  = true;
    }

    public static class Shooter {
        public static final String MOTOR_1_NAME = "shooter_motor_up";
    }
}
```

---

## Advanced Features

### AutoLog

FGCLib supports automatic subsystem state publishing through the `@AutoLog` annotation. It is the recommended way to add observability to any subsystem.

When a class is annotated with `@AutoLog`, the annotation processor generates a concrete implementation named `<ClassName>AutoLogged`. This generated class automatically publishes all logged fields to **KoalaLog** and **FTC Dashboard** on every loop cycle, with no additional boilerplate.

#### Defining an Auto-Logged Subsystem

```java
import org.firstinspires.ftc.teamcode.core.lib.logging.AutoLog;

@AutoLog
public class Shooter implements Subsystem {

    public double motorPower = 0.0;
    public boolean isRunning = false;

    // ... rest of the subsystem implementation
}
```

#### Using the Generated Class

Because `@AutoLog` generates `ShooterAutoLogged`, the singleton must use the generated type:

```java
private static ShooterAutoLogged instance;

public static synchronized ShooterAutoLogged getInstance() {
    if (instance == null) {
        instance = new ShooterAutoLogged();
    }
    return instance;
}
```

#### Automatic Lifecycle Integration

`RobotContainerInternal` calls `AutoLogManager.periodic()` on every loop cycle automatically. No additional setup is needed — all public fields in `@AutoLog`-annotated subsystems are published without manual calls.

> Prefer `@AutoLog` over manual `KoalaLog.log(...)` calls for subsystem state. Reserve manual logging for one-off or dynamic values:

```java
KoalaLog.log("Shooter/MotorPower", motor.getPower(), true);
```

---

### PIDF Controller

FGCLib includes a built-in PIDF controller for closed-loop mechanism control.

#### Basic Position Control

```java
PIDController pid = new PIDController(kP, kI, kD, kF);

double power = pid.calculate(targetPosition, motor.getCurrentPosition());
motor.setPower(power);
```

#### Motion Profile

For smoother, acceleration-limited movement:

```java
pid.enableMotionProfile(maxVelocity, maxAcceleration);
```

Useful for: linear slides, arms, elevators, and rotating mechanisms.

#### Voltage Compensation

Maintains consistent behavior as battery voltage drops throughout a match:

```java
pid.enableVoltageCompensation(hardwareMap);
```

#### Velocity Control

For flywheels, shooters, and launchers:

```java
pid.runVelocity(motor, targetTicksPerSecond);
```

---

### FTC Dashboard

FGCLib supports **FTC Dashboard** and **MultipleTelemetry** out of the box. Telemetry is forwarded automatically to both the **Driver Station** and the **Dashboard** — no additional configuration required. This enables:

- Real-time PIDF gain tuning
- Live subsystem diagnostics
- Sensor data graphing during runs

---

### HID Controller

`HIDController` provides access to advanced controller feedback — rumble patterns and LED indicators. Obtain the instance from any `SmartGamepad` via `getHID()`.

#### Rumble

```java
// Single rumble for a fixed duration (milliseconds)
driver.getHID().rumble(500);

// Continuous rumble until explicitly stopped
driver.getHID().rumbleContinuous();

// Short pulses repeated N times
driver.getHID().rumbleBlips(3);

// Custom programmatic rumble effect
driver.getHID().runRumbleEffect(effect);
```

#### LED

```java
// Set a solid LED color
driver.getHID().setLed(HIDController.LedColor.GREEN);

// Run an animated LED effect
driver.getHID().runLedEffect(effect);
```

#### Common Use Cases

| Situation | Suggested Feedback |
|---|---|
| Scoring confirmation | `rumble(300)` + `setLed(GREEN)` |
| Mechanism at limit | `rumbleBlips(2)` |
| Low battery / critical warning | `rumbleContinuous()` + `setLed(RED)` |
| Mode or state transition | `setLed(BLUE)` |

---

## Robot Lifecycle

The full execution flow from startup to shutdown, including all operations managed automatically by `RobotContainerInternal`:

```
TeleOpMode.init()
    └── RobotContainer created
    └── KoalaLog.setup(hardwareMap)       ← automatic
    └── Subsystem.initialize()            ← hardware mapped here

TeleOpMode.start()
    └── KoalaLog.start()                  ← automatic
    └── Subsystem.start()

TeleOpMode.loop()  [runs every cycle]
    └── Trigger.updateAll()               ← evaluates all SmartGamepad bindings
    └── AutoLogManager.periodic()         ← publishes all @AutoLog fields
    └── Subsystem.execute()               ← subsystem periodic logic

TeleOpMode.stop()
    └── KoalaLog.stop()                   ← automatic
    └── Subsystem.stop()
```

---

## Advanced: Multi-Team Configuration

> **Warning:** This is not recommended for inexperienced developers. Make a **full project backup** before starting, and close Android Studio before making any file-system changes.

If your club has multiple teams sharing a common codebase, you can clone the `TeamCode` module once per team so that each team maintains its own module while remaining able to view the others.

### Steps to Clone TeamCode

> Note: Some names start with `"Team"` (capital T) and others with `"team"` (lowercase). This is intentional.

1. Using your OS file manager, copy the entire `TeamCode/` folder to a sibling folder with a new name (e.g., `Team0417/`).

2. Inside `Team0417/`, **delete** the `TeamCode.iml` file.

3. Rename the source folder from:

   ```
   src/main/java/org/firstinspires/ftc/teamcode
   ```

   to a matching name with a **lowercase** `team`, e.g.:

   ```
   src/main/java/org/firstinspires/ftc/team0417
   ```

4. Edit `Team0417/src/main/AndroidManifest.xml` and change:

   ```xml
   package="org.firstinspires.ftc.teamcode"
   ```

   to:

   ```xml
   package="org.firstinspires.ftc.team0417"
   ```

5. Add the following line to your root `settings.gradle`:

   ```groovy
   include ':Team0417'
   ```

6. Open Android Studio and run **Build → Clean Project** to clear any stale build files.

Each team module will appear alongside the others in the Android Studio module list. Select the desired module from the **Run Configuration** dropdown before clicking the green **Run** arrow.

---

## License

This project is maintained by the team. Refer to the project root for license details.