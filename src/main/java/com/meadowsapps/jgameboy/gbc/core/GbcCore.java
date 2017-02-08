package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.EmulatorCore;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class GbcCore implements EmulatorCore {

    private final GbcCpu cpu;

    private final GbcGpu gpu;

    private final GbcMmu mmu;

    private final GbcCartridge cartridge;

    public GbcCore() {
        cpu = new GbcCpu(this);
        gpu = new GbcGpu(this);
        mmu = new GbcMmu(this);
        cartridge = new GbcCartridge(this);
    }

    @Override
    public GbcCpu cpu() {
        return cpu;
    }

    @Override
    public GbcGpu gpu() {
        return gpu;
    }

    @Override
    public GbcMmu mmu() {
        return mmu;
    }

    @Override
    public GbcCartridge cartridge() {
        return cartridge;
    }
}
