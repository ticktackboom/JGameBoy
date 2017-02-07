package com.meadowsapps.jgameboy.core;

import com.meadowsapps.jgameboy.core.uint.uint8;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register8Bit extends AbstractRegister {

    private uint8 value;

    public Register8Bit() {
        value = new uint8();
    }

    @Override
    public int read() {
        return value.intValue();
    }

    @Override
    public void write(int value) {
        this.value = new uint8(value);
    }

}
