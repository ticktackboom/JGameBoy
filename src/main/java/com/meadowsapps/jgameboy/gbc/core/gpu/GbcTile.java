package com.meadowsapps.jgameboy.gbc.core.gpu;

import com.meadowsapps.jgameboy.gbc.core.GbcConstants;
import sun.awt.image.ToolkitImage;

import java.awt.*;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

/**
 * Created by Dylan on 2/11/17.
 */
public class GbcTile implements GbcConstants {

    private Image[] image;

    private int[] imageData;

    private boolean[] valid;

    private GbcPalette palette;

    private MemoryImageSource[] sources;

    public static final int WIDTH = 8;

    public static final int HEIGHT = 8;

    public GbcTile() {
        int size = WIDTH * HEIGHT;
        image = new Image[size];
        imageData = new int[size];
        valid = new boolean[size];
        sources = new MemoryImageSource[size];
        palette = new GbcPalette();
        allocateImage(TILE_BKG);
    }

    private void allocateImage(int index) {
        int bits = 32;
        int r = 0x00FF0000;
        int g = 0x0000FF00;
        int b = 0x000000FF;
        int a = 0xFF000000;
        DirectColorModel model = new DirectColorModel(bits, r, g, b, a);
        MemoryImageSource source = new MemoryImageSource(WIDTH, HEIGHT, model, imageData, 0, HEIGHT);
        sources[index] = source;
        image[index] = new ToolkitImage(source);
    }

    public void dispose() {
        for (int r = 0; r < WIDTH * HEIGHT; r++) {
            if (image[r] != null) {
                image[r].flush();
                valid[r] = false;
            }
        }
    }

    public void update(int attributes, int[] vram, int offset) {
        int pX, pY;
        int rgbValue;
        if (image[attributes] == null) {
            allocateImage(attributes);
        }

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if ((attributes & TILE_FLIPX) != 0) {
                    pX = 7 - x;
                } else {
                    pX = x;
                }
                if ((attributes & TILE_FLIPY) != 0) {
                    pY = 7 - y;
                } else {
                    pY = y;
                }

                int lo = (vram[offset + (pY * 2)] & (0x80 >> pX)) >> (7 - pX);
                int hi = (vram[offset + (pY * 2) + 1] & (0x80 >> pX)) >> (7 - pX);
                int entryIndex = (hi * 2) + lo;
                rgbValue = palette.getRgbEntry(entryIndex);

//                if ((!dmgcpu.gbcFeatures) || ((attributes >> 2) > 7)) {
//                    if (entryIndex == 0) {
//                        rgbValue &= 0x00FFFFFF;
//                    }
//                }
                imageData[(y * 8) + x] = rgbValue;
            }
        }

        sources[attributes].newPixels();
        valid[attributes] = true;
    }

    public void draw(Graphics g, int attributes, int x, int y) {
        g.drawImage(image[attributes], x, y, null);
    }

    public void validate(int attributes, int[] vram, int offset) {
        if (!isValid(attributes)) {
            update(attributes, vram, offset);
        }
    }

    public void invalidate() {
        for (int r = 0; r < WIDTH * HEIGHT; r++) {
            if (image[r] != null) {
                image[r].flush();
                image[r] = null;
            }
            valid[r] = false;
        }
    }

    public void invalidate(int attributes) {
        for (int r = 0; r < 4; r++) {
            if (image[attributes + r] != null) {
                image[attributes + r].flush();
                image[attributes + r] = null;
            }
            valid[attributes + r] = false;
        }
    }

    public boolean isValid(int attributes) {
        return valid[attributes];
    }

    public GbcPalette getPalette() {
        return palette;
    }

    public void setPalette(GbcPalette palette) {
        this.palette = palette;
    }

}
