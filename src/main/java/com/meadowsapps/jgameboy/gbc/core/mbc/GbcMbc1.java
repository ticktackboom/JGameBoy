package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc1 extends AbstractGbcMbc {



    private byte[][] romBanks = new byte[0x7F - 0x01][0x7FFF - 0x4000];

    private byte[][] ramBanks = new byte[0x03 - 0x00][0xBFFF - 0xA000];

    public GbcMbc1(boolean hasRam, boolean hasBattery) {
        super(hasRam, hasBattery);
    }

    @Override
    public int read(int addr) {
        int rv = -1;

        addr &= 0xFFFF;
        switch (addr & 0xF000) {
            case 0x0000:
            case 0x1000:
            case 0x2000:
            case 0x3000:
                break;
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                int romBank = getNumRomBank();
                rv = romBanks[romBank][addr - 0x4000];
                break;
            case 0x8000:
            case 0x9000:
                break;
            case 0xA000:
            case 0xB000:
                if (hasRam()) {
                    int ramBank = getNumRamBank();
                    rv = ramBanks[ramBank][addr - 0xA000];
                }
                break;
        }
        return rv;
    }

    @Override
    public void write(int value, int addr) {

    }
}
