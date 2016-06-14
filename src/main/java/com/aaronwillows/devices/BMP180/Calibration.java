package com.aaronwillows.devices.BMP180;

/**
 * Created by Administrator on 6/14/2016.
 */
public class Calibration {

    private int ac1;
    private int ac2;
    private int ac3;
    private int ac4;
    private int ac5;
    private int ac6;

    private int b1;
    private int b2;

    private int mb;
    private int mc;
    private int md;

    public int getAc1() {
        return ac1;
    }

    public void setAc1(int ac1) {
        this.ac1 = ac1;
    }

    public int getAc2() {
        return ac2;
    }

    public void setAc2(int ac2) {
        this.ac2 = ac2;
    }

    public int getAc3() {
        return ac3;
    }

    public void setAc3(int ac3) {
        this.ac3 = ac3;
    }

    public int getAc4() {
        return ac4;
    }

    public void setAc4(int ac4) {
        this.ac4 = ac4;
    }

    public int getAc5() {
        return ac5;
    }

    public void setAc5(int ac5) {
        this.ac5 = ac5;
    }

    public int getAc6() {
        return ac6;
    }

    public void setAc6(int ac6) {
        this.ac6 = ac6;
    }

    public int getB1() {
        return b1;
    }

    public void setB1(int b1) {
        this.b1 = b1;
    }

    public int getB2() {
        return b2;
    }

    public int getB3(int rawTemperature, int overSample) {
        int b6 = getB6(rawTemperature);

        int x1 = (b2 * ((b6 * b6) >> 12)) >> 11;
        int x2 = (ac2 * b6) >> 11;
        int x3 = x1 + x2;

        return (((ac1 * 4 + x3) << overSample) + 2) >> 2;
    }

    public long getB4(int rawTemperature) {
        int b6 = getB6(rawTemperature);

        int x1 = (ac3 * b6) >> 13;
        int x2 = (b1 * ((b6 * b6) >> 12)) >> 16;
        int x3 = (x1 + x2 + 2) >> 2;

        long b4 = x3 + 32768;
        return (ac4 * b4) >> 15;
    }

    public int getB5(int rawTemperature) {
        int x1 = ((rawTemperature - ac6) * ac5) >> 15;
        double x2 = (mc << 11) / (double) (x1 + md);

        //The algo in the datasheet seems to be rounding rather than truncating values.
        return x1 + (int) Math.round(x2);
    }

    public int getB6(int rawTemperature) {
        return getB5(rawTemperature) - 4000;
    }

    public long getB7(int rawTemperature, int rawPressure, int overSample) {
        int b3 = getB3(rawTemperature, overSample);

        return (rawPressure - b3) * (50000 >> overSample);
    }

    public int getPressure(int rawTemperature, int rawPressure, int overSample) {
        long b4 = getB4(rawTemperature);
        long b7 = getB7(rawTemperature, rawPressure, overSample);

        int pressure;
        if (b7 > 0x80000000) {
            pressure = (int) (Math.round((b7 * 2) / (double) b4));
        } else {
            pressure = (int) ((Math.round(b7 / (double) b4)) * 2);
        }

        int x1 = (pressure >> 8) * (pressure >> 8);
        x1 = (x1 * 3038) >> 16;
        int x2 = (-7357 * pressure) >> 16;

        return pressure + ((x1 + x2 + 3791) >> 4);
    }

    public int getTemperature(int rawTemperature) {
        int b5 = getB5(rawTemperature);

        return (b5 + 8) >> 4;
    }

    public void setB2(int b2) {
        this.b2 = b2;
    }

    public int getMb() {
        return mb;
    }

    public void setMb(int mb) {
        this.mb = mb;
    }

    public int getMc() {
        return mc;
    }

    public void setMc(int mc) {
        this.mc = mc;
    }

    public int getMd() {
        return md;
    }

    public void setMd(int md) {
        this.md = md;
    }


}
