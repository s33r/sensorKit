package com.aaronwillows.sensorKit.devices.HCSR04;

import com.aaronwillows.sensorKit.sensors.IDistanceData;

/**
 * Created by Administrator on 6/17/2016.
 */
public class RangeData implements IDistanceData {
    private double distance;

    @Override
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
