package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register8Bit extends AbstractRegister {

    private byte value;

    @Override
    public int read() {
        return Byte.toUnsignedInt(value);
    }

    @Override
    public void write(int value) {
        this.value = (byte) value;
    }

    @Override
    public int size() {
        return 0xFF;
    }

}
