package com.meadowsapps.jgameboy.core.element;

import com.meadowsapps.jgameboy.core.EmulatorCore;
import com.meadowsapps.jgameboy.core.element.apu.Apu;
import com.meadowsapps.jgameboy.core.element.cartridge.Cartridge;
import com.meadowsapps.jgameboy.core.element.cpu.Cpu;
import com.meadowsapps.jgameboy.core.element.gpu.Gpu;
import com.meadowsapps.jgameboy.core.element.io.Joypad;
import com.meadowsapps.jgameboy.core.element.mmu.Mmu;

/**
 * Created by Dylan on 2/7/17.
 */
public abstract class AbstractCoreElement implements CoreElement {

    private EmulatorCore core;

    public AbstractCoreElement(EmulatorCore core) {
        this.core = core;
    }

    @Override
    public EmulatorCore core() {
        return core;
    }

    @Override
    public Cpu cpu() {
        return core.cpu();
    }

    @Override
    public Apu apu() {
        return core.apu();
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

    @Override
    public Joypad joypad() {
        return core.joypad();
    }

}
