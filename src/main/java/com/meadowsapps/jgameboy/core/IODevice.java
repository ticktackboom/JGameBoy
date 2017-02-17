package com.meadowsapps.jgameboy.core;

/**
 * Created by Dylan on 2/16/17.
 */
public interface IODevice {

    int read(int addr);

    void write(int value, int addr);

}
