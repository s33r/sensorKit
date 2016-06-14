package com.aaronwillows.devices;

import com.aaronwillows.IDevice;
import com.aaronwillows.sensors.IColorSink;
import com.pi4j.wiringpi.SoftPwm;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 6/10/2016.
 */
public class RgLed implements IDevice, IColorSink {
    public final int MINIMUM_INTENSITY = 0;
    public final int MAXIMUM_INTENSITY = 100;

    private boolean enabled = false;

    private int redPin;
    private int greenPin;

    private int clampColor(int color) {
        if (color < MINIMUM_INTENSITY) {
            return MINIMUM_INTENSITY;
        }

        if (color > MAXIMUM_INTENSITY) {
            return MAXIMUM_INTENSITY;
        }

        return color;
    }

    public RgLed(int redPin, int greenPin) {
        this.redPin = redPin;
        this.greenPin = greenPin;
    }

    public void enable() {
        SoftPwm.softPwmCreate(redPin, MINIMUM_INTENSITY, MAXIMUM_INTENSITY);
        SoftPwm.softPwmCreate(greenPin, MINIMUM_INTENSITY, MAXIMUM_INTENSITY);

        enabled = true;
    }

    public void disable() {
        SoftPwm.softPwmStop(redPin);
        SoftPwm.softPwmStop(greenPin);

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


    public void setColor(int red, int green) {
        if (!enabled) {
            return;
        }

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            SoftPwm.softPwmWrite(redPin, clampColor(red));

            TimeUnit.MILLISECONDS.sleep(500);
            SoftPwm.softPwmWrite(greenPin, clampColor(green));
        } catch (InterruptedException exception) {
            System.err.println(exception);
        }
    }

    @Override
    public void setColor(int red, int green, int blue) {
        setColor(red, green);
    }
}