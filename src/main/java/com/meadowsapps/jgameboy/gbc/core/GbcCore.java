package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.EmulatorCore;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class GbcCore implements EmulatorCore {

    private final GbcCpu cpu;

    private final GbcMmu mmu;

    private final GbcCartridge cartridge;

    public GbcCore() {
        cpu = new GbcCpu(this);
        mmu = new GbcMmu(this);
        cartridge = new GbcCartridge(this);
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
    public GbcCartridge getCartridge() {
        return cartridge;
    }
}
