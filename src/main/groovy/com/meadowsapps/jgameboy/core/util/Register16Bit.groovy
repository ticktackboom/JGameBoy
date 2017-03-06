package com.meadowsapps.jgameboy.core.util

/**
 * Created by dmeadows on 3/5/17.
 */
class Register16Bit {

    UInt16 word = new UInt16()

    UInt8 hi = new UInt8()

    UInt8 lo = new UInt8()

    private void setWord(Number n) {
        this.word = new UInt16(n.intValue())
        updateBytes()
    }

    private void setHi(Number n) {
        this.hi = new UInt8(n.intValue())
        updateWord()
    }

    private void setLo(Number n) {
        this.lo = new UInt8(n.intValue())
        updateWord()
    }

    private void updateWord() {
        int _hi = (hi.intValue() << 8)
        int _lo = lo.intValue()
        word = new UInt16(_hi + _lo)
    }

    private void updateBytes() {
        int _hi = (word.intValue() >> 8)
        hi = new UInt8(_hi)
        int _lo = (word.intValue() & 0xFF)
        lo = new UInt8(_lo)
    }
}
