package com.meadowsapps.jgameboy.core.uint;

/**
 * Created by Dylan on 2/6/17.
 */
public class uint16 extends uint {

    public static final int MAX_VALUE = 0xFFFF;

    public uint16() {
        this(0);
    }

    public uint16(int value) {
        super(value);
    }

    @Override
    public int maxValue() {
        return MAX_VALUE;
    }

    @Override
    public uint16 plus(Number value) {
        int result = intValue() + value.intValue();
        result = result & maxValue();
        return new uint16(result);
    }

    @Override
    public uint16 minus(Number value) {
        int result = intValue() - value.intValue();
        result = result & maxValue();
        return new uint16(result);
    }

    @Override
    public uint16 times(Number value) {
        int result = intValue() * value.intValue();
        result = result & maxValue();
        return new uint16(result);
    }

    @Override
    public uint16 divide(Number value) {
        int result = intValue() / value.intValue();
        result = result & maxValue();
        return new uint16(result);
    }

    @Override
    public uint16 inc() {
        int result = intValue() + 1;
        result = result & maxValue();
        return new uint16(result);
    }

    @Override
    public uint16 dec() {
        int result = intValue() - 1;
        result = result & maxValue();
        return new uint16(result);
    }

    @Override
    public uint16 and(Number value) {
        int result = intValue() & value.intValue();
        result = result & maxValue();
        return new uint16(result);
    }

    @Override
    public uint16 xor(Number value) {
        int result = intValue() ^ value.intValue();
        result = result & maxValue();
        return new uint16(result);
    }

    @Override
    public uint16 or(Number value) {
        int result = intValue() | value.intValue();
        result = result & maxValue();
        return new uint16(result);
    }
}
