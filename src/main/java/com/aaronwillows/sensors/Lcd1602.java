package com.aaronwillows.sensors;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created by Administrator on 6/12/2016.
 */
public class Lcd1602 implements ISensor {
    private boolean enabled;
    private final int BLEN = 1;

    private I2CBus bus;


    public Lcd1602() {


    }

    private void writeWord(int data) {
        if (BLEN == 1) {

        }
    }

    private void sendCommand(int command) {
        int buffer = command & 0xF0;
        buffer |= 0x04;

    }

    private void sendData(int data) {

    }


    @Override
    public void enable() {
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);

            enabled = true;
        } catch (I2CFactory.UnsupportedBusNumberException exception) {
            System.err.print(exception);
        } catch (IOException exception) {
            System.err.print(exception);
        }


    }

    @Override
    public void disable() {

    }

    public boolean isEnabled() {
        return enabled;
    }
}
