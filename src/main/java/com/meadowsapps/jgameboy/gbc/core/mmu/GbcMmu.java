package com.meadowsapps.jgameboy.gbc.core.mmu;

import com.meadowsapps.jgameboy.core.mmu.Mmu;
import com.meadowsapps.jgameboy.gbc.core.AbstractGbcCoreElement;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dylan on 1/12/17.
 */
public class GbcMmu extends AbstractGbcCoreElement implements Mmu {

    private int boot;

    private int[] hram;

    private byte[] bios;

    private int[] registers;

    private int interrupt;

    public GbcMmu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
        hram = new int[0x80];
        registers = new int[0x80];
        try {
            int read = 0;
            byte[] buffer = new byte[1024];
            InputStream input = getClass().getClassLoader().getResourceAsStream("gbc/DMG_ROM.bin");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            bios = output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {
        hram = new int[0x80];
        registers = new int[0x80];
    }

    @Override
    public int readByte(int addr) {
        int rv = -1;
        addr &= 0xFFFF;
        switch (addr & 0xF000) {
            case 0x0000:
                if (isBiosMapped() && addr < 0x100) {
                    rv = bios[addr];
                    break;
                }
            case 0x1000:
            case 0x2000:
            case 0x3000:
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                rv = cartridge().read(addr);
                break;
            case 0x8000:
            case 0x9000:
                rv = gpu().read(addr);
                break;
            case 0xA000:
            case 0xB000:
                rv = cartridge().read(addr);
                break;
            case 0xC000:
            case 0xD000:
                rv = ram().read(addr);
                break;
            case 0xE000:
            case 0xF000:
                if (addr < OAM) {
                    rv = ram().read(addr);
                    break;
                }

                if (HARDWARE_IO <= addr && addr < HIGH_RAM) {
                    if (addr == HARDWARE_IO) {
                        rv = joypad().read();
                        break;
                    } else {
                        addr -= HARDWARE_IO;
                        rv = registers[addr];
                        break;
                    }
                }

                if (HIGH_RAM <= addr && addr < INTERRUPT) {
                    addr -= HIGH_RAM;
                    rv = hram[addr];
                    break;
                }

                if (addr == INTERRUPT) {
                    rv = interrupt;
                    break;
                }
                break;
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
                ram().write(value, addr);
                break;
            case 0xE000:
            case 0xF000:
                if (addr < OAM) {
                    ram().write(value, addr);
                    break;
                }

                if (HARDWARE_IO <= addr && addr < HIGH_RAM) {
                    if (addr == HARDWARE_IO) {
//                        rv = joypad().read();
                        break;
                    } else {
                        addr -= HARDWARE_IO;
                        registers[addr] = value;
                        break;
                    }
                }

                if (HIGH_RAM <= addr && addr < INTERRUPT) {
                    addr -= HIGH_RAM;
                    hram[addr] = value;
                    break;
                }

                if (addr == INTERRUPT) {
                    interrupt = value;
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

    private boolean isBiosMapped() {
        int mapped = readByte(BOOT);
        return core().isBooting() && mapped == 0x00;
    }

}
