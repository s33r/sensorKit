package com.aaronwillows.sensors;

import com.aaronwillows.HumitureData;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

import java.util.concurrent.TimeUnit;

public class HumitureSensor implements ISensor {
    private final int TIMING_MAX = 85;


    private boolean enabled = false;

    private int dataPinAddress;


    public HumitureSensor(int dataPinAddress) {
        this.dataPinAddress = dataPinAddress;

    }


    public void enable() {
        GpioUtil.export(dataPinAddress, GpioUtil.DIRECTION_OUT);

        enabled = true;
    }

    public void disable() {
        if (GpioUtil.isExported(dataPinAddress)) {
            GpioUtil.unexport(dataPinAddress);
        }

        enabled = false;
    }


    private int[] readData() throws Exception {
        int[] dataBuffer = new int[5];

        if (!enabled) {
            return dataBuffer;
        }

        Gpio.pinMode(dataPinAddress, Gpio.OUTPUT);
        Gpio.digitalWrite(dataPinAddress, Gpio.LOW);
        Gpio.delay(18);

        Gpio.digitalWrite(dataPinAddress, Gpio.HIGH);
        Gpio.delayMicroseconds(40);

        Gpio.pinMode(dataPinAddress, Gpio.INPUT);

        int lastState = Gpio.HIGH;
        int bit = 0;
        int counter, bufferOffset;

        for (int cycle = 0; cycle < TIMING_MAX; cycle++) {
            counter = 0;

            while (Gpio.digitalRead(dataPinAddress) == lastState) {
                counter++;
                Gpio.delayMicroseconds(1);

                if (counter == 255) {
                    break;
                }
            }

            lastState = Gpio.digitalRead(dataPinAddress);

            if (counter == 255) {
                break;
            }

            if (cycle >= 4 && (cycle % 2 == 0)) {
                bufferOffset = bit / 8;

                dataBuffer[bufferOffset] <<= 1;

                if (counter > 16) {
                    dataBuffer[bufferOffset] |= 1;
                }

                bit++;
            }
        }

        return dataBuffer;
    }

    private HumitureData parseData(int[] data) {
        HumitureData result = new HumitureData();
        result.setValid(false);

        if (data.length != 5) {
            return result;
        }

        boolean isValid = data[4] == ((data[0] + data[1] + data[2] + data[3]) & 0xFF);


        result.setValid(isValid);
        result.setHumidity(Double.parseDouble(Integer.toString(data[0]) + "." + data[1]));
        result.setTemperature(Double.parseDouble(Integer.toString(data[2]) + "." + data[3]));

        return result;
    }

    public HumitureData getData() throws Exception {
        TimeUnit.SECONDS.sleep(2);

        int[] data = readData();
        HumitureData result = parseData(data);

        return result;
    }
}
