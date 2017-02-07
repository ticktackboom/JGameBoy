package com.meadowsapps.jgameboy.core.uint;

/**
 * Created by Dylan on 2/6/17.
 */
public abstract class uint extends Number {

    private int value;

    public uint(int value) {
        value &= maxValue();
        this.value = value;
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
    public String toString() {
        return getClass().getSimpleName() + '{' +
                "value=" + value +
                '}';
    }

    public abstract int maxValue();

    public abstract uint plus(Number value);

    public abstract uint minus(Number value);

    public abstract uint times(Number value);

    public abstract uint divide(Number value);

    public abstract uint inc();

    public abstract uint dec();

    public abstract uint and(Number value);

    public abstract uint xor(Number value);

    public abstract uint or(Number value);

}
