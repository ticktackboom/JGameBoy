package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by dmeadows on 2/3/2017.
 */
public abstract class AbstractTimerGbcMbc extends AbstractGbcMbc {

    private boolean hasTimer;

    public AbstractTimerGbcMbc(boolean hasRam, boolean hasBattery, boolean hasTimer) {
        super(hasRam, hasBattery);
        this.hasTimer = hasTimer;
    }

    public boolean hasTimer() {
        return hasTimer;
    }
}
