package com.aaronwillows.sensorKit.devices.HCSR04;

import com.aaronwillows.sensorKit.IDevice;
import com.aaronwillows.sensorKit.sensors.IDetectionSource;
import com.aaronwillows.sensorKit.sensors.IDistanceSource;
import com.pi4j.wiringpi.Gpio;

import java.util.function.Consumer;

public class UltrasonicRanger implements IDevice, IDistanceSource, IDetectionSource {
    private static final double SPEED_OF_SOUND = 17014; // Speed of sound (cm/s) / 2
    private static final double MAX_VALUE = 400;
    private static final double MIN_VALUE = 2;

    private boolean enabled;

    private int echoPinAddress;
    private int trigPinAddress;

    private double thresholdMin = MAX_VALUE - 2;
    private double thresholdMax = MAX_VALUE;
    private Consumer<Void> consumer;

    private double clampDistance(double distance) {
        if (distance < MIN_VALUE) {
            return MIN_VALUE;
        }

        if (distance > MAX_VALUE) {
            return MAX_VALUE;
        }

        return distance;
    }


    public UltrasonicRanger(int echoPinAddress, int trigPinAddress) {
        this.echoPinAddress = echoPinAddress;
        this.trigPinAddress = trigPinAddress;
    }

    @Override
    public void enable() {
        Gpio.pinMode(echoPinAddress, Gpio.INPUT);
        Gpio.pinMode(trigPinAddress, Gpio.OUTPUT);

        enabled = true;
    }

    @Override
    public void disable() {


        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public RangeData getData() {
        long startTime;
        long timeDelta;

        Gpio.digitalWrite(trigPinAddress, Gpio.LOW);
        Gpio.delay(2);

        Gpio.digitalWrite(trigPinAddress, Gpio.HIGH);
        Gpio.delayMicroseconds(10);
        Gpio.digitalWrite(trigPinAddress, Gpio.LOW);

        while (!(Gpio.digitalRead(echoPinAddress) == 1)) ;
        startTime = System.nanoTime();

        while (!(Gpio.digitalRead(echoPinAddress) == 0)) ;
        timeDelta = (System.nanoTime() - startTime);

        double distance = clampDistance(timeDelta / SPEED_OF_SOUND);

        RangeData result = new RangeData();
        result.setDistance(distance);

        if ((consumer != null) && (distance > thresholdMin) && (distance < thresholdMax)) {
            consumer.accept(null);
        }

        return result;
    }

    @Override
    public void setConsumer(Consumer<Void> consumer) {
        this.consumer = consumer;
    }

    public double getThresholdMin() {
        return thresholdMin;
    }

    public void setThresholdMin(double thresholdMin) {
        if (thresholdMin <= MIN_VALUE) {
            this.thresholdMin = MIN_VALUE;
        } else {
            this.thresholdMin = thresholdMin;
        }
    }

    public double getThresholdMax() {
        return thresholdMax;
    }

    public void setThresholdMax(double thresholdMax) {
        if (thresholdMax >= MAX_VALUE) {
            this.thresholdMax = MAX_VALUE;
        } else {
            this.thresholdMax = thresholdMax;
        }
    }
}
