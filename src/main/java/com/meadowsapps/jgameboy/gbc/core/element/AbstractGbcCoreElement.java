package com.meadowsapps.jgameboy.gbc.core.element;

import com.meadowsapps.jgameboy.core.element.AbstractCoreElement;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;
import com.meadowsapps.jgameboy.gbc.core.element.apu.GbcApu;
import com.meadowsapps.jgameboy.gbc.core.element.cartridge.GbcCartridge;
import com.meadowsapps.jgameboy.gbc.core.element.cpu.GbcCpu;
import com.meadowsapps.jgameboy.gbc.core.element.gpu.GbcGpu;
import com.meadowsapps.jgameboy.gbc.core.element.io.GbcJoypad;
import com.meadowsapps.jgameboy.gbc.core.element.mmu.GbcMmu;

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
    public GbcApu apu() {
        return (GbcApu) super.apu();
    }

    @Override
    public GbcGpu gpu() {
        return (GbcGpu) super.gpu();
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
