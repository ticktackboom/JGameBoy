package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/24/2017.
 */
public interface Display {

    int read(int addr);

    void write(int value, int addr);
}
