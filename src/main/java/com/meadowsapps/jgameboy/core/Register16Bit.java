package com.meadowsapps.jgameboy.core;

import com.meadowsapps.jgameboy.core.uint.uint16;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register16Bit extends AbstractRegister {

    private uint16 value;

    public Register16Bit() {
        value = new uint16();
    }

    @Override
    public int read() {
        return value.intValue();
    }

    @Override
    public void write(int value) {
        this.value = new uint16(value);
    }

}
