package com.meadowsapps.jgameboy.groovy.core.util

import groovy.transform.CompileStatic

/**
 * Created by dmeadows on 3/2/2017.
 */
@CompileStatic
class Register16Bit {

    UInt16 word

    UInt8 hi, lo

    Register16Bit() {
    }

    Register16Bit(Number n) {
        word = new UInt16(n.intValue())
        hi = new UInt8(n.intValue() >> 8)
        lo = new UInt8(n.intValue() & 0xFF)
    }

    @Override
    String toString() {
        return "Register16Bit{" +
                "word=" + word +
                ", hi=" + hi +
                ", lo=" + lo +
                '}'
    }

    void setWord(final Number n) {
        this.word = new UInt16(n.intValue())
        updateBytes()
    }

    void setHi(final Number n) {
        this.hi = new UInt8(n.intValue())
        updateWord()
    }

    void setLo(final Number n) {
        this.lo = new UInt8(n.intValue())
        updateWord()
    }

    private void updateWord() {
        if (hi != null && lo != null) {
            int hi = (this.hi.intValue() << 8)
            int lo = (this.lo.intValue() & 0xFF)
            word = new UInt16(hi + lo)
        }
    }

    private void updateBytes() {
        if (word != null) {
            int hi = word.intValue() >> 8
            this.hi = new UInt8(hi)
            int lo = word.intValue() & 0xFF
            this.lo = new UInt8(lo)
        }
    }

}