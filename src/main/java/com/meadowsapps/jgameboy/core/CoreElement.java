package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface CoreElement extends Constants {

    void initialize();

    void reset();

    EmulatorCore core();

    Cpu cpu();

    Gpu gpu();

    Mmu mmu();

    Display display();

    Cartridge cartridge();

    Joypad joypad();

}
