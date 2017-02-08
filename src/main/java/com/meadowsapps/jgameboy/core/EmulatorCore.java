package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/17/2017.
 */
public interface EmulatorCore {

    Cpu cpu();

    Gpu gpu();

    Mmu mmu();

    Display display();

    Cartridge cartridge();

    Joypad joypad();

}
