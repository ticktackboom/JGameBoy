package com.meadowsapps.jgameboy.core.gpu;

import com.meadowsapps.jgameboy.core.CoreElement;
import com.meadowsapps.jgameboy.core.Steppable;

import java.awt.*;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface Gpu extends CoreElement {

    void draw(Graphics g);

    int read(int addr);

    void write(int value, int addr);

    int getWidth();

    int getHeight();
}
