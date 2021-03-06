package com.meadowsapps.jgameboy.gbc.core.element.cartridge.mbc;

import com.meadowsapps.jgameboy.gbc.core.element.cartridge.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public interface MemoryBankController {

    void initialize(byte[] contents);

    int read(int addr);

    void write(int value, int addr);

    GbcCartridge cartridge();

}
