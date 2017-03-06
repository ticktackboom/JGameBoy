package com.meadowsapps.jgameboy.core.util

/**
 * Created by dmeadows on 3/5/17.
 */
class Register8Bit {

    UInt8 value

    private void setValue(Number n) {
        value = new UInt8(n.intValue())
    }

}
