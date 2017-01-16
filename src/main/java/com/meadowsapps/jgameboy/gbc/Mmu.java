package com.meadowsapps.jgameboy.gbc;

/**
 * Created by Dylan on 1/12/17.
 */
public class Mmu {

    private GbcRam ram = new GbcRam();
    private GbcVram vram = new GbcVram();

    private static Mmu instance = new Mmu();

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

    public static int read(int address) {
        int value = -1;

        int addr = address & 0xFFFF;
        switch (addr & 0xF000) {
            case 0x1000:
            case 0x2000:
            case 0x3000:
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                // read cartridge
                break;
            case 0x8000:
            case 0x9000:
                addr -= 0x8000;
                value = instance.vram.read(addr);
                break;
            case 0xA000:
            case 0xB000:
                // read cartridge
                break;
            case 0xC000:
                addr -= 0xC000;
                value = instance.ram.read(addr);
                break;
            case 0xD000:
                // read gbc bank
                break;
            case 0xE000:
                addr -= 0xE000;
                value = instance.ram.read(addr);
            case 0xF000:
                if (addr < OAM) {
                    // read ram
                } else if (addr < HARDWARE_IO) {

                }


        }
        return value;
    }

    public static void write(int value, int address) {

    }


}
