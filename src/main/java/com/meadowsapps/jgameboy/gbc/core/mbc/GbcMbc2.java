package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc2 extends AbstractGbcMbc {

    public GbcMbc2(GbcCartridge cartridge) {
        super(cartridge);
    }

    @Override
    public int read(int addr) {
        return 0;
    }

    @Override
    public void write(int value, int addr) {

    }
}