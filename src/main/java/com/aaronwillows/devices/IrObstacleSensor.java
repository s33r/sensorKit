package com.aaronwillows.devices;

import com.aaronwillows.IDevice;
import com.aaronwillows.sensors.IDetectionSource;
import com.pi4j.wiringpi.Gpio;

import java.util.function.Consumer;


public class IrObstacleSensor implements IDevice, IDetectionSource {
    private int pinAddress;
    private boolean enabled;
    private Consumer<Void> consumer;

    public IrObstacleSensor(int pinAddress) {
        this.pinAddress = pinAddress;
    }

    public void enable() {
        Gpio.wiringPiISR(pinAddress, Gpio.INT_EDGE_FALLING,
                (something) -> {
                    if (consumer != null) {
                        consumer.accept(null);
                    }
                });


        enabled = true;
    }


    public void disable() {
        Gpio.wiringPiClearISR(pinAddress);
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setConsumer(Consumer<Void> consumer) {
        this.consumer = consumer;
    }
}
