package com.aaronwillows.sensors;

/**
 * Specifies an interface for sensor data that contains humidity information.
 */
public interface IHumidityData {

    /**
     * @return gets a value representing % relative humidity.
     */
    double getHumidity();
}
