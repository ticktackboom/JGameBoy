package com.meadowsapps.jgameboy.gbc.core.mbc;

import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc3 extends AbstractGbcMbc {

    private int romSize;

    private int[] romBank0;

    private int[][] romBanks;

    private int selectedRomBank;

    private int ramBank;

    private int[][] ramBanks;

    private int selectedRamBank;


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
                int romBank = this.selectedRomBank;
                if (romBank == 0x00) {
                    romBank = 0x01;
                }
                rv = romBanks[romBank][addr];
                break;
            case 0xA000:
            case 0xB000:
                if (0x00 <= ram_rtc && ram_rtc <= 0x03) {
                    addr -= 0xA000;
                    rv = ramBanks[ram_rtc][addr];
                } else if (0x08 <= ram_rtc && ram_rtc <= 0x0C) {
                    rv = rtcRegisters[ram_rtc - 0x08];
                }
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
                        ramBanks[ram_rtc][addr] = value;
                    } else if (0x08 <= ram_rtc && ram_rtc <= 0x0C) {
                        rtcRegisters[ram_rtc - 0x08] = value;
                    }
                }
                break;
        }
    }
}
