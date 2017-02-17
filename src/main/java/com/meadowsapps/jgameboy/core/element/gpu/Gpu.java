package com.meadowsapps.jgameboy.core.element.gpu;

import com.meadowsapps.jgameboy.core.element.CoreElement;
import com.meadowsapps.jgameboy.core.element.Steppable;

import java.awt.*;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface Gpu extends CoreElement, Steppable {

    void draw(Graphics g);

    int getWidth();

    int getHeight();

}
