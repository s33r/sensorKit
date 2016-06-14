package com.aaronwillows.devices.BMP180;

import junit.framework.TestCase;
import org.junit.Assert;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests BMP-180 calibration routines with calibration data from the data sheet
 */
public class CalibrationTest extends TestCase {

    private Calibration calibration;
    private int rawTemperature;
    private int rawPressure;

    @org.junit.Before
    public void setUp() throws Exception {
        calibration = new Calibration();
        calibration.setAc1(408);
        calibration.setAc2(-72);
        calibration.setAc3(-14383);
        calibration.setAc4(32741);
        calibration.setAc5(32757);
        calibration.setAc6(23153);

        calibration.setB1(6190);
        calibration.setB2(4);

        calibration.setMb(-32768);
        calibration.setMc(-8711);
        calibration.setMd(2868);

        rawTemperature = 27898;
        rawPressure = 23843;
    }


    @org.junit.Test
    public void testGetB3() throws Exception {

        assertEquals(422, calibration.getB3(rawTemperature, 0));
    }

    @org.junit.Test
    public void testGetB4() throws Exception {
        assertEquals(33457, calibration.getB4(rawTemperature));
    }

    @org.junit.Test
    public void testGetB5() throws Exception {
        assertEquals(2399, calibration.getB5(rawTemperature));
    }

    @org.junit.Test
    public void testGetB6() throws Exception {
        assertEquals(-1601, calibration.getB6(rawTemperature));
    }

    @org.junit.Test
    public void testGetB7() throws Exception {
        assertEquals(1171050000L, calibration.getB7(rawTemperature, rawPressure, 0));
    }

    @org.junit.Test
    public void testGetPressure() throws Exception {
        assertEquals(69964, calibration.getPressure(rawTemperature, rawPressure, 0));
    }

    @org.junit.Test
    public void testGetTemperature() throws Exception {
        assertEquals(150, calibration.getTemperature(rawTemperature));
    }

    @org.junit.Test
    public void testSetB2() throws Exception {

    }

}