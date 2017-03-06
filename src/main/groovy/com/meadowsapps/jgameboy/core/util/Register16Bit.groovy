package com.meadowsapps.jgameboy.core.util

/**
 * Created by dmeadows on 3/5/17.
 */
class Register16Bit {

    UInt16 word = new UnionUInt16()

    UInt8 hi = new UnionUInt8()

    UInt8 lo = new UnionUInt8()

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

    private class UnionUInt8 extends UInt8 {

        @Override
        void putAt(Number bit, boolean set) {
            super.putAt(bit, set)
            updateWord()
        }
    }

    private class UnionUInt16 extends UInt16 {

        @Override
        void putAt(Number bit, boolean set) {
            super.putAt(bit, set)
            updateBytes()
        }
    }
}
