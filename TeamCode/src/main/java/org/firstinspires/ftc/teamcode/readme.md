# Introduction to FGCLib

Welcome to **FGCLib**, a lightweight framework designed for FIRST Tech Challenge (FTC) and FIRST Global Challenge (FGC) robots.

FGCLib helps teams write cleaner, more organized, and highly scalable robot code. It automates essential boilerplate tasks, manages the robot's lifecycle automatically, and provides a modern, trigger-driven approach to programming. To prepare your programming environment, access this [video](https://www.youtube.com/watch?v=_te1nUU-av4).

## Key Features

* **Subsystem-based architecture:** Encapsulate mechanisms cleanly.
* **RobotContainer:** Centralized control configuration and automatic lifecycle management.
* **Reactive Controls:** Trigger-driven input (`onTrue`, `whileTrue`) via `SmartGamepad`.
* **Zero-Boilerplate Logging:** Automatic state publishing with the `@AutoLog` annotation.
* **Advanced Utilities:** Built-in PIDF controllers, FTC Dashboard support, and HID feedback (Rumble/LEDs).

---

## Library Structure

The standard Lib structure separates the framework backend from your team's active code. To access the code, navigate to:
`TeamCode/src/main/java/org/firstinspires/ftc/teamcode`.

**Terminal:**

```bash
cd TeamCode/src/main/java/org/firstinspires/ftc/teamcode

```

In this directory, you will find two main folders:

1. **core**: Directory containing the library's internal workings and auxiliary classes (such as PIDF, SmartGamepad, AutoLog processor). **It is recommended that nothing in this folder is changed**, as it is the foundation of the framework.
2. **robot & opmodes**: This is where your team will actually work. You will create your subsystems, configure your controls, and build your OpModes here.

---

### The Robot Folder

The library standardizes your robot's code into a few key components:

#### 1. Subsystems

This is where all the robot's physical mechanisms are created in code (e.g., drivetrain, shooter, elevator, intake). Everything the robot is capable of doing will be created here as a different class.

* All subsystems implement the `Subsystem` interface and follow a Singleton pattern.
* **@AutoLog:** By adding the `@AutoLog` annotation to your subsystem, FGCLib will automatically generate an `AutoLogged` version of your class that publishes its state to KoalaLog and FTC Dashboard automatically!

See our documentation [Creating a Subsystem](https://www.google.com/search?q=2%2520-%2520Creating%2520a%2520Subsystem.md) for more details.

#### 2. RobotContainer (The Core Hub)

The `RobotContainer` is the central nervous system of your robot. Instead of spreading your gamepad logic across multiple files, `RobotContainer` handles it all.

* It extends `RobotContainerInternal`, which means it **automatically** manages your subsystems' start/stop lifecycle, updates your gamepad triggers, and handles telemetry and logging every loop cycle.
* This is where you will bind your `SmartGamepad` buttons to your subsystem actions.

#### 3. Constants

This file/folder contains all the immutable values your systems will need (e.g., motor names, PIDF values, target positions). Centralizing hardware names in `Constants.java` avoids hardcoded strings spread across your codebase.

#### 4. OpModes & TeleOpMode

Finally, your robot needs OpModes to run on the Driver Station.

* **TeleOpMode:** The main teleoperated mode acts simply as a bridge. It initializes the `RobotContainer` and lets the framework handle the rest.
* **Autonomous:** Other autonomous OpModes will be created in the `opmodes` folder.

---

### Core Functionalities Highlights

The `core` directory does the heavy lifting so you don't have to. Here are the main tools you will use from it:

1. **SmartGamepad & Triggers:** Replaces traditional `if(gamepad.a)` polling with reactive triggers. You can easily bind actions using commands like `driver.a().onTrue(...)` or `driver.leftY().whileTrue(...)`. It also includes an `HIDController` to easily trigger controller rumbles and LEDs!
2. **Logging & Telemetry:** Managed automatically by `RobotContainerInternal`. Includes native integration with **KoalaLog** and **FTC Dashboard** for real-time tuning and diagnostics.
3. **PIDF & MathUtils:** Provides built-in PIDF controllers with advanced features like motion profiling, voltage compensation, and velocity control.
4. **Builders:** Utility classes like `DrivetrainBuilder` to automate the creation of complex base subsystems.
