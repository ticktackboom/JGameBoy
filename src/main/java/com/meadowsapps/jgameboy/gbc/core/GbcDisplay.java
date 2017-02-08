package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Display;
import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;

/**
 * Created by dmeadows on 2/8/2017.
 */
public class GbcDisplay extends AbstractGbcCoreElement implements Display {

    public static final int WIDTH = 160;

    public static final int HEIGHT = 144;

    public GbcDisplay(GbcCore core) {
        super(core);
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        image.set
    }

    @Override
    public void display(GraphicsContext context) {
        int lcdControl = mmu().readByte(0xFF40);
        if ((lcdControl & 0x80) == 1) {
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {

                }
            }
        }
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }
}
