package com.aaronwillows.sensorKit.sensors;

import java.util.function.Consumer;

/**
 * Created by Administrator on 6/14/2016.
 */
public interface IDetectionSource {

    void setConsumer(Consumer<Void> consumer);

}
