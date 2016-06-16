package com.aaronwillows.sensorKit.sensors;

/**
 * Created by Administrator on 6/14/2016.
 */
public interface ILcd {
    void clear();

    void write(char character, int row, int column);

    void write(String value);

    void write(String value, boolean wrap);

    void write(String value, int row);

    void write(String value, int row, int column);

    void write(String value, int row, boolean wrap);

    void write(String value, int row, int column, boolean wrap);

    int getWidth();

    int getHeight();

    char getCharacter(int row, int column);

    String getCharacters(int row, int column, int length);
}
