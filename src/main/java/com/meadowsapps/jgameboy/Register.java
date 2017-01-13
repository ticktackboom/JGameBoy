package com.meadowsapps.jgameboy;

/**
 * Created by dmeadows on 1/12/2017.
 */
public interface Register {

    int read();

    void write(int value);

    static void inc(Register r) {
        int value = r.read();
        r.write(value + 1);
    }

    static void dec(Register r) {
        int value = r.read();
        r.write(value - 1);
    }

    static void add(Register r, int value) {
        int _value = r.read();
        r.write(_value + value);
    }

    static void subtract(Register r, int value) {
        int _value = r.read();
        r.write(_value - value);
    }
}
