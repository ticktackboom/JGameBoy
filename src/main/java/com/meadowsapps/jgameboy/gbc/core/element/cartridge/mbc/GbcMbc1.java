package com.meadowsapps.jgameboy.gbc.core.element.cartridge.mbc;

import com.meadowsapps.jgameboy.gbc.core.element.cartridge.GbcCartridge;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc1 extends AbstractGbcMbc {

    private int romSize;

    private int[] romBank0;

    private int[][] romBanks;

    private int selectedRomBank;

    private boolean romBanking;

    private int ramSize;

    private boolean hasRam;

    private int[][] ramBanks;

    private int selectedRamBank;

    private boolean ramEnabled;

    private boolean hasBattery;

    public GbcMbc1(GbcCartridge cartridge) {
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
            IntBuffer intBuf = ByteBuffer.wrap(current).order(ByteOrder.nativeOrder()).asIntBuffer();
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
                int romBank = this.selectedRomBank;
                if (romBank == 0x00) {
                    romBank = 0x01;
                }
                rv = romBanks[romBank][addr];
                break;
            case 0xA000:
            case 0xB000:
                addr -= 0xA000;
                rv = ramBanks[selectedRamBank][addr];
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
                if (hasRam) {
                    ramEnabled = (value & 0x0F) == 0x0A;
                }
                break;
            case 0x2000:
            case 0x3000:
                int lower5 = value & 0x1F;
                selectedRomBank &= 0xE0;
                selectedRomBank |= lower5;
                break;
            case 0x4000:
            case 0x5000:
                if (romBanking) {
                    selectedRomBank &= 0x1F;
                    value &= 0xE0;
                    selectedRomBank |= value;
                } else {
                    selectedRamBank = value & 0x03;
                }
                break;
            case 0x6000:
            case 0x7000:
                romBanking = (value & 0x01) == 0;
                if (romBanking) {
                    selectedRamBank = 0;
                }
                break;
            case 0xA000:
            case 0xB000:
                addr -= 0xA000;
                if (ramEnabled) {
                    ramBanks[selectedRamBank][addr] = value;
                }
                break;
        }
    }
}
