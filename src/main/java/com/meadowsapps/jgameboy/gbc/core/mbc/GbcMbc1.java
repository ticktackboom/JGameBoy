package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc1 extends AbstractGbcMbc {

    public GbcMbc1(GbcCartridge cartridge) {
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
                int romBank = 1;
                rv = rom[romBank][addr - 0x4000];
                break;
            case 0xA000:
            case 0xB000:
                int ramBank = 1;
                rv = ram[ramBank][addr - 0xA000];
                break;
        }
        return rv;
    }

    @Override
    public void write(int value, int addr) {

    }
}
