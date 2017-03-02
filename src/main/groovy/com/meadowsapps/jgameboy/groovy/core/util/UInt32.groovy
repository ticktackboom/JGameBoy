package com.meadowsapps.jgameboy.groovy.core.util

import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/2/2017.
 */
@InheritConstructors
class UInt32 extends UInt {

    static final int MAX_VALUE = 0xFFFFFFFF

    @Override
    protected newInstance(int value) {
        return new UInt32(value)
    }

    @Override
    int maxValue() {
        return MAX_VALUE
    }
}
