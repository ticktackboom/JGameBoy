package com.meadowsapps.jgameboy.core;

/**
 * Created by Dylan on 2/7/17.
 */
public abstract class AbstractCoreElement implements CoreElement {

    private EmulatorCore core;

    public AbstractCoreElement(EmulatorCore core) {
        this.core = core;
    }

    @Override
    public Cpu cpu() {
        return core.cpu();
    }

    @Override
    public Gpu gpu() {
        return core.gpu();
    }

    @Override
    public Mmu mmu() {
        return core.mmu();
    }

    @Override
    public Cartridge cartridge() {
        return core.cartridge();
    }
}
