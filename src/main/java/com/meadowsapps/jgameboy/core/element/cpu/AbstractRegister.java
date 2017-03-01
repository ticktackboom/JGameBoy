package com.meadowsapps.jgameboy.core.element.cpu;

import com.meadowsapps.jgameboy.core.util.Constants;

/**
 * Created by dmeadows on 1/13/2017.
 */
public abstract class AbstractRegister implements Register {

    @Override
    public int read() {
        return _readImpl();
    }

    @Override
    public void write(int value) {
        _writeImpl(value);
    }

    @Override
    public final void inc() {
        int result = _readImpl() + 1;
        _writeImpl(result);
    }

    @Override
    public final void dec() {
        int result = _readImpl() - 1;
        _writeImpl(result);
    }

    @Override
    public final void shift(int dir, int by) {
        int value = _readImpl();
        int result = (dir == Constants.LEFT) ? value << by : value >> by;
        _writeImpl(result);
    }

    @Override
    public final void invert() {
        int result = ~_readImpl();
        _writeImpl(result);
    }

    @Override
    public final void add(int value) {
        int result = _readImpl() + value;
        _writeImpl(result);
    }

    @Override
    public final void subtract(int value) {
        int result = _readImpl() - value;
        _writeImpl(result);
    }

    @Override
    public final int get(int bit) {
        return ((_readImpl() >> bit) & 1);
    }

    @Override
    public final void set(int bit, int set) {
        int value = _readImpl();

        int result;
        if (set == 1) {
            result = value | (1 << bit);
        } else {
            result = value & ~(1 << bit);
        }
        _writeImpl(result);
    }

    @Override
    public final void set(int bit, boolean set) {
        set(bit, (set) ? 1 : 0);
    }

    @Override
    public final void flip(int bit) {
        int value = _readImpl();
        int result = (value ^ (1 << bit));
        _writeImpl(result);
    }

    @Override
    public final boolean isSet(int bit) {
        return get(bit) == 1;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '{' +
                "value: " + _readImpl() +
                '}';
    }

    /**
     * Read implementation used for internal reading within the register
     *
     * @return
     */
    protected abstract int _readImpl();

    /**
     * Write implementation used for internal writing within the register
     *
     * @param value
     */
    protected abstract void _writeImpl(int value);

}
