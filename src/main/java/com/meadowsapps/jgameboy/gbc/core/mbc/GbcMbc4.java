package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc4 extends AbstractGbcMbc {

    public GbcMbc4(boolean hasRam, boolean hasBattery) {
        super(hasRam, hasBattery);
    }

    @Override
    public int read(int addr) {
        return 0;
    }

    @Override
    public void write(int value, int addr) {

    }
}
