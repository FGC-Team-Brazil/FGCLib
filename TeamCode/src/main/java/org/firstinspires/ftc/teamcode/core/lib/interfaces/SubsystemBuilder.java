package org.firstinspires.ftc.teamcode.core.lib.interfaces;

public interface SubsystemBuilder<T extends SubsystemBuilder<T>> extends Subsystem {
    T configure();
}
