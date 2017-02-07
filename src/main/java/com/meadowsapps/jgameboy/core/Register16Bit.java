package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register16Bit extends AbstractRegister {

    private int value;

    public Register16Bit() {
        value = 0;
    }

    @Override
    public int read() {
        return value;
    }

    @Override
    public void write(int value) {
        this.value = value & 0xFFFF;
    }

}
