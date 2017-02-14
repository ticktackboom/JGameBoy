package com.meadowsapps.jgameboy.gbc.core.cartridge.mbc;

import com.meadowsapps.jgameboy.gbc.core.cartridge.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbcFactory {

    public static final int ROM = 0x00;
    public static final int ROM_RAM = 0x08;
    public static final int ROM_RAM_BATTERY = 0x09;
    public static final int MBC1 = 0x01;
    public static final int MBC1_RAM = 0x02;
    public static final int MBC1_RAM_BATTERY = 0x03;
    public static final int MBC2 = 0x05;
    public static final int MBC2_BATTERY = 0x06;
    public static final int MMM01 = 0x0B;
    public static final int MMM01_RAM = 0x0C;
    public static final int MMM01_RAM_BATTERY = 0x0D;
    public static final int MBC3 = 0x11;
    public static final int MBC3_RAM = 0x12;
    public static final int MBC3_RAM_BATTERY = 0x13;
    public static final int MBC3_TIMER_BATTERY = 0x0F;
    public static final int MBC3_TIMER_RAM_BATTERY = 0x10;
    public static final int MBC4 = 0x15;
    public static final int MBC4_RAM = 0x16;
    public static final int MBC4_RAM_BATTERY = 0x17;
    public static final int MBC5 = 0x19;
    public static final int MBC5_RAM = 0x1A;
    public static final int MBC5_RUMBLE = 0x1C;
    public static final int MBC5_RAM_BATTERY = 0x1B;
    public static final int MBC5_RUMBLE_RAM = 0x1D;
    public static final int MBC5_RUMBLE_RAM_BATTERY = 0x1E;
    public static final int POCKET_CAMERA = 0xFC;
    public static final int HUC3 = 0xFE;
    public static final int HUC1_RAM_BATTERY = 0xFF;

    private static GbcMbcFactory factory = new GbcMbcFactory();

    private GbcMbcFactory() {
    }

    public MemoryBankController getMbc(int type, GbcCartridge cartridge) {
        boolean hasRam = hasRam(type);
        boolean hasBattery = hasBattery(type);
        boolean hasTimer = hasTimer(type);
        boolean hasRumble = hasRumble(type);

        MemoryBankController rv = null;
        switch (type) {
            case ROM:
            case ROM_RAM:
            case ROM_RAM_BATTERY:
                rv = new GbcMbcRom(cartridge);
                break;
            case MBC1:
            case MBC1_RAM:
            case MBC1_RAM_BATTERY:
                rv = new GbcMbc1(cartridge);
                break;
            case MBC2:
            case MBC2_BATTERY:
                rv = new GbcMbc2(cartridge);
                break;
            case MMM01:
            case MMM01_RAM:
            case MMM01_RAM_BATTERY:
                rv = new GbcMbcMmm01(cartridge);
                break;
            case MBC3:
            case MBC3_RAM:
            case MBC3_RAM_BATTERY:
            case MBC3_TIMER_BATTERY:
            case MBC3_TIMER_RAM_BATTERY:
                rv = new GbcMbc3(cartridge);
                break;
            case MBC4:
            case MBC4_RAM:
            case MBC4_RAM_BATTERY:
                rv = new GbcMbc4(cartridge);
                break;
            case MBC5:
            case MBC5_RAM:
            case MBC5_RAM_BATTERY:
            case MBC5_RUMBLE:
            case MBC5_RUMBLE_RAM:
            case MBC5_RUMBLE_RAM_BATTERY:
                rv = new GbcMbc5(cartridge);
                break;
            case POCKET_CAMERA:
                break;
            case HUC3:
                break;
            case HUC1_RAM_BATTERY:
                break;
        }
        return rv;
    }

    private boolean hasRam(int type) {
        return type == ROM_RAM
                || type == ROM_RAM_BATTERY
                || type == MBC1_RAM
                || type == MBC1_RAM_BATTERY
                || type == MBC3_RAM
                || type == MBC3_RAM_BATTERY
                || type == MBC3_TIMER_RAM_BATTERY
                || type == MBC4_RAM
                || type == MBC4_RAM_BATTERY
                || type == MBC5_RAM
                || type == MBC5_RAM_BATTERY
                || type == MBC5_RUMBLE_RAM
                || type == MBC5_RUMBLE_RAM_BATTERY
                || type == MMM01_RAM
                || type == MMM01_RAM_BATTERY;
    }

    private boolean hasBattery(int type) {
        return type == ROM_RAM_BATTERY
                || type == MBC1_RAM_BATTERY
                || type == MBC2_BATTERY
                || type == MBC3_RAM_BATTERY
                || type == MBC3_TIMER_BATTERY
                || type == MBC3_TIMER_RAM_BATTERY
                || type == MBC4_RAM_BATTERY
                || type == MBC5_RAM_BATTERY
                || type == MBC5_RUMBLE_RAM_BATTERY
                || type == MMM01_RAM_BATTERY;
    }

    private boolean hasTimer(int type) {
        return type == MBC3_TIMER_BATTERY
                || type == MBC3_TIMER_RAM_BATTERY;
    }

    private boolean hasRumble(int type) {
        return type == MBC5_RUMBLE
                || type == MBC5_RUMBLE_RAM
                || type == MBC5_RUMBLE_RAM_BATTERY;
    }

    public static GbcMbcFactory getFactory() {
        return factory;
    }
}
