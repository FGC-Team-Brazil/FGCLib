# Measuring a Periodic (PeriodicTimer)

Sometimes, you need to know exactly how long a specific piece of code takes to execute to ensure your robot isn't experiencing lag.
Team Brazil has recorded a video explaining the whole process step by step.
Video will be release on [Team Brazil YouTube]().

## Overview

The `PeriodicTimer` utility class allows you to track execution times of periodic tasks. It measures how many milliseconds a process takes and automatically logs the statistics (Last Ms, Average Ms, Max Ms, and Overruns) directly to **KoalaLog**.

### How to Use PeriodicTimer

To measure a block of code, simply wrap it with the `start()` and `stop()` methods, passing a unique name for that timer.

```java
import org.firstinspires.ftc.teamcode.core.util.PeriodicTimer;

// Start the timer before your complex logic
PeriodicTimer.start("VisionProcessing");

// ... your heavy code here ...

// Stop the timer when finished
PeriodicTimer.stop("VisionProcessing");

```

### What gets logged?

By calling `stop()`, the utility calculates the elapsed time and automatically updates KoalaLog with:

* `Timers/VisionProcessing/LastMs`: The time the last loop took.
* `Timers/VisionProcessing/AvgMs`: The average time across all loops.
* `Timers/VisionProcessing/MaxMs`: The absolute longest time the loop took.
* `Timers/VisionProcessing/Overruns`: How many times the loop exceeded the **20.0ms** threshold.