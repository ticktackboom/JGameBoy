package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc1 extends AbstractGbcMbc {

    private int romBankNumber;

    private int ramBankNumber;

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
                int romBank = romBankNumber;
                if (romBank == 0x00) {
                    romBank = 0x01;
                }
                rv = rom[romBank][addr - 0x4000];
                break;
            case 0xA000:
            case 0xB000:
                rv = ram[ramBankNumber][addr - 0xA000];
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
                ramEnabled = (value & 0xF) == 0xA;
                break;
            case 0x2000:
            case 0x3000:
                int lower5 = value & 0x1F;
                romBankNumber &= 0xE0;
                romBankNumber |= lower5;
                break;
            case 0x4000:
            case 0x5000:
                if (romBanking) {
                    romBankNumber &= 0x1F;
                    value &= 0xE0;
                    romBankNumber |= value;
                } else {
                    ramBankNumber = value & 0x03;
                }
                break;
            case 0x6000:
            case 0x7000:
                romBanking = (value & 0x01) == 0;
                if (romBanking) {
                    ramBankNumber = 0;
                }
                break;
        }
    }
}
