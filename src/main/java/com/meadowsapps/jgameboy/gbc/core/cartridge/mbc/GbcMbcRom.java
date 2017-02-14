package com.meadowsapps.jgameboy.gbc.core.cartridge.mbc;

import com.meadowsapps.jgameboy.gbc.core.cartridge.GbcCartridge;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by Dylan on 2/6/17.
 */
public class GbcMbcRom extends AbstractGbcMbc {

    private int[] rom;

    private int romSize;

    private int[] ram;

    private int ramSize;

    public GbcMbcRom(GbcCartridge cartridge) {
        super(cartridge);
    }

    @Override
    public void initialize(byte[] contents) {
        romSize = cartridge().getHeader().getRomSize();
        rom = new int[romSize];
        ramSize = cartridge().getHeader().getRamSize();
        ram = new int[ramSize];

        IntBuffer intBuf = ByteBuffer.wrap(contents).asIntBuffer();
        int[] buffer = new int[intBuf.remaining()];
        intBuf.get(buffer);
        rom = buffer;
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
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
            case 0x8000:
                rv = rom[addr];
                break;
            case 0xA000:
            case 0xB000:
                rv = ram[addr - 0xA000];
                break;
        }

        rv &= 0xFF;
        return rv;
    }

    @Override
    public void write(int value, int addr) {

    }
}
