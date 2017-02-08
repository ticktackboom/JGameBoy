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
    public GbcCartridge cartridge() {
        return (GbcCartridge) super.cartridge();
    }
}
