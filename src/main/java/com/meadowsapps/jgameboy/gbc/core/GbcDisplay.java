package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Display;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by dmeadows on 2/8/2017.
 */
public class GbcDisplay extends AbstractGbcCoreElement implements Display {

    public static final int WIDTH = 160;

    public static final int HEIGHT = 144;

    public GbcDisplay(GbcCore core) {
        super(core);

    }

    @Override
    public void initialize() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void display(GraphicsContext context) {
        context.clearRect(0, 0, WIDTH, HEIGHT);
        gpu().draw(context);
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
