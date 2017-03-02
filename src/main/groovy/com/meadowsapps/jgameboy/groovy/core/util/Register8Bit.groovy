package com.meadowsapps.jgameboy.groovy.core.util

import groovy.transform.CompileStatic

/**
 * Created by dmeadows on 3/2/2017.
 */
@CompileStatic
class Register8Bit {

    UInt8 value

    Register8Bit() {
    }

    Register8Bit(Number n) {
        value = new UInt8(n.intValue())
    }

    @Override
    String toString() {
        return "Register8Bit{" +
                "value=" + value +
                '}'
    }

    void setValue(final Number n) {
        value = new UInt8(n.intValue())
    }

    def plus(final Number n) {
        int result = value.intValue() + n.intValue()
        return new Register8Bit(result)
    }

    def minus(final Number n) {
        int result = value.intValue() - n.intValue()
        return new Register8Bit(result)
    }

    def multiply(final Number n) {
        int result = value.intValue() * n.intValue()
        return new Register8Bit(result)
    }

    def div(final Number n) {
        int result = (int) (value.intValue() / n.intValue())
        return new Register8Bit(result)
    }

    def mod(final Number n) {
        int result = value.intValue() % n.intValue()
        return new Register8Bit(result)
    }

    def power(final Number n) {
        int result = (int) (value.intValue()**n.intValue())
        return new Register8Bit(result)
    }

    def or(final Number n) {
        int result = value.intValue() | n.intValue()
        return new Register8Bit(result)
    }

    def and(final Number n) {
        int result = value.intValue() & n.intValue()
        return new Register8Bit(result)
    }

    def xor(final Number n) {
        int result = value.intValue() ^ n.intValue()
        return new Register8Bit(result)
    }

    def getAt(int bit) {
        return (value.intValue() >> bit) & 1
    }

    def putAt(int bit, int set) {
        if (0 <= set && set <= 1) {
            return putAt(bit, set == 1)
        } else {
            return this
        }
    }

    def putAt(int bit, boolean set) {
        int result
        if (set) {
            result = value.intValue() | (1 << bit)
        } else {
            result = value.intValue() & ~(1 << bit)
        }
        return new Register8Bit(result)
    }

    def leftShift(final Number n) {
        int result = value.intValue() << n.intValue()
        return new Register8Bit(result)
    }

    def rightShift(final Number n) {
        int result = value.intValue() >> n.intValue()
        return new Register8Bit(result)
    }

    def next() {
        int result = value.intValue() + 1
        return new Register8Bit(result)
    }

    def previous() {
        int result = value.intValue() - 1
        return new Register8Bit(result)
    }
}
