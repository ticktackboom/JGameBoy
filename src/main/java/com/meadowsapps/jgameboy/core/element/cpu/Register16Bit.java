package com.meadowsapps.jgameboy.core.element.cpu;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register16Bit extends AbstractRegister {

    private short value;

    public Register16Bit() {
        value = 0;
    }

    @Override
    protected int _readImpl() {
        return Short.toUnsignedInt(value);
    }

    @Override
    protected void _writeImpl(int value) {
        this.value = (short) value;
    }

    public int hi() {
        return _readImpl() >> 8;
    }

    public void hi(int hi) {
        _writeImpl(((hi & 0xFF) << 8) + lo());
    }

    public int lo() {
        return _readImpl() & 0xFF;
    }

    public void lo(int lo) {
        _writeImpl((hi() << 8) + (lo & 0xFF));
    }

}
