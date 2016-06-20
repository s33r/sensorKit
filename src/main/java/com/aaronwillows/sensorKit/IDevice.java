package com.aaronwillows.sensorKit;

/**
 * Devices are any hardware like sensors or lcd displays.
 */
public interface IDevice {

    void enable();

    void disable();

    boolean isEnabled();
}
