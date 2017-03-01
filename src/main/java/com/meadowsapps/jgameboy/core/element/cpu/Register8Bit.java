package com.meadowsapps.jgameboy.core.element.cpu;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register8Bit extends AbstractRegister {

    private byte value;

    public Register8Bit() {
        value = 0;
    }

    @Override
    protected int _readImpl() {
        return Byte.toUnsignedInt(value);
    }

    @Override
    protected void _writeImpl(int value) {
        this.value = (byte) value;
    }

}
