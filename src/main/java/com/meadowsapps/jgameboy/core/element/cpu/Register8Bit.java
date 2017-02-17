package com.meadowsapps.jgameboy.core.element.cpu;

import com.meadowsapps.jgameboy.core.element.cpu.AbstractRegister;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register8Bit extends AbstractRegister {

    private int value;

    public Register8Bit() {
        value = 0;
    }

    @Override
    public int read() {
        return value;
    }

    @Override
    public void write(int value) {
        this.value = value & 0xFF;
    }

}
