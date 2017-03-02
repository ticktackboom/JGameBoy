package com.meadowsapps.jgameboy.groovy.gbc.element.mmu

import com.meadowsapps.jgameboy.groovy.core.element.mmu.MemoryAccessException
import com.meadowsapps.jgameboy.groovy.core.element.mmu.Mmu
import com.meadowsapps.jgameboy.groovy.core.util.InitializationException
import com.meadowsapps.jgameboy.groovy.core.util.UInt16
import com.meadowsapps.jgameboy.groovy.core.util.UInt8
import com.meadowsapps.jgameboy.groovy.gbc.element.GbcCoreElement
import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/2/2017.
 */
@InheritConstructors
class GbcMmu extends GbcCoreElement implements Mmu<UInt16, UInt8> {

    private UInt8[] bios

    private UInt8[] ram

    private UInt8[] vram

    private UInt8[] oam

    private UInt8[] hram

    private UInt8[] io

    private UInt8 ie

    @Override
    void initialize() throws InitializationException {
        try {

        } catch (Exception e) {
            throw new InitializationException(e)
        }
    }

    @Override
    void reset() {

    }

    UInt8 readByte(UInt16 addr) throws MemoryAccessException {

    }

    void writeByte(UInt16 addr, UInt8 data) throws MemoryAccessException {

    }

    UInt16 readWord(UInt16 addr) throws MemoryAccessException {
        int lo = read(addr).intValue()
        int hi = read(addr + 1).intValue()
        return new UInt16(hi + lo)
    }

    void writeWord(UInt16 addr, UInt16 data) throws MemoryAccessException {
        UInt8 lo = new UInt8(data.intValue() & 0xFF)
        write(lo, addr)
        UInt8 hi = new UInt8(data.intValue() >> 8)
        write(hi, addr + 1)
    }

}
