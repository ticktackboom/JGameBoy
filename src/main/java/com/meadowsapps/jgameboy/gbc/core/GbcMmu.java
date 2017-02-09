package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Mmu;

/**
 * Created by Dylan on 1/12/17.
 */
public class GbcMmu extends AbstractGbcCoreElement implements Mmu {

    private int[] wram;

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
            rv = cartridge().read(addr);
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
