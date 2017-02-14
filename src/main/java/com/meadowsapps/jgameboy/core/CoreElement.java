package com.meadowsapps.jgameboy.core;

import com.meadowsapps.jgameboy.core.cartridge.Cartridge;
import com.meadowsapps.jgameboy.core.cpu.Cpu;
import com.meadowsapps.jgameboy.core.gpu.Gpu;
import com.meadowsapps.jgameboy.core.io.Joypad;
import com.meadowsapps.jgameboy.core.mmu.Mmu;
import com.meadowsapps.jgameboy.core.ram.Ram;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface CoreElement extends Constants {

    void initialize();

    void reset();

    EmulatorCore core();

    Cpu cpu();

    Gpu gpu();

    Ram ram();

    Mmu mmu();

    Cartridge cartridge();

    Joypad joypad();

}
