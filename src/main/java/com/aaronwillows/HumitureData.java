package com.aaronwillows;

/**
 * Created by Administrator on 6/10/2016.
 */
public class HumitureData {

    private double temperature;
    private double humidity;
    private boolean valid;

    public double getTemperature() {
        return temperature;
    }

    public double GetFahrenheitTemperature() {
        return getTemperature() * 1.8f + 32;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }


    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
