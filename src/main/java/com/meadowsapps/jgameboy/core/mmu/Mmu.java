package com.meadowsapps.jgameboy.core.mmu;

import com.meadowsapps.jgameboy.core.CoreElement;

/**
 * Created by dmeadows on 1/17/2017.
 */
public interface Mmu extends CoreElement {

    int readByte(int addr);

    int readWord(int addr);

    void writeByte(int value, int addr);

    void writeWord(int value, int addr);

}