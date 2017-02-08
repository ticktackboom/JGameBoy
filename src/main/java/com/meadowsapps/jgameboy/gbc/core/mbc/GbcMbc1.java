package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc1 extends AbstractGbcMbc {

    private int romBank;

    private int ramBank;

    private boolean ramEnabled;

    private boolean romBanking;

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
                addr -= 0x4000;
                int romBank = this.romBank;
                if (romBank == 0x00) {
                    romBank = 0x01;
                }
                rv = rom[romBank][addr];
                break;
            case 0xA000:
            case 0xB000:
                addr -= 0xA000;
                rv = ram[ramBank][addr];
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
                ramEnabled = (value & 0x0F) == 0x0A;
                break;
            case 0x2000:
            case 0x3000:
                int lower5 = value & 0x1F;
                romBank &= 0xE0;
                romBank |= lower5;
                break;
            case 0x4000:
            case 0x5000:
                if (romBanking) {
                    romBank &= 0x1F;
                    value &= 0xE0;
                    romBank |= value;
                } else {
                    ramBank = value & 0x03;
                }
                break;
            case 0x6000:
            case 0x7000:
                romBanking = (value & 0x01) == 0;
                if (romBanking) {
                    ramBank = 0;
                }
                break;
            case 0xA000:
            case 0xB000:
                addr -= 0xA000;
                if (ramEnabled) {
                    ram[ramBank][addr] = value;
                }
                break;
        }
    }
}
