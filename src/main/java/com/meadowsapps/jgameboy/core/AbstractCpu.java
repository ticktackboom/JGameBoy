package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/17/2017.
 */
public abstract class AbstractCpu implements Cpu {

    private EmulatorCore core;

    protected AbstractCpu(EmulatorCore core) {
        this.core = core;
    }

    @Override
    public int readByte(int addr) {
        Mmu mmu = core.getMmu();
        return mmu.readByte(addr);
    }

    @Override
    public int readWord(int addr) {
        Mmu mmu = core.getMmu();
        return mmu.readWord(addr);
    }

    @Override
    public void writeByte(int value, int addr) {
        Mmu mmu = core.getMmu();
        mmu.writeByte(value, addr);
    }

    @Override
    public void writeWord(int value, int addr) {
        Mmu mmu = core.getMmu();
        mmu.writeWord(value, addr);
    }

    @Override
    public EmulatorCore getCore() {
        return core;
    }
}
