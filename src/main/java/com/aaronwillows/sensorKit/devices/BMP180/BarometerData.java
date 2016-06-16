package com.aaronwillows.sensorKit.devices.BMP180;

import com.aaronwillows.sensorKit.sensors.IAltitudeData;
import com.aaronwillows.sensorKit.sensors.IPressureData;
import com.aaronwillows.sensorKit.sensors.ITemperatureData;


public class BarometerData implements ITemperatureData, IPressureData, IAltitudeData {
    public static final double SEA_LEVEL_PRESSURE = 101325;

    private double temperature;
    private int pressure;

    public double getTemperature() {
        return temperature;
    }

    public double GetFahrenheitTemperature() {
        return getTemperature() * 1.8f + 32;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getAltitude() {
        double d = (1 - Math.pow(getPressure() / SEA_LEVEL_PRESSURE, 0.1902949571836346));

        return 44330 / d;
    }

}
