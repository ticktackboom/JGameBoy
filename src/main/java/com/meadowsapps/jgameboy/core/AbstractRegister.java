package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/13/2017.
 */
public abstract class AbstractRegister implements Register {

    @Override
    public final void inc() {
        int result = read() + 1;
        write(result);
    }

    @Override
    public final void dec() {
        int result = read() - 1;
        write(result);
    }

    @Override
    public final void shift(int dir, int by) {
        int value = read();
        int result = (dir == LEFT) ? value << by : value >> by;
        write(result);
    }

    @Override
    public final void invert() {
        int result = ~read();
        write(result);
    }

    @Override
    public final void add(int value) {
        int result = read() + value;
        write(result);
    }

    @Override
    public final void subtract(int value) {
        int result = read() - value;
        write(result);
    }

    @Override
    public final int get(int bit) {
        return ((read() >> bit) & 1);
    }

    @Override
    public final void set(int bit, int set) {
        int value = read();

        int result;
        if (set == 1) {
            result = value | (1 << bit);
        } else {
            result = value & ~(1 << bit);
        }
        write(result);
    }

    @Override
    public final void set(int bit, boolean set) {
        set(bit, (set) ? 1 : 0);
    }

    @Override
    public final void flip(int bit) {
        int value = read();
        int result = (value ^ (1 << bit));
        write(result);
    }

    @Override
    public final boolean isSet(int bit) {
        return get(bit) == 1;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '{' +
                "value: " + read() +
                '}';
    }

}
