package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.EmulatorCore;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class GbcCore implements EmulatorCore {

    private final GbcCpu cpu;

    private final GbcMmu mmu;

    private final GbcDisplay display;

    public GbcCore() {
        cpu = new GbcCpu(this);
        mmu = new GbcMmu(this);
        display = new GbcDisplay(this);
    }

    @Override
    public GbcCpu getCpu() {
        return cpu;
    }

    @Override
    public GbcMmu getMmu() {
        return mmu;
    }

    @Override
    public GbcDisplay getDisplay() {
        return display;
    }
}
