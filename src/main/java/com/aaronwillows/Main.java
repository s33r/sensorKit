package com.aaronwillows;


import com.aaronwillows.devices.BMP180.Barometer;
import com.aaronwillows.devices.BMP180.BarometerData;
import com.aaronwillows.devices.DHT11.HumitureData;
import com.aaronwillows.devices.DHT11.HumitureSensor;
import com.aaronwillows.devices.IrObstacleSensor;
import com.aaronwillows.devices.Lcd1602;
import com.aaronwillows.devices.PiCamera2;
import com.aaronwillows.devices.RgbLed;
import com.pi4j.system.SystemInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        com.pi4j.wiringpi.Gpio.wiringPiSetup();


        System.out.println("Board: " + SystemInfo.getBoardType().toString());

        lcdBarometerTest();
    }

    private static void lcdBarometerTest() throws Exception {
        Barometer sensor = new Barometer();
        Lcd1602 lcd = new Lcd1602();

        sensor.enable();
        lcd.enable();

        lcd.clear();

        while (true) {
            BarometerData data = sensor.getData();

            String line1 = "T: " + data.getTemperature() + "c";
            String line2 = "P: " + data.getPressure() + "pa";

            System.out.println("-- -- -- -- --");
            System.out.println(line1);
            System.out.println(line2);

            lcd.write(line1, 0, 0);
            lcd.write(line2, 0, 1);

            TimeUnit.SECONDS.sleep(2);
        }
    }

    private static void barometerTest() throws Exception {
        Barometer sensor = new Barometer();
        sensor.enable();

        while (true) {
            BarometerData data = sensor.getData();

//            System.out.println(data.getTemperature());

            TimeUnit.SECONDS.sleep(2);
        }

//        sensor.disable();
    }

    private static void cameraTest() throws Exception {
        PiCamera2 camera = new PiCamera2();
        camera.enable();

        camera.noPreview()
                .raw();


        BufferedImage image = camera.toStream();
        ImageIO.write(image, "png", new File("test2.jpg"));


        camera.disable();
    }

    private static void humitureLcdTest() throws Exception {
        Lcd1602 lcd = new Lcd1602();
        lcd.enable();

        lcd.write("T: ", 0, 0);
        lcd.write("H: ", 0, 1);

        HumitureSensor sensor = new HumitureSensor(0);
        sensor.enable();

        while (true) {
            HumitureData data = sensor.getData();

            String isValid = "(i) ";

            if (data.isValid()) {
                isValid = "(v) ";
            }

            lcd.write(isValid + Double.toString(data.getTemperature()), 3, 0, false);
            lcd.write(isValid + Double.toString(data.getHumidity()), 3, 1, false);
        }


    }

    private static void testLcd1602() {
        String dataString = "Hello World - This is a really long string. There this should wrap and just overflow. This last sentence is just in case";

        Lcd1602 lcd = new Lcd1602();
        lcd.enable();

        lcd.write(dataString, 0, 0);

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
