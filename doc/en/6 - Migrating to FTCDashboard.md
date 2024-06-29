# Migrating to FTCDashboard

In this documentation we'll teach you how to use FtcDashboard instead of the SDK's standard Telemetry.
To make it easier to understand, Team Brazil has recorded a video explaining the whole process step by step. Access the video [here]().

## Using MultipleTelemetry
When using standard Telemetry with FGCLib, the Telemetry object is passed to the
subsystems via the Robot class:
````java
// Inside the Robot.java Class

// ...
public void init(@NonNull HardwareMap hardwareMap, @NonNull **Telemetry telemetry**) {
        this.telemetry = telemetry;
        this.subsystems = RobotSubsystems.get();

        subsystems.forEach(subsystem -> subsystem.initialize(hardwareMap, **telemetry**));

        telemetry.update();
    }
// ...
````

But to use FtcDashboard, we're going to use the library's own Telemetry. To use it, in your
subsystem, in the initialize() method, instead of passing the telemetry parameter from initialize
to the telemetry variable, use the MultipleTelemetry class from FtcDashboard:
### Default Telemetry Example
````java
public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
    // ...
    
    this.telemetry = telemetry;
    
    // ...
    telemetry.addData(...);
````

### MultipleTelemetry FtcDashboard example
````java
public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
    // ...

    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    
    // ...
    telemetry.addData(...);
````

## Learning how to use FtcDashboard
If you don't know how FtcDashboard works, take a look at the library's documentation.
It is very complete, detailing the operation of the classes used and includes examples of their
examples. See the documentation [here](https://acmerobotics.github.io/ftc-dashboard/).
Access the library's Github [here](https://github.com/acmerobotics/ftc-dashboard).