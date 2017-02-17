package com.meadowsapps.jgameboy.core.gpu;

import com.meadowsapps.jgameboy.core.CoreElement;
import com.meadowsapps.jgameboy.core.IODevice;

import java.awt.*;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface Gpu extends CoreElement, IODevice {

    void draw(Graphics g);

    int getWidth();

    int getHeight();

}
