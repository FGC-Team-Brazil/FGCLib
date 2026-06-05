# SmartGamePad Utility Class

In this documentation we will teach you how to use SmartGamePad, the class that controls access to the robot's gamepads/controls. 
To make it easier to understand, Team Brazil has recorded a video explaining the whole process step by step. 
Video will be release on [Team Brazil YouTube]().

## Overview

`SmartGamepad` replaces traditional `if/else` button polling with a modern, reactive "Trigger" system. It allows you to bind robot actions directly to button states and provides advanced feedback.

### Key Features

* **Event-Driven Triggers:** Bind actions to specific states using methods like `.onTrue()`, `.onFalse()`, `.whileTrue()`, and `.whileFalse()`.
* **Combinations:** Combine inputs logically using `.and()`, `.or()`, and `.negate()`.
* **HID Controller:** Access rumble and LED feedback via `getHID()`.

**Example:**

```java
// Run intake while 'A' is held, stop when released
driver.a().whileTrue(intake::run).onFalse(intake::stop);

// Rumble the controller for 500ms
driver.getHID().rumble(500);

```