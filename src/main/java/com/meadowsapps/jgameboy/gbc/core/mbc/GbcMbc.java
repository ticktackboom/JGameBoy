package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by dmeadows on 2/3/2017.
 */
public enum GbcMbc implements MemoryBankController {
    MBC1,
    MBC1_RAM,
    MBC1_RAM_BATTERY,
    MBC2,
    MBC2_BATTERY,
    MBC3,
    MBC3_RAM,
    MBC3_RAM_BATTERY,
    MBC3_TIMER_BATTERY,
    MBC3_TIMER_RAM_BATTERY,
    MBC4,
    MBC4_RAM,
    MBC4_RAM_BATTERY,
    MBC5,
    MBC5_RAM,
    MBC5_RAM_BATTERY,
    MBC5_RUMBLE,
    MBC5_RUMBLE_RAM,
    MBC5_RUMBLE_RAM_BATTERY,
    MMM01,
    MMM01_RAM,
    MMM01_RAMB_BATTERY,
    POCKET_CAMERA,
    HUC3,
    HUC1_RAM_BATTERY;

    @Override
    public int read(int addr, byte[] contents) {
        return 0;
    }

    @Override
    public void write(int value, int addr) {

    }
}
