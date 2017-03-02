package com.meadowsapps.jgameboy.groovy.core.util

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/2/2017.
 */
@CompileStatic
@InheritConstructors
class UInt8 extends UInt {

    static final int MAX_VALUE = 0xFF

    @Override
    protected newInstance(int value) {
        return new UInt8(value)
    }

    @Override
    int maxValue() {
        return MAX_VALUE
    }
}
