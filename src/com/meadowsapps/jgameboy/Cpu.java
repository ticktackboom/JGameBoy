package com.meadowsapps.jgameboy;

/**
 * Created by Dylan on 1/6/17.
 */
public class Cpu implements Constants {

    /**
     * Registers
     */
    private int[] registers;

    /**
     * The number of instructions that have been executed since the
     * last reset
     */
    private int instructionCount;

    public Cpu() {
        registers = new int[REGISTER_COUNT];
    }

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

    }

    public int registerRead(int register) {
        return -1;
    }

    public void registerWrite(int register, int data) {

    }
}
