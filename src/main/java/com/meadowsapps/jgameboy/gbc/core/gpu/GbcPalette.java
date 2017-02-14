package com.meadowsapps.jgameboy.gbc.core.gpu;

import java.awt.*;

/**
 * Created by Dylan on 2/11/17.
 */
public class GbcPalette {

    private int[] data;

    private int[] gbcData;

    private int[] colors = {
            DEFAULT_COLORS[0],
            DEFAULT_COLORS[1],
            DEFAULT_COLORS[2],
            DEFAULT_COLORS[3]
    };

    private static final int PALETTE_SIZE = 4;

    private static final int[] DEFAULT_COLORS = {0xFFFFFFFF, 0xFFAAAAAA, 0xFF555555, 0xFF000000};

    public GbcPalette() {
        data = new int[PALETTE_SIZE];
        gbcData = new int[PALETTE_SIZE];
        for (int index = 0; index < PALETTE_SIZE; index++) {
            data[index] = index;
        }
    }

    private GbcPalette(int c1, int c2, int c3, int c4) {
        data = new int[PALETTE_SIZE];
        gbcData = new int[PALETTE_SIZE];
        data[0] = c1;
        data[1] = c2;
        data[2] = c3;
        data[3] = c4;
    }

    public void setColors(int c1, int c2, int c3, int c4) {
        colors[0] = c1;
        colors[1] = c2;
        colors[2] = c3;
        colors[3] = c4;
    }

    public void setGbcColor(int index, boolean high, int color) {
        int value = gbcData[index];
        value = (high) ? value & 0x00FF : value & 0xFF00;
        gbcData[index] = (high) ? value | (color << 8) : value | color;

        int r = (gbcData[index] & 0x001F) << 3;
        int g = (gbcData[index] & 0x03E0) >> 2;
        int b = (gbcData[index] & 0x7C00) >> 7;
        Color c = new Color(r, g, b);
        colors[index] = c.getRGB();
    }

    public int getGbcColor(int index, boolean high) {
        int value = gbcData[index];
        value = (high) ? value >> 8 : value & 0xFF;
        return value;
    }

    public int getRgbEntry(int index) {
        int entry = data[index];
        return colors[entry];
    }

    public int getEntry(int index) {
        return data[index];
    }

    public static GbcPalette decode(int palette) {
        int c1 = (short) (palette & 0x03);
        int c2 = (short) ((palette & 0x0C) >> 2);
        int c3 = (short) ((palette & 0x30) >> 4);
        int c4 = (short) ((palette & 0xC0) >> 6);
        return new GbcPalette(c1, c2, c3, c4);
    }

}
