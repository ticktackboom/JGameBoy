package com.meadowsapps.jgameboy.gbc.core.element.gpu;

import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.core.element.gpu.Gpu;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;
import com.meadowsapps.jgameboy.gbc.core.element.AbstractGbcCoreElement;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Dylan on 2/7/17.
 */
public class GbcGpu extends AbstractGbcCoreElement implements Gpu {

    private Image buffer;


    public GbcGpu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() throws InitializationException {
        // create buffer
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void reset() {
        buffer.flush();
    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public void step() {

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
