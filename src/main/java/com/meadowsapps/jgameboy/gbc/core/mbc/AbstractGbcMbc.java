package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public abstract class AbstractGbcMbc implements MemoryBankController {

    private boolean hasRam;

    private boolean hasBattery;

    private GbcCartridge cartridge;

    public AbstractGbcMbc(boolean hasRam, boolean hasBattery) {
        this.hasRam = hasRam;
        this.hasBattery = hasBattery;
    }

    public boolean hasRam() {
        return hasRam;
    }

    public boolean hasBattery() {
        return hasBattery;
    }

    @Override
    public GbcCartridge getCartridge() {
        return cartridge;
    }

    @Override
    public void setCartridge(GbcCartridge cartridge) {
        this.cartridge = cartridge;
    }
}
