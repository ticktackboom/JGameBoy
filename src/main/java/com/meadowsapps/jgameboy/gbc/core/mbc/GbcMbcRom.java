package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

/**
 * Created by Dylan on 2/6/17.
 */
public class GbcMbcRom extends AbstractGbcMbc {

    public GbcMbcRom(GbcCartridge cartridge) {
        super(cartridge);
    }

    @Override
    public int read(int addr) {
        int rv = -1;

        int[] rom = cartridge().getRom()[0];
        int[] ram = cartridge().getRam()[0];

        switch (addr & 0xF000) {
            case 0x1000:
            case 0x2000:
            case 0x3000:
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
            case 0x8000:
                rv = rom[addr];
                break;
            case 0xA000:
            case 0xB000:
                rv = ram[addr - 0xA000];
                break;
        }

        return rv;
    }

    @Override
    public void write(int value, int addr) {

    }
}
