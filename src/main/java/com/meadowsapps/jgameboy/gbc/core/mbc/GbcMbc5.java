package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc5 extends AbstractRumbleGbcMbc {

    public GbcMbc5(boolean hasRam, boolean hasBattery, boolean hasRumble) {
        super(hasRam, hasBattery, hasRumble);
    }

    @Override
    public int read(int addr, byte[] contents) {
        return 0;
    }

    @Override
    public void write(int value, int addr) {
    }
}
