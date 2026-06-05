# Logging Data

In this documentation we will teach you how to log a data. 
To make it easier to understand, Team Brazil has recorded a video explaining the whole process step by step. 
Video will be release on [Team Brazil YouTube]().

## Overview

Logging allows you to visualize what your robot is thinking in real-time, which is crucial for debugging and tuning.

### The `@AutoLog` Annotation (Recommended)

The easiest way to log subsystem data is by adding `@AutoLog` to your class.
Any `public` field in an `@AutoLog` class is automatically published to the FTC Dashboard and KoalaLog on every loop cycle. **No manual logging calls required!**

### Manual Logging

For one-off calculations or temporary tuning data inside your logic, use KoalaLog directly:

```java
KoalaLog.log("SystemName/VariableName", value, true);

```