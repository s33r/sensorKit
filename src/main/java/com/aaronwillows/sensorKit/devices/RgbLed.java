package com.aaronwillows.sensorKit.devices;

import com.aaronwillows.sensorKit.IDevice;
import com.aaronwillows.sensorKit.sensors.IColorSink;
import com.pi4j.wiringpi.SoftPwm;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 6/10/2016.
 */
public class RgbLed implements IDevice, IColorSink {

    public final int MINIMUM_INTENSITY = 0;
    public final int MAXIMUM_INTENSITY = 100;

    private boolean enabled = false;

    private int redPin;
    private int greenPin;
    private int bluePin;


    private int clampColor(int color) {
        if (color < MINIMUM_INTENSITY) {
            return MINIMUM_INTENSITY;
        }

        if (color > MAXIMUM_INTENSITY) {
            return MAXIMUM_INTENSITY;
        }

        return color;
    }

    public RgbLed(int redPin, int greenPin, int bluePin) {
        this.redPin = redPin;
        this.greenPin = greenPin;
        this.bluePin = bluePin;
    }

    public void enable() {
        SoftPwm.softPwmCreate(redPin, MINIMUM_INTENSITY, MAXIMUM_INTENSITY);
        SoftPwm.softPwmCreate(greenPin, MINIMUM_INTENSITY, MAXIMUM_INTENSITY);
        SoftPwm.softPwmCreate(bluePin, MINIMUM_INTENSITY, MAXIMUM_INTENSITY);

        enabled = true;
    }

    public void disable() {
        SoftPwm.softPwmStop(redPin);
        SoftPwm.softPwmStop(greenPin);
        SoftPwm.softPwmStop(bluePin);

        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }


    public int getGreenPin() {
        return greenPin;
    }

    public int getRedPin() {
        return redPin;
    }

    public int getBluePin() {
        return bluePin;
    }

    public void setColor(int red, int green, int blue) {
        if (!enabled) {
            return;
        }

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            SoftPwm.softPwmWrite(redPin, clampColor(red));

            TimeUnit.MILLISECONDS.sleep(500);
            SoftPwm.softPwmWrite(greenPin, clampColor(green));

            TimeUnit.MILLISECONDS.sleep(500);
            SoftPwm.softPwmWrite(bluePin, clampColor(blue));
        } catch(InterruptedException exception) {
            System.err.println(exception);
        }
    }
}
