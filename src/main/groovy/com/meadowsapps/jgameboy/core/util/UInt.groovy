package com.meadowsapps.jgameboy.core.util

import groovy.transform.Canonical
import groovy.transform.PackageScope

/**
 * Created by dmeadows on 3/4/17.
 */
@Canonical
@PackageScope
abstract class UInt extends Number {

    int value

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

    abstract int maxValue()

    abstract plus(Number n)

    abstract minus(Number n)

    abstract multiply(Number n)

    abstract div(Number n)

    abstract mod(Number n)

    abstract power(Number n)

    abstract or(Number n)

    abstract and(Number n)

    abstract xor(Number n)

    abstract int getAt(Number bit)

    abstract void putAt(Number bit, Number set)

    abstract void putAt(Number bit, boolean set)

    abstract leftShift(Number bits)

    abstract rightShift(Number bits)

    abstract next()

    abstract previous()

    abstract bitwiseNegate()

    boolean equals(Number n) {
        return value == n.intValue()
    }

}
