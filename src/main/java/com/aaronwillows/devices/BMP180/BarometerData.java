package com.aaronwillows.devices.BMP180;

import com.aaronwillows.sensors.IPressureData;
import com.aaronwillows.sensors.ITemperatureData;


public class BarometerData implements ITemperatureData, IPressureData {
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
}
