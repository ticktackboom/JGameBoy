package com.meadowsapps.jgameboy.core.util

import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/5/17.
 */
@InheritConstructors
class UInt32 extends UInt {

    static final int MAX_VALUE = 0xFFFFFFFF

    int maxValue() {
        return MAX_VALUE
    }

    @Override
    UInt32 plus(Number n) {
        int result = (value + n.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 minus(Number n) {
        int result = (value - n.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 multiply(Number n) {
        int result = (value * n.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 div(Number n) {
        int result = (value / n.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 mod(Number n) {
        int result = (value % n.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 power(Number n) {
        int result = (value**n.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 or(Number n) {
        int result = (value | n.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 and(Number n) {
        int result = (value & n.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 xor(Number n) {
        int result = (value ^ n.intValue()) as int
        return new UInt32(result)
    }

    @Override
    int getAt(Number bit) {
        return (value >> bit.intValue()) & 1
    }

    @Override
    void putAt(Number bit, Number set) {
        if (0 <= set.intValue() && set.intValue() <= 1) {
            putAt(bit, set.intValue() == 1)
        }
    }

    @Override
    void putAt(Number bit, boolean set) {
        if (set) {
            value |= (1 << bit.intValue())
        } else {
            value &= ~(1 << bit.intValue())
        }
    }

    @Override
    UInt32 leftShift(Number bits) {
        int result = (value << bits.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 rightShift(Number bits) {
        int result = (value >> bits.intValue()) as int
        return new UInt32(result)
    }

    @Override
    UInt32 next() {
        int result = (value + 1) as int
        return new UInt32(result)
    }

    @Override
    UInt32 previous() {
        int result = (value - 1) as int
        return new UInt32(result)
    }

}
