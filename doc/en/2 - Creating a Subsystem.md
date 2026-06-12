# Creating a Subsystem

In this documentation you will find the necessary steps to create your own subsystem using FGCLib's functionalities. 
To make it easier to understand, Team Brazil has recorded a video explaining the whole process step by step. 
Video will be release on [Team Brazil YouTube](https://www.youtube.com/watch?v=uC5V-J-20h8).

## Overview

A subsystem encapsulates a specific physical mechanism of your robot (e.g., Drivetrain, Shooter, Intake).

### Key Steps to Create a Subsystem:

1. **Implement the Interface:** Your class must implement the `Subsystem` interface, which requires methods like `initialize()`, `start()`, `execute()`, and `stop()`.
2. **Use the Singleton Pattern:** Ensure only one instance of your subsystem exists by making the constructor `protected` and providing a `getInstance()` method.
3. **Add @AutoLog:** Add the `@AutoLog` annotation above your class definition. This automatically logs your public variables to the FTC Dashboard and KoalaLog without extra boilerplate.
4. **Register in RobotContainer:** Pass your subsystem's instance into the `super()` call inside your `RobotContainer` constructor so the framework manages its lifecycle automatically.