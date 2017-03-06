package com.meadowsapps.jgameboy.core.util

import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/4/17.
 */
@InheritConstructors
class UInt8 extends UInt {

    static final int MAX_VALUE = 0xFF

    @Override
    int maxValue() {
        return MAX_VALUE
    }

    @Override
    UInt8 plus(Number n) {
        int result = (value + n.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 minus(Number n) {
        int result = (value - n.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 multiply(Number n) {
        int result = (value * n.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 div(Number n) {
        int result = (value / n.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 mod(Number n) {
        int result = (value % n.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 power(Number n) {
        int result = (value**n.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 or(Number n) {
        int result = (value | n.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 and(Number n) {
        int result = (value & n.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 xor(Number n) {
        int result = (value ^ n.intValue()) as int
        return new UInt8(result)
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
    UInt8 leftShift(Number bits) {
        int result = (value << bits.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 rightShift(Number bits) {
        int result = (value >> bits.intValue()) as int
        return new UInt8(result)
    }

    @Override
    UInt8 next() {
        int result = (value + 1) as int
        return new UInt8(result)
    }

    @Override
    UInt8 previous() {
        int result = (value - 1) as int
        return new UInt8(result)
    }

}
