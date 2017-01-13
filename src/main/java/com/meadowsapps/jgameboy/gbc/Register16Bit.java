package com.meadowsapps.jgameboy.gbc;

import org.joou.UShort;

import static org.joou.Unsigned.ushort;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register16Bit implements GbcRegister {

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
        this.value = ushort(value);
    }
}
