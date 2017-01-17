package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/17/2017.
 */
public interface Mmu {

    int read(int addr);

    void write(int value, int addr);

    int[] dump();
}
