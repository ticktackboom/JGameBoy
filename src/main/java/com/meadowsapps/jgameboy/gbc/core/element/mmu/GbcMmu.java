package com.meadowsapps.jgameboy.gbc.core.element.mmu;

import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.core.element.mmu.Mmu;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;
import com.meadowsapps.jgameboy.gbc.core.element.AbstractGbcCoreElement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static com.meadowsapps.jgameboy.gbc.core.element.GbcMemoryMap.*;

/**
 * Created by Dylan on 1/12/17.
 */
public class GbcMmu extends AbstractGbcCoreElement implements Mmu {

    private byte[] bios;

    private byte[] ram;

    private byte[] vram;

    private byte[] oam;

    private byte[] hram;

    private byte[] io;

    private byte ie;

    public GbcMmu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() throws InitializationException {
        try {
            int read;
            byte[] buffer = new byte[1024];
            InputStream input = getClass().getClassLoader().getResourceAsStream("gbc/DMG_ROM.bin");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            bios = Arrays.copyOf(output.toByteArray(), BIOS.size());

            ram = new byte[RAM.size()];
            vram = new byte[VRAM.size()];
            oam = new byte[OAM.size()];
            hram = new byte[HRAM.size()];
            io = new byte[IO.size()];
        } catch (IOException e) {
            throw new InitializationException(e);
        }
    }

    @Override
    public void reset() {
        ram = new byte[RAM.size()];
        vram = new byte[VRAM.size()];
        oam = new byte[OAM.size()];
        hram = new byte[HRAM.size()];
        io = new byte[IO.size()];
    }

    @Override
    public int read(int addr) {
        int rv = -1;
        if (BIOS.contains(addr) && isBooting()) {
            rv = bios[addr];
        } else if (ROM.contains(addr)) {
            rv = cartridge().read(addr);
        } else if (VRAM.contains(addr)) {
            addr -= VRAM.start();
            rv = vram[addr];
        } else if (ERAM.contains(addr)) {
            rv = cartridge().read(addr);
        } else if (RAM.contains(addr)) {
            addr -= RAM.start();
            rv = ram[addr];
        } else if (ECHO.contains(addr)) {
            addr -= ECHO.start();
            rv = ram[addr];
        } else if (OAM.contains(addr)) {
            addr -= OAM.start();
            rv = oam[addr];
        } else if (UNUSED.contains(addr)) {
            // do nothing
        } else if (IO.contains(addr)) {
            addr -= IO.start();
            rv = io[addr];
        } else if (HRAM.contains(addr)) {
            addr -= HRAM.start();
            rv = hram[addr];
        } else if (addr == IE) {
            rv = ie;
        }

        rv &= 0xFF;
        return rv;
    }

    @Override
    public void write(int value, int addr) {
        byte val = (byte) value;
        addr &= 0xFFFF;
        if (isBooting() && BIOS.contains(addr)) {
            //
        } else if (ROM.contains(addr)) {
            cartridge().write(value, addr);
        } else if (VRAM.contains(addr)) {
            addr -= VRAM.start();
            vram[addr] = val;
        } else if (ERAM.contains(addr)) {
            cartridge().write(value, addr);
        } else if (RAM.contains(addr)) {
            addr -= RAM.start();
            ram[addr] = val;
        } else if (ECHO.contains(addr)) {
            addr -= ECHO.start();
            ram[addr] = val;
        } else if (OAM.contains(addr)) {
            addr -= OAM.start();
            oam[addr] = val;
        } else if (UNUSED.contains(addr)) {
            // do nothing
        } else if (IO.contains(addr)) {
            addr -= IO.start();
            io[addr] = val;
        } else if (HRAM.contains(addr)) {
            addr -= HRAM.start();
            hram[addr] = val;
        } else if (addr == IE) {
            ie = val;
        }
    }

    public int readWord(int addr) {
        int lo = read(addr);
        int hi = read(addr + 1);
        return (hi << 8) + lo;
    }

    public void writeWord(int value, int addr) {
        int hi = value >> 8;
        int lo = value & 0xFF;
        write(lo, addr);
        write(hi, addr + 1);
    }

    private boolean isBooting() {
        boolean mapped = read(BOOT) == 0x00;
        return core().isBootEnabled() && mapped;
    }

}
