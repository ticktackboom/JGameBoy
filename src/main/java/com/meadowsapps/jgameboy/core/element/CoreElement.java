package com.meadowsapps.jgameboy.core.element;

import com.meadowsapps.jgameboy.core.EmulatorCore;
import com.meadowsapps.jgameboy.core.element.apu.Apu;
import com.meadowsapps.jgameboy.core.element.cartridge.Cartridge;
import com.meadowsapps.jgameboy.core.element.cpu.Cpu;
import com.meadowsapps.jgameboy.core.element.gpu.Gpu;
import com.meadowsapps.jgameboy.core.element.io.Joypad;
import com.meadowsapps.jgameboy.core.element.mmu.Mmu;
import com.meadowsapps.jgameboy.core.util.Constants;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface CoreElement extends Constants {

    void initialize() throws InitializationException;

    void reset();

    EmulatorCore core();

    Cpu cpu();

    Apu apu();

    Gpu gpu();

    Mmu mmu();

    Cartridge cartridge();

    Joypad joypad();

}
