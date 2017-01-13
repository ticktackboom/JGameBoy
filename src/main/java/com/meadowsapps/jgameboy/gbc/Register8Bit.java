package com.meadowsapps.jgameboy.gbc;

import org.joou.UByte;

import static org.joou.Unsigned.ubyte;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register8Bit implements GbcRegister {

    private UByte value;

    public Register8Bit() {
        value = ubyte(0);
    }

    @Override
    public int read() {
        return value.intValue();
    }

    @Override
    public void write(int value) {
        this.value = ubyte(value);
    }
}
