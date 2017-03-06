package com.meadowsapps.jgameboy.gbc

import com.meadowsapps.jgameboy.core.util.UInt32

/**
 * Created by dmeadows on 3/5/17.
 */
class Clock {

    UInt32 m = new UInt32()

    UInt32 t = new UInt32()

    private void setM(Number n) {
        m = new UInt32(n.intValue())
    }

    private void setT(Number n) {
        t = new UInt32(n.intValue())
    }

}
