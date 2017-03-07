package com.meadowsapps.jgameboy.core.util

/**
 * Created by dmeadows on 3/7/2017.
 */
class Utilities {

    static String toHex(int i) {
        return "0x" + String.format("%2s", Integer.toHexString(i)).toUpperCase().replaceAll(' ', '0')
    }
}
