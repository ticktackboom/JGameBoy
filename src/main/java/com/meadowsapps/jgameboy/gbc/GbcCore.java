package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.core.EmulatorCore;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class GbcCore implements EmulatorCore {

    private DmgCpu cpu;

    private GbcMmu mmu;

    public GbcCore() {
        cpu = new DmgCpu(this);
        mmu = new GbcMmu(this);
    }

    @Override
    public DmgCpu getCpu() {
        return cpu;
    }

    @Override
    public GbcMmu getMmu() {
        return mmu;
    }
}
