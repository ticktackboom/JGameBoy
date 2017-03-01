package com.meadowsapps.jgameboy.core;

import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.core.element.apu.Apu;
import com.meadowsapps.jgameboy.core.element.cartridge.Cartridge;
import com.meadowsapps.jgameboy.core.element.cpu.Cpu;
import com.meadowsapps.jgameboy.core.element.gpu.Gpu;
import com.meadowsapps.jgameboy.core.element.io.Joypad;
import com.meadowsapps.jgameboy.core.element.mmu.Mmu;
import com.meadowsapps.jgameboy.core.util.Constants;

/**
 * Created by dmeadows on 1/17/2017.
 */
public interface EmulatorCore extends Constants {

    void initialize() throws InitializationException;

    void reset();

    void start();

    void stop();

    Cpu cpu();

    Apu apu();

    Gpu gpu();

    Mmu mmu();

    Cartridge cartridge();

    Joypad joypad();

    double getScale();

    boolean isRunning();

}
