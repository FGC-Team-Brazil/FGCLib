package org.firstinspires.ftc.teamcode.core.lib.interfaces;

/**
 * to-do document it later
 * @param <T>
 */
public interface SubsystemBuilder<T extends SubsystemBuilder<T>> extends Subsystem {
    T configure();
}
