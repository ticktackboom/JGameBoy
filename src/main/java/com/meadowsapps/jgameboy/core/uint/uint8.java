package com.meadowsapps.jgameboy.core.uint;

/**
 * Created by Dylan on 2/6/17.
 */
public class uint8 extends uint {

    public static final int MAX_VALUE = 0xFF;

    public uint8() {
        this(0);
    }

    public uint8(int value) {
        super(value);
    }

    @Override
    public int maxValue() {
        return MAX_VALUE;
    }

    @Override
    public uint8 plus(Number value) {
        int result = intValue() + value.intValue();
        result = result & maxValue();
        return new uint8(result);
    }

    @Override
    public uint8 minus(Number value) {
        int result = intValue() - value.intValue();
        result = result & maxValue();
        return new uint8(result);
    }

    @Override
    public uint8 times(Number value) {
        int result = intValue() * value.intValue();
        result = result & maxValue();
        return new uint8(result);
    }

    @Override
    public uint8 divide(Number value) {
        int result = intValue() / value.intValue();
        result = result & maxValue();
        return new uint8(result);
    }

    @Override
    public uint8 inc() {
        int result = intValue() + 1;
        result = result & maxValue();
        return new uint8(result);
    }

    @Override
    public uint8 dec() {
        int result = intValue() - 1;
        result = result & maxValue();
        return new uint8(result);
    }

    @Override
    public uint8 and(Number value) {
        int result = intValue() & value.intValue();
        result = result & maxValue();
        return new uint8(result);
    }

    @Override
    public uint8 xor(Number value) {
        int result = intValue() ^ value.intValue();
        result = result & maxValue();
        return new uint8(result);
    }

    @Override
    public uint8 or(Number value) {
        int result = intValue() | value.intValue();
        result = result & maxValue();
        return new uint8(result);
    }
}
