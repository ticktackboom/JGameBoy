package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.AbstractCoreElement;
import com.meadowsapps.jgameboy.gbc.core.cartridge.GbcCartridge;
import com.meadowsapps.jgameboy.gbc.core.cpu.GbcCpu;
import com.meadowsapps.jgameboy.gbc.core.gpu.GbcGpu;
import com.meadowsapps.jgameboy.gbc.core.io.GbcJoypad;
import com.meadowsapps.jgameboy.gbc.core.mmu.GbcMmu;
import com.meadowsapps.jgameboy.gbc.core.ram.GbcRam;

/**
 * Created by Dylan on 2/7/17.
 */
public abstract class AbstractGbcCoreElement extends AbstractCoreElement implements GbcCoreElement {

    public AbstractGbcCoreElement(GbcCore core) {
        super(core);
    }

    @Override
    public GbcCore core() {
        return (GbcCore) super.core();
    }

    @Override
    public GbcCpu cpu() {
        return (GbcCpu) super.cpu();
    }

    @Override
    public GbcGpu gpu() {
        return (GbcGpu) super.gpu();
    }

    @Override
    public GbcRam ram() {
        return (GbcRam) super.ram();
    }

    @Override
    public GbcMmu mmu() {
        return (GbcMmu) super.mmu();
    }

    @Override
    public GbcCartridge cartridge() {
        return (GbcCartridge) super.cartridge();
    }

    @Override
    public GbcJoypad joypad() {
        return (GbcJoypad) super.joypad();
    }
}
