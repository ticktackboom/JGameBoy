package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface Gpu extends CoreElement {

    int read(int addr);

    void write(int value, int addr);
}
