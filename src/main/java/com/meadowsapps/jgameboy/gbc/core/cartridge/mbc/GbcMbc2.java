package com.meadowsapps.jgameboy.gbc.core.cartridge.mbc;

import com.meadowsapps.jgameboy.gbc.core.cartridge.GbcCartridge;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc2 extends AbstractGbcMbc {

    private int romSize;

    private int[] romBank0;

    private int[][] romBanks;

    private int selectedRomBank;

    private boolean ramEnabled;

    private int[] ram = new int[0x0200];

    public GbcMbc2(GbcCartridge cartridge) {
        super(cartridge);
    }

    @Override
    public void initialize(byte[] contents) {
        romSize = cartridge().getHeader().getRomSize();
        int romBankCount = cartridge().getHeader().getRomBankCount();
        romBank0 = new int[0x4000];
        romBanks = new int[romBankCount][0x4000];

        int bank = 0;
        for (int i = 0; i < contents.length; i += 0x4000) {
            // store buffer as int[] to proper rom bank
            byte[] current = Arrays.copyOfRange(contents, i, i + 0x4000);
            IntBuffer intBuf = ByteBuffer.wrap(current).asIntBuffer();
            int[] buffer = new int[intBuf.remaining()];
            intBuf.get(buffer);
            if (bank == 0) {
                romBank0 = buffer;
            } else {
                romBanks[bank] = buffer;
            }
            bank++;
        }
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
                rv = romBank0[addr];
                break;
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                addr -= 0x4000;
                rv = romBanks[selectedRomBank][addr];
                break;
            case 0xA000:
                addr -= 0xA000;
                rv = ram[addr] & 0x0F;
                break;
        }

        rv &= 0xFF;
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
