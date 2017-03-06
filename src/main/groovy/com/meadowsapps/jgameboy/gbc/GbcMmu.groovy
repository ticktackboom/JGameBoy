package com.meadowsapps.jgameboy.gbc

import com.meadowsapps.jgameboy.core.util.UInt16
import com.meadowsapps.jgameboy.core.util.UInt8

/**
 * Created by dmeadows on 3/5/17.
 */
class GbcMmu {

    UInt8[] memory = new UInt8[0xFFFF]
    
    UInt8 readByte(UInt16 addr) {
        UInt8 rv = memory[addr.intValue()]
        if (rv == null) {
            rv = new UInt8()
        }
        return rv
    }

    void writeByte(UInt16 addr, UInt8 data) {
        memory[addr.intValue()] = data
    }

    UInt16 readWord(UInt16 addr) {
        UInt8 lo = readByte(addr)
        UInt8 hi = readByte(addr + 1)
        return UInt16.combine(hi, lo)
    }

    void writeWord(UInt16 addr, UInt16 data) {
        writeByte(addr, UInt16.lo(data))
        writeByte(addr + 1, UInt16.hi(data))
    }

}
