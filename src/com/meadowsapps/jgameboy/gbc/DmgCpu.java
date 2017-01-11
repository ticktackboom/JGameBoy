package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.Cpu;

/**
 * Created by Dylan on 1/6/17.
 */
public class DmgCpu implements Cpu, Constants {

    /**
     * 8-Bit Registers
     */
    private int a, b, c, d, e, f;

    /**
     * 16-Bit Registers
     */
    private int sp, pc, hl;

    /**
     * The number of instructions that have been executed since the
     * last reset
     */
    private int instructionCount;

    public short addressRead(int address) {
        short rv = 0x00;

        address &= 0xFFFF;
        switch (address & 0xF000) {
            case 0x0000:
            case 0x1000:
            case 0x2000:
            case 0x3000:
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                // return cartridge.addressRead(addr);
            case 0x8000:
            case 0x9000:
                // return graphicsChip.addressRead(addr);
            case 0xA000:
            case 0xB000:
                // return cartridge.addressRead(addr);
            case 0xC000:
                // return mainRam[addr - 0xC000];
            case 0xD000:
                // return mainRam[addr - 0xD000 + gbcRamBank * 0x1000)];
            case 0xE000:
                // return mainRam[addr - 0xE000];
            case 0xF000:
//                if (addr < 0xFE00) {
//                    return mainRam[addr - 0xE000];
//                } else if (addr < 0xFF00) {
//                    return (short) (oam[addr - 0xFE00] & 0x00FF);
//                } else {
//                    return ioHandler.ioRead(addr - 0xFF00);
//                }
            default:

        }
        return rv;
    }

    public void addressWrite(int address, int data) {
        switch (address & 0xF000) {
            case 0x0000:
            case 0x1000:
            case 0x2000:
            case 0x3000:
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
//                if (!running) {
//                    cartridge.debuggerAddressWrite(addr, data);
//                } else {
//                    cartridge.addressWrite(addr, data);
//                    //    System.out.println("Tried to write to ROM! PC = " + javaboy.JavaBoy.hexWord(pc) + ", Data = " + javaboy.JavaBoy.hexByte(javaboy.JavaBoy.unsign((byte) data)));
//                }
                break;
            case 0x8000:
            case 0x9000:
//                graphicsChip.addressWrite(addr - 0x8000, (byte) data);
                break;
            case 0xA000:
            case 0xB000:
//                cartridge.addressWrite(addr, data);
                break;
            case 0xC000:
//                mainRam[addr - 0xC000] = (byte) data;
                break;
            case 0xD000:
//                mainRam[addr - 0xD000 + (gbcRamBank * 0x1000)] = (byte) data;
                break;
            case 0xE000:
//                mainRam[addr - 0xE000] = (byte) data;
                break;
            case 0xF000:
//                if (addr < 0xFE00) {
//                    try {
//                        mainRam[addr - 0xE000] = (byte) data;
//                    } catch (ArrayIndexOutOfBoundsException e) {
//                        System.out.println("Address error: " + addr + " pc = " + JavaBoy.hexWord(pc));
//                    }
//                } else if (addr < 0xFF00) {
//                    oam[addr - 0xFE00] = (byte) data;
//                } else {
//                    ioHandler.ioWrite(addr - 0xFF00, (short) data);
//                }
                break;
        }
    }

    public int registerRead(int register) {
        return -1;
    }

    public void registerWrite(int register, int data) {

    }
}
