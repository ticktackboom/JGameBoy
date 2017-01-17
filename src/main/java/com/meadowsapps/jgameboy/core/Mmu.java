package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/17/2017.
 */
public interface Mmu {

    int readByte(int addr);

    int readWord(int addr);

    void writeByte(int value, int addr);

    void writeWord(int value, int addr);

    int[] dump();
}
