package com.aaronwillows;

import com.aaronwillows.sensorKit.IDevice;
import com.aaronwillows.sensorKit.devices.BMP180.Barometer;
import com.aaronwillows.sensorKit.devices.BMP180.BarometerData;
import com.aaronwillows.sensorKit.devices.DHT11.HumitureData;
import com.aaronwillows.sensorKit.devices.DHT11.HumitureSensor;
import com.aaronwillows.sensorKit.devices.Lcd1602;
import com.aaronwillows.sensorKit.sensors.*;

public class WeatherStation implements IDevice, ITemperatureSource, IAltitudeSource, IPressureSource, IHumiditySource {

    private boolean enabled;

    private Barometer barometer;
    private HumitureSensor humiditySensor;
    private ILcd lcd;


    public WeatherStation() {
        lcd = new Lcd1602();
        humiditySensor = new HumitureSensor(0);
        barometer = new Barometer();
    }


    private WeatherData collectData() {
        BarometerData barometerData = barometer.getData();
        HumitureData humitureData = humiditySensor.getData();


        WeatherData data = new WeatherData();

        data.setTemperature(barometerData.getTemperature());
        data.setPressure(barometerData.getPressure());
        data.setHumidity(humitureData.getHumidity());

        return data;
    }

    private void outputData(WeatherData data) {
        lcd.write("T=" + data.getTemperature(), 0);
        lcd.write("P=" + data.getPressure(), 1);

    }

    public WeatherData getData() {
        WeatherData data = collectData();

        outputData(data);


        return data;
    }

    @Override
    public void enable() {
        if(enabled) {
            return;
        }

        lcd.enable();
        barometer.enable();
        humiditySensor.enable();

        lcd.clear();

        enabled = true;
    }

    @Override
    public void disable() {
        if(!enabled) {
            return;
        }

        lcd.clear();

        humiditySensor.disable();
        barometer.disable();
        lcd.disable();

        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
