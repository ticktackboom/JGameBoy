package com.meadowsapps.jgameboy.gbc.core.element;

import com.meadowsapps.jgameboy.core.element.CoreElement;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;
import com.meadowsapps.jgameboy.gbc.core.element.apu.GbcApu;
import com.meadowsapps.jgameboy.gbc.core.element.cartridge.GbcCartridge;
import com.meadowsapps.jgameboy.gbc.core.element.cpu.GbcCpu;
import com.meadowsapps.jgameboy.gbc.core.element.gpu.GbcGpu;
import com.meadowsapps.jgameboy.gbc.core.element.io.GbcJoypad;
import com.meadowsapps.jgameboy.gbc.core.element.mmu.GbcMmu;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface GbcCoreElement extends CoreElement, GbcConstants {

    GbcCore core();

    GbcCpu cpu();

    GbcApu apu();

    GbcGpu gpu();

    GbcMmu mmu();

    GbcCartridge cartridge();

    GbcJoypad joypad();
}
