package com.aaronwillows.sensorKit.devices.BMP180;

import com.aaronwillows.sensorKit.IDevice;
import com.aaronwillows.sensorKit.sensors.IAltitudeSource;
import com.aaronwillows.sensorKit.sensors.IPressureSource;
import com.aaronwillows.sensorKit.sensors.ITemperatureSource;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.I2C;

import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

public class Barometer implements IDevice, ITemperatureSource, IPressureSource, IAltitudeSource {
    private final int DEVICE_ID = 0x77;

    private final int EPROM_OFFSET = 0xAA;

    private final int CONTROL_REGISTER = 0xF4;

    private final int TEMP_CONTROL = 0x2E;
    private final int TEMP_DATA = 0xF6;

    private final int PRESSURE_CONTROL = 0x34;
    private final int PRESSURE_DATA = 0xF6;

    private final int CAL_INDEX_AC1 = 0x00;
    private final int CAL_INDEX_AC2 = 0x01;
    private final int CAL_INDEX_AC3 = 0x02;
    private final int CAL_INDEX_AC4 = 0x03;
    private final int CAL_INDEX_AC5 = 0x04;
    private final int CAL_INDEX_AC6 = 0x05;
    private final int CAL_INDEX_B1 = 0x06;
    private final int CAL_INDEX_B2 = 0x07;
    private final int CAL_INDEX_MB = 0x08;
    private final int CAL_INDEX_MC = 0x09;
    private final int CAL_INDEX_MD = 0x0A;


    private int deviceHandle;
    private boolean enabled;


    Calibration calibration;

    private int readUnsignedRegister(int register) {
        int msb = I2C.wiringPiI2CReadReg8(deviceHandle, register);
        int lsb = I2C.wiringPiI2CReadReg8(deviceHandle, register + 1);

        return (msb << 8) + lsb;
    }

    private int readSignedRegister(int register) {
        int msb = I2C.wiringPiI2CReadReg8(deviceHandle, register);
        int lsb = I2C.wiringPiI2CReadReg8(deviceHandle, register + 1);

        return (short) ((msb << 8) + lsb);
    }

    private void writeRegister(int register, int value) {
        I2C.wiringPiI2CWriteReg16(deviceHandle, register, value);
    }

    private int readTemperature() {
        writeRegister(CONTROL_REGISTER, TEMP_CONTROL);
        Gpio.delay(5);

        return readSignedRegister(TEMP_DATA);
    }

    private int readPressure() {
        writeRegister(CONTROL_REGISTER, PRESSURE_CONTROL);
        Gpio.delay(5);

        return readSignedRegister(PRESSURE_DATA);
    }

    private BarometerData calculateTrueValues(int rawTemperature, int rawPressure) {
        BarometerData result = new BarometerData();

        result.setTemperature(calibration.getTemperature(rawTemperature) / 10.0);
        result.setPressure(calibration.getPressure(rawTemperature, rawPressure, 0));

        return result;
    }


    @Override
    public void enable() {
        deviceHandle = I2C.wiringPiI2CSetup(DEVICE_ID);

        PrimitiveIterator.OfInt iterator = IntStream.range(0, 12).iterator();

        calibration = new Calibration();
        calibration.setAc1(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));
        calibration.setAc2(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));
        calibration.setAc3(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));
        calibration.setAc4(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));
        calibration.setAc5(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));
        calibration.setAc6(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));

        calibration.setB1(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));
        calibration.setB2(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));

        calibration.setMb(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));
        calibration.setMc(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));
        calibration.setMd(readSignedRegister(EPROM_OFFSET + iterator.nextInt() * 2));

        enabled = true;
    }

    @Override
    public void disable() {
        calibration = null;

        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }


    public Barometer() {
    }

    public BarometerData getData() {
        BarometerData result = new BarometerData();

        if (!enabled) {
            return result;
        }

        int rawTemperature = readTemperature();
        int rawPressure = readPressure();

        result = calculateTrueValues(rawTemperature, rawPressure);

        return result;
    }


}
