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
    public int read(int addr) {
        Mmu mmu = core.getMmu();
        return mmu.read(addr);
    }

    @Override
    public void write(int value, int addr) {
        Mmu mmu = core.getMmu();
        mmu.write(value, addr);
    }

    public EmulatorCore getCore() {
        return core;
    }
}
