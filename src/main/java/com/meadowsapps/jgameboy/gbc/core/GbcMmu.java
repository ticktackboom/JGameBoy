package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Mmu;

/**
 * Created by Dylan on 1/12/17.
 */
public class GbcMmu extends AbstractGbcCoreElement implements Mmu {

    private int[] wram;

    private int[] bios = new int[]{
            0x31, 0xFE, 0xFF, 0xAF, 0x21, 0xFF, 0x9F, 0x32, 0xCB, 0x7C, 0x20, 0xFB, 0x21, 0x26, 0xFF, 0x0E,
            0x11, 0x3E, 0x80, 0x32, 0xE2, 0x0C, 0x3E, 0xF3, 0xE2, 0x32, 0x3E, 0x77, 0x77, 0x3E, 0xFC, 0xE0,
            0x47, 0x11, 0x04, 0x01, 0x21, 0x10, 0x80, 0x1A, 0xCD, 0x95, 0x00, 0xCD, 0x96, 0x00, 0x13, 0x7B,
            0xFE, 0x34, 0x20, 0xF3, 0x11, 0xD8, 0x00, 0x06, 0x08, 0x1A, 0x13, 0x22, 0x23, 0x05, 0x20, 0xF9,
            0x3E, 0x19, 0xEA, 0x10, 0x99, 0x21, 0x2F, 0x99, 0x0E, 0x0C, 0x3D, 0x28, 0x08, 0x32, 0x0D, 0x20,
            0xF9, 0x2E, 0x0F, 0x18, 0xF3, 0x67, 0x3E, 0x64, 0x57, 0xE0, 0x42, 0x3E, 0x91, 0xE0, 0x40, 0x04,
            0x1E, 0x02, 0x0E, 0x0C, 0xF0, 0x44, 0xFE, 0x90, 0x20, 0xFA, 0x0D, 0x20, 0xF7, 0x1D, 0x20, 0xF2,
            0x0E, 0x13, 0x24, 0x7C, 0x1E, 0x83, 0xFE, 0x62, 0x28, 0x06, 0x1E, 0xC1, 0xFE, 0x64, 0x20, 0x06,
            0x7B, 0xE2, 0x0C, 0x3E, 0x87, 0xF2, 0xF0, 0x42, 0x90, 0xE0, 0x42, 0x15, 0x20, 0xD2, 0x05, 0x20,
            0x4F, 0x16, 0x20, 0x18, 0xCB, 0x4F, 0x06, 0x04, 0xC5, 0xCB, 0x11, 0x17, 0xC1, 0xCB, 0x11, 0x17,
            0x05, 0x20, 0xF5, 0x22, 0x23, 0x22, 0x23, 0xC9, 0xCE, 0xED, 0x66, 0x66, 0xCC, 0x0D, 0x00, 0x0B,
            0x03, 0x73, 0x00, 0x83, 0x00, 0x0C, 0x00, 0x0D, 0x00, 0x08, 0x11, 0x1F, 0x88, 0x89, 0x00, 0x0E,
            0xDC, 0xCC, 0x6E, 0xE6, 0xDD, 0xDD, 0xD9, 0x99, 0xBB, 0xBB, 0x67, 0x63, 0x6E, 0x0E, 0xEC, 0xCC,
            0xDD, 0xDC, 0x99, 0x9F, 0xBB, 0xB9, 0x33, 0x3E, 0x3c, 0x42, 0xB9, 0xA5, 0xB9, 0xA5, 0x42, 0x4C,
            0x21, 0x04, 0x01, 0x11, 0xA8, 0x00, 0x1A, 0x13, 0xBE, 0x20, 0xFE, 0x23, 0x7D, 0xFE, 0x34, 0x20,
            0xF5, 0x06, 0x19, 0x78, 0x86, 0x23, 0x05, 0x20, 0xFB, 0x86, 0x20, 0xFE, 0x3E, 0x01, 0xE0, 0x50
    };

    GbcMmu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
        wram = new int[0x2000];
    }

    @Override
    public void reset() {
        wram = new int[0x2000];
    }

    @Override
    public int readByte(int addr) {
        int rv = -1;

        addr &= 0xFFFF;

        // Cartridge
        // 0x0000 - 0x7FFF
        if (isCartridge(addr)) {
            rv = (core().isBooting() && addr < 0x0100) ? bios[addr] : cartridge().read(addr);
        }

        // Video RAM
        // 0x8000 - 0x9FFF
        if (isVram(addr)) {
            rv = gpu().read(addr);
        }

        // External RAM
        // 0xA000 - 0xB000
        if (isExternalRam(addr)) {
            rv = cartridge().read(addr);
        }

        // Working RAM
        // 0xC000 - 0xDFFF
        if (isWorkingRam(addr)) {
            addr -= 0xC000;
            rv = wram[addr];
        }

        // Working RAM Echo
        // 0xE000 - 0xFDFF
        if (isWorkingEcho(addr)) {
            addr -= 0xE000;
            rv = wram[addr];
        }

        // OAM
        // 0xFE00 - 0xFE9F
        if (isOam(addr)) {
            rv = gpu().read(addr);
        }

        // Unused
        // 0xFEA0 - 0xFEFF
        if (isUnused(addr)) {
        }

        // Hardware I/O
        // 0xFF00 - 0xFF7F
        if (isHardwareIO(addr)) {
            // Joypad
            // 0xFF00
            if (addr == JOYPAD) {
                rv = joypad().read();
            }
        }

        // High RAM
        // 0xFF80 - 0xFFFE
        if (isHighRam(addr)) {
        }

        // Interrupt
        // 0xFFFF
        if (isInterrupt(addr)) {
        }

        rv &= 0xFF;
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
                cartridge().write(value, addr);
                break;
            case 0x8000:
            case 0x9000:
                gpu().write(value, addr);
                break;
            case 0xA000:
            case 0xB000:
                cartridge().write(value, addr);
                break;
            case 0xC000:
            case 0xD000:
                addr -= 0xC000;
                wram[addr] = value;
                break;
            case 0xE000:
            case 0xF000:
                // Working RAM Echo
                if (isWorkingEcho(addr)) {
                    addr -= 0xE000;
                    wram[addr] = value;
                    break;
                }

                // OAM
                if (isOam(addr)) {
                    gpu().write(value, addr);
                    break;
                }

                // Unused
                if (isUnused(addr)) {
                    break;
                }

                // Hardware I/O
                if (isHardwareIO(addr)) {
                    break;
                }

                // High RAM
                if (isHighRam(addr)) {
                    break;
                }

                // Interrupt
                if (isInterrupt(addr)) {
                    break;
                }
                break;
        }

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

    private boolean isCartridge(int addr) {
        return CARTRIDGE <= addr && addr < VIDEO_RAM;
    }

    private boolean isVram(int addr) {
        return VIDEO_RAM <= addr && addr < EXTERNAL_RAM;
    }

    private boolean isExternalRam(int addr) {
        return EXTERNAL_RAM <= addr && addr < WORKING_RAM;
    }

    private boolean isWorkingRam(int addr) {
        return WORKING_RAM <= addr && addr < WORKING_ECHO;
    }

    private boolean isWorkingEcho(int addr) {
        return WORKING_ECHO <= addr && addr < OAM;
    }

    private boolean isOam(int addr) {
        return OAM <= addr && addr < UNUSED;
    }

    private boolean isUnused(int addr) {
        return UNUSED <= addr && addr < HARDWARE_IO;
    }

    private boolean isHardwareIO(int addr) {
        return HARDWARE_IO <= addr && addr < HIGH_RAM;
    }

    private boolean isHighRam(int addr) {
        return HIGH_RAM <= addr && addr < INTERRUPT;
    }

    private boolean isInterrupt(int addr) {
        return addr == INTERRUPT;
    }

}
