package com.aaronwillows.sensorKit.devices;

import com.aaronwillows.sensorKit.IDevice;
import com.aaronwillows.sensorKit.sensors.ILcd;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.I2C;

/**
 * Created by Administrator on 6/12/2016.
 */
public class Lcd1602 implements IDevice, ILcd {
    private int deviceHandle;
    private boolean enabled;


    public Lcd1602() {


    }

    private void writeWord(int data) {

        /* No idea what BLEN is, its set to one and then never changed
        int temp = data;
        if ( BLEN == 1 )
            temp |= 0x08;
        else
            temp &= 0xF7;
        I2C.wiringPiI2CWrite(deviceHandle, temp);
        */

        I2C.wiringPiI2CWrite(deviceHandle, data | 0x08);
    }

    private void sendCommand(int command) {
        int buffer = command & 0xF0;
        buffer |= 0x04;
        writeWord(buffer);

        buffer &= 0xFB;
        Gpio.delay(2);
        writeWord(buffer);

        buffer = (command & 0x0F) << 4;
        buffer |= 0x04;
        writeWord(buffer);

        buffer &= 0xFB;
        Gpio.delay(2);
        writeWord(buffer);
    }

    private void sendData(int data) {
        int buffer = data & 0xF0;
        buffer |= 0x05;
        writeWord(buffer);

        buffer &= 0xFB;
        Gpio.delay(2);
        writeWord(buffer);

        buffer = (data & 0x0F) << 4;
        buffer |= 0x05;
        writeWord(buffer);

        buffer &= 0xFB;
        Gpio.delay(2);
        writeWord(buffer);
    }

    private boolean validatePosition(int row, int column) {
        return row >= 0 && row <= 15 && (column == 0 || column == 1);
    }

    private int getAddress(int row, int column) {
        return 0x80 + (0x40 * column) + row;
    }


    @Override
    public void enable() {
        deviceHandle = I2C.wiringPiI2CSetup(0x27);

        sendCommand(0x33);
        Gpio.delay(5);

        sendCommand(0x32);
        Gpio.delay(5);

        sendCommand(0x28);
        Gpio.delay(5);

        sendCommand(0x0C);
        Gpio.delay(5);

        clear();
        I2C.wiringPiI2CWrite(deviceHandle, 0x08);

        enabled = true;
    }

    @Override
    public void disable() {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void clear() {
        if (!enabled) {
            return;
        }

        sendCommand(0x01);
    }

    public void write(char character, int row, int column) {
        if (!enabled || !validatePosition(row, column)) {
            return;
        }

        sendCommand(getAddress(row, column));
        sendData(character);
    }

    public void write(String value) {
        write(value, 0, 0, true);
    }

    public void write(String value, boolean wrap) {
        write(value, 0, 0, wrap);
    }

    public void write(String value, int row) {
        write(value, row, 0, true);
    }

    public void write(String value, int row, int column) {
        write(value, row, column, true);
    }

    public void write(String value, int row, boolean wrap) {
        write(value, row, 0, true);
    }

    public void write(String value, int row, int column, boolean wrap) {
        if (!enabled) {
            return;
        }

        int characterCounter = 0;

        for (int y = column; y < getHeight(); y++) {
            for (int x = row; x < getWidth(); x++) {
                if (characterCounter >= value.length()) {
                    break;
                }

                write(value.charAt(characterCounter++), x, y);
            }

            if (!wrap) {
                break;
            }
        }

    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override
    public char getCharacter(int row, int column) {
        return ' ';
    }

    @Override
    public String getCharacters(int row, int column, int length) {
        return "";
    }
}
