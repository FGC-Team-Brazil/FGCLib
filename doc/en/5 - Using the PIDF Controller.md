# Using the PIDF Controller

In this documentation we'll teach you how to use Lib's PIDFController class.
Using it, you will be able to create your own PIDs for your subsystems.

# Table of Contents
- What is a PID controller?
    - Proportional
    - Integral
    - Derivative
    - FeedFoward
- PIDController class
    - How the class works
    - Creating the class object
    - Complementary methods

# What is a PID controller?
A PID controller is a controller for feedback systems. In robotics, we use PID to
control the operation of the robot's subsystems. The PID generates an output from two inputs:
- Setpoint: The final goal you want to achieve;
- Reference: The current progress/position towards the goal.
  One example is a PID for traction. Let's say your team wants to walk the robot 3m (setpoint)
  and your robot is currently at position 0m (reference). The PID will make the robot move forward, until it walks 3m.
  The output of the PID will vary according to the variation in the robot's position (reference), but the setpoint will always be the same.

The PID equation consists of 3 constants: proportional, integral and derivative. Each one has a different function.

## Proportional
Varies the PID output proportionally to the reference. Example: if the reference is 2, the proportional output will be 0.5,
when the reference goes up to 4, the proportional output will be 0.25 (indirectly proportional to the value of the reference)

## Integral
The integral constant works by adding up the PID error (difference between setpoint and reference) and multiplying this by the
constant. But why? Well, the proportional constant alone does not have the capacity to bring the robot to the setpoint,
think, if it is proportional to the reference, when the robot is very close to the setpoint, won't the proportional be
almost 0? This makes the robot stand still

To solve this problem, the integral allows the robot to move even with a very small error, because it adds up to
it over time, until it is a significant value that makes the robot move.

## Derivative
The derivative constant acts by predicting the robot's movement, to avoid future errors. It is proportional to the
derivative of the error. Unlike the other constants, its operation is more complex.

## FeedFoward
In addition to the common PID, the PIDController class also has a simple FeedFoward constant. It is basically
a minimum value for the output, to prevent the output from being less than the system's friction with the medium.

# PIDController class
The PIDController class implements PID and FeedForward in one equation. It also has some functions
to control the operation of the PID, such as a tolerance value for the setpoint.

## Class Operation
#### Import
````java
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;
````
#### Object creation
````java
PIDController pidController = new PIDController(kP, kI, kD, kF);
````
#### Setting the Setpoint
````java
pidController.setSetpoint(setPoint);
````
### Using the class
The calculate() method returns the output of the PID, so place it as a parameter of a setPower function, for example:
````java
setPower(pidController.calculate(reference));
````
## Complementary methods
In addition to the standard functionality, the class has some complementary methods, such as Set and Get methods,
a method to set the position tolerance, among others. Explore Lib to see all the methods.