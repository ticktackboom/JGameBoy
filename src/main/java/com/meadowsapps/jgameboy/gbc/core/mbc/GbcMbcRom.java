package com.meadowsapps.jgameboy.gbc.core.mbc;

/**
 * Created by Dylan on 2/6/17.
 */
public class GbcMbcRom extends AbstractGbcMbc {

    private byte[] ram;

    public GbcMbcRom(boolean hasRam, boolean hasBattery) {
        super(hasRam, hasBattery);
        ram = new byte[0];
    }

    @Override
    public int read(int addr) {
        byte rv = -1;
        byte[] contents = getCartridge().getContents();
        switch (addr & 0xF000) {
            case 0x1000:
            case 0x2000:
            case 0x3000:
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
            case 0x8000:
                rv = contents[addr];
                break;
            case 0xA000:
            case 0xB000:
                rv = ram[addr - 0xA000];
                break;
        }
        return Byte.toUnsignedInt(rv);
    }

    @Override
    public void write(int value, int addr) {

    }
}
