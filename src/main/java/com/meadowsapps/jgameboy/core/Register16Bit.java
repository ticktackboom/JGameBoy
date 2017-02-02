package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register16Bit extends AbstractRegister {

    private short value;

    @Override
    public int read() {
        return Short.toUnsignedInt(value);
    }

    @Override
    public void write(int value) {
        this.value = (short) value;
    }

    @Override
    public int size() {
        return 0xFFFF;
    }
}
