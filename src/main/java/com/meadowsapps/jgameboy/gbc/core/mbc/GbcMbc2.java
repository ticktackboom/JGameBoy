package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc2 extends AbstractGbcMbc {

    private int romBank;

    private boolean ramEnabled;

    private int[] ram = new int[0x0200];

    public GbcMbc2(GbcCartridge cartridge) {
        super(cartridge);
    }

    @Override
    public int read(int addr) {
        int rv = -1;

        int[][] rom = cartridge().getRom();

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
                rv = rom[romBank][addr];
                break;
            case 0xA000:
                addr -= 0xA000;
                rv = ram[addr] & 0x0F;
                break;
        }
        return rv;
    }

    @Override
    public void write(int value, int addr) {
        addr &= 0xFFFF;
        switch (addr & 0xF000) {
            case 0x0000:
            case 0x1000:

                break;
            case 0x2000:
            case 0x3000:

                break;
            case 0xA000:
                if (ramEnabled) {
                    addr -= 0xA000;
                    ram[addr] = value & 0x0F;
                }
                break;
        }
    }
}
