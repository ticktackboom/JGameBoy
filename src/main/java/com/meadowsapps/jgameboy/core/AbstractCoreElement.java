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
    public Display display() {
        return core.display();
    }

    @Override
    public Cartridge cartridge() {
        return core.cartridge();
    }

    @Override
    public Joypad joypad() {
        return core.joypad();
    }
}
