package com.meadowsapps.jgameboy.groovy.core.util

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/2/2017.
 */
@CompileStatic
@InheritConstructors
class UInt16 extends UInt {

    static final int MAX_VALUE = 0xFFFF

    @Override
    protected newInstance(int value) {
        return new UInt16(value)
    }

    @Override
    int maxValue() {
        return MAX_VALUE
    }
}
