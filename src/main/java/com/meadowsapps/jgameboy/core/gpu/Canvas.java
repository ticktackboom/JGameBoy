package com.meadowsapps.jgameboy.core.gpu;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dylan on 2/12/17.
 */
public abstract class Canvas extends JPanel {

    @Override
    public final void paint(Graphics g) {
        draw(g);
    }

    public abstract void draw(Graphics g);

}
