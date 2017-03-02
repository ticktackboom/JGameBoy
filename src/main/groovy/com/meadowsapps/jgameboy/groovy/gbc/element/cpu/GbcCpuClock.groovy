package com.meadowsapps.jgameboy.groovy.gbc.element.cpu

import com.meadowsapps.jgameboy.groovy.core.util.UInt32

/**
 * Created by dmeadows on 3/2/2017.
 */
class GbcCpuClock {

    UInt32 m = new UInt32()

    UInt32 t = new UInt32()

    UInt32 getM() {
        return m
    }

    void setM(Number n) {
        this.m = new UInt32(n.intValue())
    }

    UInt32 getT() {
        return t
    }

    void setT(Number n) {
        this.t = new UInt32(n.intValue())
    }
}
