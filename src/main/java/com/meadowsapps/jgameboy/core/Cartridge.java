package com.meadowsapps.jgameboy.core;

import java.io.File;
import java.io.IOException;

/**
 * Created by Dylan on 2/6/17.
 */
public interface Cartridge {

    void load(File file) throws IOException;

    int read(int addr);

    void write(int value, int addr);
}
