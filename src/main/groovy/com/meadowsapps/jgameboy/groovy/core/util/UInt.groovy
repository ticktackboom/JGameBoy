package com.meadowsapps.jgameboy.groovy.core.util

import groovy.transform.CompileStatic

/**
 * Created by dmeadows on 3/2/2017.
 */
abstract class UInt extends Number {

    private int value

    UInt() {
        this(0)
    }

    UInt(int value) {
        value &= maxValue()
        this.value = value
    }

    @Override
    int intValue() {
        return value
    }

    @Override
    long longValue() {
        return value
    }

    @Override
    float floatValue() {
        return value
    }

    @Override
    double doubleValue() {
        return value
    }

    @Override
    String toString() {
        return String.format("%s{value=%d}", this.class.simpleName, value)
    }

    protected abstract def newInstance(int value)

    abstract int maxValue()

    def plus(final Number n) {
        int result = value + n.intValue()
        return newInstance(result)
    }

    def minus(final Number n) {
        int result = value - n.intValue()
        return newInstance(result)
    }

    def multiply(final Number n) {
        int result = value * n.intValue()
        return newInstance(result)
    }

    def div(final Number n) {
        int result = (int) (value / n.intValue())
        return newInstance(result)
    }

    def mod(final Number n) {
        int result = value % n.intValue()
        return newInstance(result)
    }

    def power(final Number n) {
        int result = (int) (value**n.intValue())
        return newInstance(result)
    }

    def or(final Number n) {
        int result = value | n.intValue()
        return newInstance(result)
    }

    def and(final Number n) {
        int result = value & n.intValue()
        return newInstance(result)
    }

    def xor(final Number n) {
        int result = value ^ n.intValue()
        return newInstance(result)
    }

    def getAt(int bit) {
        return (value >> bit) & 1
    }

    def putAt(int bit, int set) {
        if (0 <= set && set <= 1) {
            return putAt(bit, set == 1)
        } else {
            return this
        }
    }

    def putAt(int bit, boolean set) {
        int result;
        if (set) {
            result = value | (1 << bit)
        } else {
            result = value & ~(1 << bit)
        }
        return newInstance(result)
    }

    def leftShift(final Number n) {
        int result = value << n.intValue()
        return newInstance(result)
    }

    def rightShift(final Number n) {
        int result = value >> n.intValue()
        return newInstance(result)
    }

    def next() {
        int result = value + 1
        return newInstance(result)
    }

    def previous() {
        int result = value - 1
        return newInstance(result)
    }

}
