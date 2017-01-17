package com.meadowsapps.jgameboy.core;

import org.joou.UShort;

import static org.joou.Unsigned.ushort;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register16Bit extends AbstractRegister {

    private UShort value;

    public Register16Bit() {
        value = ushort(0);
    }

    @Override
    public int read() {
        return value.intValue();
    }

    @Override
    public void write(int value) {
        value &= size();
        this.value = ushort(value);
    }

    @Override
    public int size() {
        return UShort.MAX_VALUE;
    }
}
