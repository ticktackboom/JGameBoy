package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc3 extends AbstractGbcMbc {

    private int romBank;

    private int ram_rtc;

    private boolean ram_rtc_enabled;

    private int[] rtcRegisters = new int[0x0D - 0x08];

    public static final int SECONDS = 0x08;

    public static final int MINUTES = 0x09;

    public static final int HOURS = 0x0A;

    public static final int LOWER_DAY = 0x0B;

    public static final int UPPER_DAY = 0x0C;

    public GbcMbc3(GbcCartridge cartridge) {
        super(cartridge);
    }

    @Override
    public int read(int addr) {
        int rv = -1;

        int[][] rom = cartridge().getRom();
        int[][] ram = cartridge().getRam();

        addr &= 0xFFFF;
        switch (addr & 0xF000) {
            case 0x0000:
            case 0x1000:
            case 0x2000:
            case 0x3000:
                rv = rom[0][addr];
                break;
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                addr -= 0x4000;
                int romBank = this.romBank;
                if (romBank == 0x00) {
                    romBank = 0x01;
                }
                rv = rom[romBank][addr];
                break;
            case 0xA000:
            case 0xB000:
                if (0x00 <= ram_rtc && ram_rtc <= 0x03) {
                    addr -= 0xA000;
                    rv = ram[ram_rtc][addr];
                } else if (0x08 <= ram_rtc && ram_rtc <= 0x0C) {
                    rv = rtcRegisters[ram_rtc - 0x08];
                }
                break;
        }
        return rv;
    }

    @Override
    public void write(int value, int addr) {

        int[][] ram = cartridge().getRam();

        addr &= 0xFFFF;
        switch (addr & 0xF000) {
            case 0x0000:
            case 0x1000:
                ram_rtc_enabled = value == 0x0A;
                break;
            case 0x2000:
            case 0x3000:
                break;
            case 0x4000:
            case 0x5000:
                break;
            case 0x6000:
            case 0x7000:
                break;
            case 0xA000:
            case 0xB000:
                if (ram_rtc_enabled) {
                    if (0x00 <= ram_rtc && ram_rtc <= 0x03) {
                        addr -= 0xA000;
                        ram[ram_rtc][addr] = value;
                    } else if (0x08 <= ram_rtc && ram_rtc <= 0x0C) {
                        rtcRegisters[ram_rtc - 0x08] = value;
                    }
                }
                break;
        }
    }
}
