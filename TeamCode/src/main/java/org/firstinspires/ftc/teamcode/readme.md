# FGCLib — TeamCode Module

Welcome to **FGCLib**, a lightweight command-based framework designed for **FIRST Tech Challenge (FTC)** and **FIRST Global Challenge (FGC)** robots.

FGCLib helps teams write cleaner, more organized, and more scalable robot code through a structured architecture inspired by command-based paradigms — without the overhead of full frameworks.

---

## Features

- **Subsystem-based architecture** — encapsulate each robot mechanism into its own class
- **RobotContainer** — centralized control configuration and binding management
- **Trigger-driven controls** — reactive, event-driven gamepad input via `SmartGamepad`
- **Built-in PIDF controller** — with motion profiling and voltage compensation
- **KoalaLog integration** — automatic subsystem logging for telemetry and debugging
- **FTC Dashboard support** — real-time tuning and live diagnostics via `MultipleTelemetry`
- **HID Controller** — advanced controller feedback including rumble and LED indicators
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
@TeleOp(name="Template: Linear OpMode", group="Linear Opmode")
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

Each subsystem follows the singleton pattern and implements the `Subsystem` interface:

```java
package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.core.lib.interfaces.Subsystem;

public class Shooter implements Subsystem {

    private static Shooter instance;
    private DcMotorEx motor;

    protected Shooter() {}

    public static synchronized Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
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

---

### RobotContainer

`RobotContainer` is the central configuration class of the robot. It is responsible for:

- Registering all subsystems
- Configuring controller bindings
- Connecting commands to triggers
- Managing all robot input logic

```java
package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.core.lib.builders.DrivetrainBuilder;
import org.firstinspires.ftc.teamcode.core.lib.gamepad.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.lib.internal.RobotContainerInternal;
import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;

public class RobotContainer extends RobotContainerInternal {

    private final SmartGamepad driver;
    private final SmartGamepad operator;

    private final Shooter shooter;
    private final DrivetrainBuilder drivetrain;

    public RobotContainer(Gamepad driver, Gamepad operator) {
        super(
            DrivetrainBuilder.getInstance(),
            Shooter.getInstance()
        );

        this.driver = new SmartGamepad(driver);
        this.operator = new SmartGamepad(operator);

        drivetrain = DrivetrainBuilder.build(
            Constants.DrivetrainBuilderConstants.MOTOR_RIGHT,
            Constants.DrivetrainBuilderConstants.MOTOR_LEFT,
            Constants.DrivetrainBuilderConstants.MOTOR_RIGHT_INVERTED,
            Constants.DrivetrainBuilderConstants.MOTOR_LEFT_INVERTED
        );

        shooter = Shooter.getInstance();
    }

    @Override
    public void configureBindings() {
        driver
            .leftY()
            .or(driver.rightX())
            .whileTrue(() -> drivetrain.arcadeDrive(-driver.getLeftY(), driver.getRightX()))
            .onFalse(drivetrain::stop);

        operator.b()
            .onTrue(() -> shooter.runMotorPower(0.8))
            .onFalse(shooter::stopMotor);
    }
}
```

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
driver.a()            driver.b()
driver.x()            driver.y()

driver.dpadUp()       driver.dpadDown()
driver.dpadLeft()     driver.dpadRight()

driver.leftBumper()   driver.rightBumper()
driver.leftStick()    driver.rightStick()

driver.start()        driver.back()
driver.guide()

driver.leftTrigger()  driver.rightTrigger()
```

#### Available Axis Triggers

```java
driver.leftX()    driver.leftY()
driver.rightX()   driver.rightY()
```

#### Combining Triggers

Triggers can be composed for more advanced logic:

```java
// Both buttons pressed
driver.start()
    .and(driver.back())
    .onTrue(...);

// Button B pressed while A is not pressed
driver.a()
    .negate()
    .and(driver.b())
    .onTrue(...);

// Either condition triggers the action
driver.leftY()
    .or(driver.rightX())
    .whileTrue(...);
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

Hardware names and configuration values should be centralized in `Constants.java` to avoid hardcoded strings across the codebase:

```java
package org.firstinspires.ftc.teamcode.robot;

public class Constants {

    public static class DrivetrainBuilderConstants {
        public static final String MOTOR_RIGHT         = "right_drive";
        public static final String MOTOR_LEFT          = "left_drive";
        public static final boolean MOTOR_RIGHT_INVERTED = false;
        public static final boolean MOTOR_LEFT_INVERTED  = true;
    }
}
```

---

## Advanced Features

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

Maintains consistent behavior as battery voltage drops:

```java
pid.enableVoltageCompensation(hardwareMap);
```

#### Velocity Control

For flywheels, shooters, and launchers:

```java
pid.runVelocity(motor, targetTicksPerSecond);
```

---

### Logging with KoalaLog

FGCLib integrates with **KoalaLog** for structured subsystem logging:

```java
KoalaLog.log("Shooter/MotorPower", motor.getPower(), true);
```

Useful for:
- Monitoring subsystem state in real time
- Tuning mechanism parameters
- Tracking motor output during testing and competition

---

### FTC Dashboard Support

FGCLib supports **FTC Dashboard** and **MultipleTelemetry** out of the box.

Telemetry is forwarded automatically to both the **Driver Station** and the **Dashboard**, enabling:

- Real-time mechanism tuning (PIDF gains, setpoints)
- Live subsystem diagnostics
- Graphing of sensor data during runs

---

### HID Controller

`HIDController` provides access to advanced controller feedback features:

```java
// Rumble the controller for 500ms
driver.getHID().rumble(500);

// Set the controller LED to green
driver.getHID().setLed(HIDController.LedColor.GREEN);
```

Useful for:
- Driver feedback on game events
- Scoring or action confirmation
- Warning indicators (e.g., low battery, mechanism limit reached)
- State transition signals

---

## Robot Lifecycle

The full execution flow from startup to shutdown:

```
TeleOpMode.init()
    └── RobotContainer created
    └── Subsystem.initialize() ← hardware mapped here

TeleOpMode.start()
    └── Subsystem.start()

TeleOpMode.loop()  [runs every cycle]
    └── Trigger.updateAll()   ← evaluates all SmartGamepad bindings
    └── Subsystem.execute()   ← subsystem periodic logic

TeleOpMode.stop()
    └── Subsystem.stop()
```

This architecture keeps the robot modular, readable, and maintainable as the project grows.

---

## Advanced: Multi-Team Configuration

> **Warning:** This is not recommended for inexperienced developers. Make a **full project backup** before starting, and close Android Studio before making changes.

If your club has multiple teams sharing a common codebase, you can clone the `TeamCode` module — once per team — so each team maintains its own module while still being able to see the others.

### Steps to Clone TeamCode

> Note: Some names start with `"Team"` (capital T) and others with `"team"` (lowercase). This is intentional.

1. Using your OS file manager, copy the entire `TeamCode/` folder to a sibling folder with a new name (e.g., `Team0417/`).

2. Inside the new `Team0417/` folder, **delete** the `TeamCode.iml` file.

3. Rename the folder:
   ```
   src/main/java/org/firstinspires/ftc/teamcode
   ```
   to a matching name with a **lowercase** `team`, e.g.:
   ```
   src/main/java/org/firstinspires/ftc/team0417
   ```

4. Edit `Team0417/src/main/AndroidManifest.xml` — change:
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