package com.aaronwillows;

import com.aaronwillows.sensorKit.sensors.IAltitudeData;
import com.aaronwillows.sensorKit.sensors.IHumidityData;
import com.aaronwillows.sensorKit.sensors.IPressureData;
import com.aaronwillows.sensorKit.sensors.ITemperatureData;

public class WeatherData implements ITemperatureData, IPressureData, IAltitudeData, IHumidityData {
    public static final double SEA_LEVEL_PRESSURE = 101325;

    private double temperature;
    private int pressure;
    private double humidity;

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

    public double getHumidity() {
        return humidity;
    }


    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }


    @Override
    public String toString() {
        return "[T=" + getTemperature() + "°c][P=" + getPressure() + "pa][H=" + getHumidity() + "][A=" + getAltitude() + "]";
    }
}
