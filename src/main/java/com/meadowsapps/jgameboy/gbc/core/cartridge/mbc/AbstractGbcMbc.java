package com.meadowsapps.jgameboy.gbc.core.cartridge.mbc;

import com.meadowsapps.jgameboy.gbc.core.cartridge.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public abstract class AbstractGbcMbc implements MemoryBankController {

    private GbcCartridge cartridge;

    public AbstractGbcMbc(GbcCartridge cartridge) {
        this.cartridge = cartridge;
    }

    @Override
    public GbcCartridge cartridge() {
        return cartridge;
    }

}
