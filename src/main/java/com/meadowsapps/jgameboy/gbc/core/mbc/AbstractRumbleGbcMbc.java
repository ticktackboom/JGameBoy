package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by dmeadows on 2/3/2017.
 */
public abstract class AbstractRumbleGbcMbc extends AbstractGbcMbc {

    private boolean hasRumble;

    public AbstractRumbleGbcMbc(boolean hasRam, boolean hasBattery, boolean hasRumble) {
        super(hasRam, hasBattery);
        this.hasRumble = hasRumble;
    }

    public boolean hasRumble() {
        return hasRumble;
    }
}
