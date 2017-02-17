package com.meadowsapps.jgameboy.core.element.cartridge;

import com.meadowsapps.jgameboy.core.element.CoreElement;

import java.io.File;
import java.io.IOException;

/**
 * Created by Dylan on 2/6/17.
 */
public interface Cartridge extends CoreElement {

    void load(File file) throws IOException;

    int read(int addr);

    void write(int value, int addr);

}
