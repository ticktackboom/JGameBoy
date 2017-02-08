package com.meadowsapps.jgameboy.core;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by dmeadows on 2/8/2017.
 */
public interface Display extends CoreElement {

    void display(GraphicsContext context);

    int getWidth();

    int getHeight();
}
