package com.meadowsapps.jgameboy.core;

import com.meadowsapps.jgameboy.core.apu.Apu;
import com.meadowsapps.jgameboy.core.cartridge.Cartridge;
import com.meadowsapps.jgameboy.core.cpu.Cpu;
import com.meadowsapps.jgameboy.core.gpu.Gpu;
import com.meadowsapps.jgameboy.core.io.Joypad;
import com.meadowsapps.jgameboy.core.mmu.Mmu;
import com.meadowsapps.jgameboy.core.ram.Ram;

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
    public Ram ram() {
        return core.ram();
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
