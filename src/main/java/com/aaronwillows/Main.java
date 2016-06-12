package com.aaronwillows;

import com.aaronwillows.sensors.*;
import com.pi4j.system.SystemInfo;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        com.pi4j.wiringpi.Gpio.wiringPiSetup();

        System.out.println("Board: " + SystemInfo.getBoardType().toString());

        testIrObstacle();
    }

    private static void testLcd1602() {
        Lcd1602 lcd = new Lcd1602();
        lcd.enable();




        lcd.disable();
    }

    private static void testIrObstacle() throws Exception {
        IrObstacleSensor sensor = new IrObstacleSensor(0);
        sensor.enable();

        TimeUnit.SECONDS.sleep(30);

        sensor.disable();
    }

    private static void testHumitureSensor() throws Exception {
        HumitureSensor sensor = new HumitureSensor(0);

        sensor.enable();

        for (int j = 0; j < 50; j++) {
            HumitureData data = sensor.getData();

            String isValid = "invalid";

            if (data.isValid()) {
                isValid = "  valid";
            }

            System.out.println("[" + isValid + "] Temperature: " + data.GetFahrenheitTemperature() + ", Humidity: " + data.getHumidity());
        }

        TimeUnit.SECONDS.sleep(30);
        sensor.disable();
    }

    private static void testRgbLed() throws Exception {
        RgbLed dualColorLed = new RgbLed(0, 2, 3);

        dualColorLed.enable();

        boolean toggle = false;
        for (int j = 0; j < 15; j++) {
            if (toggle) {
                dualColorLed.setColor(0, 100, 0);
            } else {
                dualColorLed.setColor(100, 0, 0);
            }

            toggle = !toggle;
        }

        dualColorLed.setColor(100, 100, 100);

        TimeUnit.SECONDS.sleep(30);

        dualColorLed.disable();
    }
}
