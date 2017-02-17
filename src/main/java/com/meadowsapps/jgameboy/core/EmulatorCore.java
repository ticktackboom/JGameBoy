package com.meadowsapps.jgameboy.core;

import com.meadowsapps.jgameboy.core.apu.Apu;
import com.meadowsapps.jgameboy.core.cartridge.Cartridge;
import com.meadowsapps.jgameboy.core.cpu.Cpu;
import com.meadowsapps.jgameboy.core.gpu.Gpu;
import com.meadowsapps.jgameboy.core.io.Joypad;
import com.meadowsapps.jgameboy.core.mmu.Mmu;
import com.meadowsapps.jgameboy.core.ram.Ram;

/**
 * Created by dmeadows on 1/17/2017.
 */
public interface EmulatorCore extends Constants, Runnable {

    void initialize();

    void reset();

    void start();

    void stop();

    boolean isRunning();

    Cpu cpu();

    Apu apu();

    Gpu gpu();

    Ram ram();

    Mmu mmu();

    Cartridge cartridge();

    Joypad joypad();

    double getScale();

}
