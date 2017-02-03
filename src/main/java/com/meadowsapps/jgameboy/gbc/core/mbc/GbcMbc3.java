package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc3 extends AbstractTimerGbcMbc {

    public GbcMbc3(boolean hasRam, boolean hasBattery, boolean hasTimer) {
        super(hasRam, hasBattery, hasTimer);
    }

    @Override
    public int read(int addr, byte[] contents) {
        return 0;
    }

    @Override
    public void write(int value, int addr) {

    }
}
