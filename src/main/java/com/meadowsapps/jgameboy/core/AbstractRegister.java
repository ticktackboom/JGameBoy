package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/13/2017.
 */
public abstract class AbstractRegister implements Register {

    @Override
    public final void inc() {
        int value = read();
        value = (value + 1) & size();
        write(value);
    }

    @Override
    public final void dec() {
        int value = read();
        value = (value - 1) & size();
        write(value);
    }

    @Override
    public final void shift(int dir, int by) {
        int value = read();
        value = (dir == LEFT) ? value << by : value >> by;
        value &= size();
        write(value);
    }

    @Override
    public final void invert() {
        int value = read();
        write(~value);
    }

    @Override
    public final void add(int value) {
        int current = read();
        int sum = current + value;
        sum &= size();
        write(sum);
    }

    @Override
    public final void subtract(int value) {
        int current = read();
        int difference = current - value;
        difference &= size();
        write(difference);
    }

    @Override
    public final int get(int bit) {
        return ((read() >> bit) & 1);
    }

    @Override
    public final void set(int bit, int set) {
        int value = read();
        if (set == 1) {
            value |= (1 << bit);
        } else {
            value &= ~(1 << bit);
        }
        value &= size();
        write(value);
    }

    @Override
    public final void set(int bit, boolean set) {
        set(bit, (set) ? 1 : 0);
    }

    @Override
    public final void flip(int bit) {
        int value = read();
        value = (value ^ (1 << bit));
        write(value);
    }

    @Override
    public final boolean isSet(int bit) {
        return get(bit) == 1;
    }

    @Override
    public String toString() {
        return "" + read();
    }
}
