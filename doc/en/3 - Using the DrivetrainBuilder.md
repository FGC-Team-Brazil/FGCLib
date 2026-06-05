# Using the DrivetrainBuilder

In this documentation we will teach you how to use DrivetrainBuilder, the class that helps you in creating a drivetrain tank system. 
To make it easier to understand, Team Brazil has recorded a video explaining the whole process step by step. 
Access the video [here]().

## Overview

The `DrivetrainBuilder` automates the creation of a standard differential (tank or arcade) drive base, saving you from writing repetitive motor configuration code.

### How to use it:

1. **Define Constants:** Set up your motor names and inversion states in your `Constants.java` file.
2. **Build the Instance:** Inside your `RobotContainer`, initialize the drivetrain by calling:

```java
DrivetrainBuilder.build(
    Constants.MOTOR_RIGHT, 
    Constants.MOTOR_LEFT, 
    Constants.MOTOR_RIGHT_INVERTED, 
    Constants.MOTOR_LEFT_INVERTED
);

```

3. **Bind Controls:** Use `SmartGamepad` triggers to link joystick axes directly to the builder's drive methods, like `arcadeDrive(speed, turn)`.