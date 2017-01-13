package com.meadowsapps.jgameboy.gbc;

import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * Created by dmeadows on 1/12/2017.
 */
public final class Register {

    private int value;

    /**
     * Zero flag
     */
    public static short ZERO_FLAG = 0x80;
    /**
     * Subtract/negative flag
     */
    public static short SUBTRACT_FLAG = 0x40;
    /**
     * Half carry flag
     */
    public static short HALF_CARRY_FLAG = 0x20;
    /**
     * Carry flag
     */
    public static short CARRY_FLAG = 0x10;

    public Register() {
    }

    public int read() {
        return value;
    }

    public void write(int value) {
        this.value = value;
    }

    public static int lo(Register r) {
        return r.read() >> 8;
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

    public static void main(String[] args) {

    }
}
