package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.CoreElement;
import com.meadowsapps.jgameboy.gbc.core.cartridge.GbcCartridge;
import com.meadowsapps.jgameboy.gbc.core.cpu.GbcCpu;
import com.meadowsapps.jgameboy.gbc.core.gpu.GbcGpu;
import com.meadowsapps.jgameboy.gbc.core.io.GbcJoypad;
import com.meadowsapps.jgameboy.gbc.core.mmu.GbcMmu;
import com.meadowsapps.jgameboy.gbc.core.ram.GbcRam;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface GbcCoreElement extends CoreElement, GbcConstants {

    GbcCore core();

    GbcCpu cpu();

    GbcGpu gpu();

    GbcRam ram();

    GbcMmu mmu();

    GbcCartridge cartridge();

    GbcJoypad joypad();
}
