package com.meadowsapps.jgameboy.core.uint;

/**
 * Created by Dylan on 2/6/17.
 */
public class uint32 extends uint {

    public static final int MAX_VALUE = 0xFFFFFF;

    public uint32() {
        this(0);
    }

    public uint32(int value) {
        super(value);
    }

    @Override
    public int maxValue() {
        return MAX_VALUE;
    }

    @Override
    public uint32 plus(Number value) {
        int result = intValue() + value.intValue();
        result = result & maxValue();
        return new uint32(result);
    }

    @Override
    public uint32 minus(Number value) {
        int result = intValue() - value.intValue();
        result = result & maxValue();
        return new uint32(result);
    }

    @Override
    public uint32 times(Number value) {
        int result = intValue() * value.intValue();
        result = result & maxValue();
        return new uint32(result);
    }

    @Override
    public uint32 divide(Number value) {
        int result = intValue() / value.intValue();
        result = result & maxValue();
        return new uint32(result);
    }

    @Override
    public uint32 inc() {
        int result = intValue() + 1;
        result = result & maxValue();
        return new uint32(result);
    }

    @Override
    public uint32 dec() {
        int result = intValue() - 1;
        result = result & maxValue();
        return new uint32(result);
    }

    @Override
    public uint32 and(Number value) {
        int result = intValue() & value.intValue();
        result = result & maxValue();
        return new uint32(result);
    }

    @Override
    public uint32 xor(Number value) {
        int result = intValue() ^ value.intValue();
        result = result & maxValue();
        return new uint32(result);
    }

    @Override
    public uint32 or(Number value) {
        int result = intValue() | value.intValue();
        result = result & maxValue();
        return new uint32(result);
    }
}
