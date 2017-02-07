package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public interface MemoryBankController {

    int read(int addr);

    void write(int value, int addr);

    GbcCartridge cartridge();

}
