package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.AbstractMmu;

/**
 * Created by Dylan on 1/12/17.
 */
public class GbcMmu extends AbstractMmu implements GbcCoreElement {

    private int[] wram = new int[0x2000];
    private int[] zram = new int[0x7F];
    private int[] oam = new int[0xA0];

    public static final int RESTART_INTERRUPT_VECTORS = 0x0000;
    public static final int CARTRIDGE_HEADER = 0x0100;
    public static final int CARTRIDGE_ROM_BANK = 0x0150;
    public static final int CARTRIDGE_SECOND_ROM_BANK = 0x4000;

    public static final int CARTRIDGE = 0x0000;
    public static final int VIDEO_RAM = 0x8000;
    public static final int EXTERNAL_RAM = 0xA000;
    public static final int WORKING_RAM = 0xC000;
    public static final int WORKING_ECHO = 0xE000;
    public static final int OAM = 0xFE00;
    public static final int UNUSED = 0xFEA0;
    public static final int HARDWARE_IO = 0xFF00;
    public static final int HIGH_RAM = 0xFF80;
    public static final int INTERRUPT = 0xFFFF;

    GbcMmu(GbcCore core) {
        super(core);
    }

    @Override
    public int readByte(int addr) {
        int rv = -1;

        addr &= 0xFFFF;
        switch (addr & 0xF000) {
            case 0x0000:
            case 0x1000:
            case 0x2000:
            case 0x3000:
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                rv = getCore().getCartridge().read(addr);
                break;
            case 0x8000:
            case 0x9000:
                addr -= 0x8000;
//                rv = vram[addr];
                break;
            case 0xA000:
            case 0xB000:
                rv = getCore().getCartridge().read(addr);
                break;
            case 0xC000:
            case 0xD000:
                addr -= 0xC000;
                rv = wram[addr];
                break;
            case 0xE000:
                addr -= 0xE000;
                rv = wram[addr];
            case 0xF000:
                if (addr < OAM) {
                    // read ram
                } else if (addr < HARDWARE_IO) {

                }
        }

        return rv;
    }

    @Override
    public int readWord(int addr) {
        int lo = readByte(addr);
        int hi = readByte(addr + 1);
        return (hi << 8) + lo;
    }

    @Override
    public void writeByte(int value, int addr) {
        value &= 0xFF;
    }

    @Override
    public void writeWord(int value, int addr) {
        value &= 0xFFFF;
        int hi = value >> 8;
        int lo = value & 0xFF;
        writeByte(lo, addr);
        writeByte(hi, addr + 1);
    }

    @Override
    public int[] dump() {
        return wram;
    }

    @Override
    public GbcCore getCore() {
        return (GbcCore) super.getCore();
    }
}