package com.aaronwillows.sensorKit.sensors;

/**
 * Specifies an interface for sensor data that contains temperature information.
 */
public interface ITemperatureData {

    /**
     *
     * @return gets a value representing temperature in degrees celsius
     */
    double getTemperature();

    /**
     *
     * @return gets a value representing temperature in degrees fahrenheit
     */
    double GetFahrenheitTemperature();

}
