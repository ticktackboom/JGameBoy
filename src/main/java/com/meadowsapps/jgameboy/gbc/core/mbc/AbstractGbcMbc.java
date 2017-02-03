package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by dmeadows on 2/3/2017.
 */
public abstract class AbstractGbcMbc implements MemoryBankController {

    private int romBank;

    private int ramBank;

    private boolean hasRam;

    private boolean hasBattery;

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

    public int getRomBank() {
        return romBank;
    }

    public void setRomBank(int romBank) {
        this.romBank = romBank;
    }

    public int getRamBank() {
        return ramBank;
    }

    public void setRamBank(int ramBank) {
        this.ramBank = ramBank;
    }
}
