package com.meadowsapps.jgameboy;

public class UInt8 extends Number {

    private int value;

    public static final int MIN_VALUE = 0;

    public static final int MAX_VALUE = 256;

    public UInt8(int value) {
        this.value = value;
    }

    public UInt8(String value) {
        this(Integer.parseInt(value));
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof UInt8) && ((UInt8) obj).value == value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}