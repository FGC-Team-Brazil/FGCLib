# Using the PIDF Controller

In this documentation we'll teach you how to use Lib's PIDFController class. 
Using it, you will be able to create your own PIDs for your subsystems.

## Table of Contents

* What is a PID controller?
* The Math Behind PIDF
* PIDController class
* Position & Velocity Control
* Advanced Features (Motion Profiling & Voltage Comp)


## What is a PID controller?

A PID controller is a system used to control the operation of the robot's mechanisms smoothly and accurately. The PID generates a motor output from two main inputs:

* **Setpoint:** The final goal you want to achieve (e.g., Target encoder ticks).
* **Reference:** The current progress/position towards the goal (e.g., Current encoder ticks).

If you want an elevator to reach a height of **1000 ticks**, the PID controller calculates the error (Setpoint - Reference) and adjusts motor power continuously to reach the target without violently overshooting or stopping too early.

### The Math Behind PIDF

* **Proportional ($k_P$):** Reacts proportionally to the current error. Large error = large power.
* **Integral ($k_I$):** Adds up past error over time to overcome steady-state resistance (like friction or gravity) when the robot is close to the target but stuck.
* **Derivative ($k_D$):** Predicts future error based on the rate of change, acting as a "brake" to dampen the movement and prevent overshoot.
* **FeedForward ($k_F$):** A base baseline power applied independently of error, useful for offsetting known forces (like holding an arm up against gravity).

## PIDController Class

FGCLib's `PIDController` implements all of these terms and adds powerful robotics-specific features.

### Creating the Object

```java
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;

// Create a PIDF controller
PIDController pid = new PIDController(kP, kI, kD, kF);

```

### Position vs. Velocity Control

* **Position Control:** Used for arms, slides, and driving to a point.
```java
double power = pid.calculate(targetPosition, motor.getCurrentPosition());
motor.setPower(power);

```


* **Velocity Control:** Used for shooters and flywheels to maintain a specific speed (ticks per second).
```java
pid.runVelocity(shooterMotor, targetTicksPerSecond);

```



### Advanced Features

FGCLib's PID includes features specifically designed to improve consistency:

| Feature | Description | Example Setup |
| --- | --- | --- |
| **Motion Profiling** | Generates a smooth trapezoidal path (limits max acceleration/velocity) to prevent mechanical shock on slides and arms. | `pid.enableMotionProfile(maxVel, maxAccel);` |
| **Voltage Comp.** | Scales motor output based on current battery voltage, keeping mechanisms consistent as the battery drains during a match. | `pid.enableVoltageCompensation(hardwareMap);` |
