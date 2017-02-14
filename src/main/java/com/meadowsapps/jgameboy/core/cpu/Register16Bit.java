package com.meadowsapps.jgameboy.core.cpu;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register16Bit extends AbstractRegister {

    private int word;

    public Register16Bit() {
        word = 0;
    }

    @Override
    public int read() {
        return word;
    }

    @Override
    public void write(int value) {
        this.word = value & 0xFFFF;
    }

    public int word() {
        return word;
    }

    public void word(int word) {
        word &= 0xFFFF;
        this.word = word;
    }

    public int hi() {
        return word >> 8;
    }

    public void hi(int hi) {
        hi &= 0xFF;
        word = (hi << 8) + lo();
    }

    public int lo() {
        return word & 0xFF;
    }

    public void lo(int lo) {
        lo &= 0xFF;
        word = (hi() << 8) + lo;
    }

}
