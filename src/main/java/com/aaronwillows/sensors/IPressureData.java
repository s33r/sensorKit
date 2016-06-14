package com.aaronwillows.sensors;

/**
 * Specifies an interface for sensor data that contains pressure information.
 */
public interface IPressureData {

    /**
     *
     * @return gets a value representing pressure in pascals
     */
    int getPressure();
}
