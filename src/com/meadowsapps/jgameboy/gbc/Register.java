package com.meadowsapps.jgameboy.gbc;

/**
 * Created by dmeadows on 1/12/2017.
 */
public final class Register {

    private int value;

    public Register() {
    }

    public Register(int initialValue) {
        value = initialValue;
    }

    public int read() {
        return value;
    }

    public void write(int value) {
        this.value = value;
    }

    public static void inc(Register r) {
        int value = r.read();
        r.write(value + 1);
    }

    public static void dec(Register r) {
        int value = r.read();
        r.write(value - 1);
    }

    public static void add(Register r, int value) {
        int _value = r.read();
        r.write(_value + value);
    }

    public static void subtract(Register r, int value) {
        int _value = r.read();
        r.write(_value - value);
    }
}
