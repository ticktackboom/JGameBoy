package com.meadowsapps.jgameboy.core;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface Gpu extends CoreElement {

    void draw(GraphicsContext context);

    int read(int addr);

    void write(int value, int addr);
}
