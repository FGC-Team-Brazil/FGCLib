# Introduction to FGCLib

Welcome to FGCLib, a library for FGC and FTC that automates some essential tasks (boilerplate)
for programming the robot, as well as providing a standardized and easy-to-understand code structure.
To prepare your programming environment, access this [video](https://www.youtube.com/watch?v=_te1nUU-av4) and [video](https://www.youtube.com/watch?v=WDku3kcykPw).

## Library structure
The standard Lib structure is very similar to any standard FGC code. To access the code, simply navigate to
``TeamCode/src/main/java/org/firstinspires/ftc/teamcode``.

**Terminal:**

        cd TeamCode/src/main/java/org/firstinspires/ftc/teamcode

In this directory, you will find two folders:
1. **core**: directory containing the library's auxiliary classes, such as PIDF and SmartGamePad,
   It is recommended that nothing in this folder is changed, because as the name suggests, it is part of the "core" of the library;
2. **robot**: directory containing the code for the robot itself, this is where the team will create the robot's systems and its opmodes.

### Robot folder
The library will separate the robot's code into 3 parts, which are:

#### Subsystems
This is where all the robot's systems will be created, such as traction, claws, elevators, etc. Everything the robot is capable of doing will be created here, with each subsystem being a different class. All subsystems implement the Subsystem interface.

Once the subsystems have been created (subsystems directory), all the systems created must be listed in the RobotSubsystems class, which is responsible for running them.

See our documentation [Creating a Subsystem](2%20-%20Creating%20a%20Subsystem.md) for more details on how to create your own subsystems.

#### Constants
This folder contains all the code constants, i.e. immutable values that the systems will need. Each subsystem will have a constants file, which by default should be named like this:
``` js
SubsystemName + Constants // Ex: DrivetrainConstants
```

In addition to the constants for each subsystem, the folder also has a class called GlobalConstants, which will store global constants, i.e. those that more than one subsystem can use.

#### OpModes
Finally, all the robot's systems will run in the robot's operational modes. The main Teleoperated mode must be created in TeleOpMode.java, the rest of the operational modes will be created in the opmodes part. All the operational modes are documented, go to the class and see the documentation and how to create your own Opmode.

### Core
The library's functionalities are divided into 2 parts:

#### Utils
Within the utils folder we have utility classes of various uses, designed to automate some functions that are normally used within the code. Currently, the library has the class:
MathUtils.java. It includes mathematical functions that help to produce code.
PeriodicTimer.java. This can measure the time of the periodic helping to find loop overflows.
