package com.aaronwillows.sensors;

import com.pi4j.wiringpi.Gpio;


public class IrObstacleSensor implements ISensor {
    private int pinAddress;

    public IrObstacleSensor(int pinAddress) {
        this.pinAddress = pinAddress;
    }

    public void enable() {
        Gpio.wiringPiISR(pinAddress, Gpio.INT_EDGE_FALLING,
                (something) -> System.out.println("Detected Obstacle"));
    }

    public void disable() {

    }


}
