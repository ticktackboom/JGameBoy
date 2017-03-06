package com.meadowsapps.jgameboy.core.util

import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/4/17.
 */
@InheritConstructors
class UInt16 extends UInt {

    static final int MAX_VALUE = 0xFFFF

    @Override
    int maxValue() {
        return MAX_VALUE
    }

    @Override
    UInt16 plus(Number n) {
        int result = (value + n.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 minus(Number n) {
        int result = (value - n.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 multiply(Number n) {
        int result = (value * n.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 div(Number n) {
        int result = (value / n.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 mod(Number n) {
        int result = (value % n.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 power(Number n) {
        int result = (value**n.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 or(Number n) {
        int result = (value | n.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 and(Number n) {
        int result = (value & n.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 xor(Number n) {
        int result = (value ^ n.intValue()) as int
        return new UInt16(result)
    }

    @Override
    int getAt(Number bit) {
        return (value >> bit.intValue()) & 1
    }

    @Override
    UInt16 putAt(Number bit, Number set) {
        UInt16 rv = this
        if (0 <= set.intValue() && set.intValue() <= 1) {
            rv = putAt(bit, set.intValue() == 1)
        }
        return rv
    }

    @Override
    UInt16 putAt(Number bit, boolean set) {
        int result;
        if (set) {
            result = value | (1 << bit.intValue())
        } else {
            result = value & ~(1 << bit.intValue())
        }
        return new UInt16(result)
    }

    @Override
    UInt16 leftShift(Number bits) {
        int result = (value << bits.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 rightShift(Number bits) {
        int result = (value >> bits.intValue()) as int
        return new UInt16(result)
    }

    @Override
    UInt16 next() {
        int result = (value + 1) as int
        return new UInt16(result)
    }

    @Override
    UInt16 previous() {
        int result = (value - 1) as int
        return new UInt16(result)
    }

    static UInt16 combine(Number hi, Number lo) {
        int _hi = ((hi.intValue() & 0xFF) >> 8)
        int _lo = (lo.intValue() & 0xFF)
        return new UInt16(_hi + _lo)
    }

    static UInt8 hi(UInt16 u) {
        int result = (u.intValue() >> 8)
        return new UInt8(result)
    }

    static UInt8 lo(UInt16 u) {
        int result = (u.intValue() & 0xFF)
        return new UInt8(result)
    }
}
