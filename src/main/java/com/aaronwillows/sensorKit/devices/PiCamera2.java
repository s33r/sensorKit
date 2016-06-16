package com.aaronwillows.sensorKit.devices;

import com.aaronwillows.sensorKit.IDevice;
import com.aaronwillows.sensorKit.sensors.IImageSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 6/12/2016.
 */
public class PiCamera2 implements IDevice, IImageSource {
    private String command = "raspistill";

    HashMap<String, String> commands = new HashMap<>();

    private int clampValue(int value, int min, int max) {
        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }

    public PiCamera2 raw() {
        command = "raspistillyuv";
        commands.put("rgb", null);

        return this;
    }

    public PiCamera2 noPreview() {
        commands.put("nopreview", null);

        return this;
    }

    public PiCamera2 setMode(int mode) {
        commands.put("mode", Integer.toString(clampValue(mode, 0, 7)));

        return this;
    }

    public int toFile(String outputPath) throws Exception {
        commands.put("output", outputPath);

        ProcessBuilder processBuilder = new ProcessBuilder(getArguments());

        return processBuilder.start().waitFor();
    }

    public BufferedImage toStream() {
        BufferedImage image = null;

        commands.put("output", "-");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(getArguments());
            Process process = processBuilder.start();

            image = ImageIO.read(process.getInputStream());

            process.waitFor();
        } catch (Exception exception) {
            System.err.println(exception);
        }

        return image;
    }

    @Override
    public BufferedImage getData() {
        return toStream();
    }

    private List<String> getArguments() {
        List<String> result = new ArrayList<>();

        result.add(command);

        commands.forEach((key, value) -> {
            result.add("--" + key);

            if (value != null) {
                result.add(value);
            }
        });

        return result;
    }


    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
