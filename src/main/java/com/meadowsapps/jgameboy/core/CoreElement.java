package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface CoreElement extends Constants {

    Cpu cpu();

    Gpu gpu();

    Mmu mmu();

    Display display();

    Cartridge cartridge();

    Joypad joypad();

}
