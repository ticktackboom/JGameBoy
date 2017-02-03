package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by dmeadows on 2/3/2017.
 */
public interface MemoryBankController {

    int read(int addr, byte[] contents);

    void write(int value, int addr);
}
