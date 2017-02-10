package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.AbstractCoreElement;

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
    public GbcMmu mmu() {
        return (GbcMmu) super.mmu();
    }

    @Override
    public GbcDisplay display() {
        return (GbcDisplay) super.display();
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
