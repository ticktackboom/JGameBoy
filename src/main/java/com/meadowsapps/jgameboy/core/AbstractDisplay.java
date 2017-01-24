package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/24/2017.
 */
public abstract class AbstractDisplay implements Display {

    private EmulatorCore core;

    protected AbstractDisplay(EmulatorCore core) {
        this.core = core;
    }

    @Override
    public int read(int addr) {
        Mmu mmu = core.getMmu();
        return mmu.readByte(addr);
    }

    @Override
    public void write(int value, int addr) {
        Mmu mmu = core.getMmu();
        mmu.writeByte(value, addr);
    }
}
