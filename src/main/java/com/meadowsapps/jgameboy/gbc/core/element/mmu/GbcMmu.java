package com.meadowsapps.jgameboy.gbc.core.element.mmu;

import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.core.element.mmu.Mmu;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;
import com.meadowsapps.jgameboy.gbc.core.element.AbstractGbcCoreElement;

/**
 * Created by Dylan on 1/12/17.
 */
public class GbcMmu extends AbstractGbcCoreElement implements Mmu {

    public GbcMmu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() throws InitializationException {
    }

    @Override
    public void reset() {
    }

    @Override
    public int readByte(int addr) {
        int rv = -1;
        if (BIOS.contains(addr) && isBooting()) {
            rv = core().bios()[addr];
        } else if (ROM.contains(addr)) {
            rv = cartridge().read(addr);
        } else if (VRAM.contains(addr)) {
            addr -= VRAM.start();
            rv = core().vram()[addr];
        } else if (ERAM.contains(addr)) {
            rv = cartridge().read(addr);
        } else if (RAM.contains(addr)) {
            addr -= RAM.start();
            rv = core().ram()[addr];
        } else if (ECHO.contains(addr)) {
            addr -= ECHO.start();
            rv = core().ram()[addr];
        } else if (OAM.contains(addr)) {
            addr -= OAM.start();
            rv = core().oam()[addr];
        } else if (UNUSED.contains(addr)) {
            // do nothing
        } else if (IO.contains(addr)) {
            addr -= IO.start();
            rv = core().io()[addr];
        } else if (HRAM.contains(addr)) {
            addr -= HRAM.start();
            rv = core().hram()[addr];
        } else if (addr == IE) {
            rv = core().ie();
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
        if (isBooting() && BIOS.contains(addr)) {
            //
        } else if (ROM.contains(addr)) {
            cartridge().write(value, addr);
        } else if (VRAM.contains(addr)) {
            addr -= VRAM.start();
            core().vram()[addr] = value;
        } else if (ERAM.contains(addr)) {
            cartridge().write(value, addr);
        } else if (RAM.contains(addr)) {
            addr -= RAM.start();
            core().ram()[addr] = value;
        } else if (ECHO.contains(addr)) {
            addr -= ECHO.start();
            core().ram()[addr] = value;
        } else if (OAM.contains(addr)) {
            addr -= OAM.start();
            core().oam()[addr] = value;
        } else if (UNUSED.contains(addr)) {
            // do nothing
        } else if (IO.contains(addr)) {
            addr -= IO.start();
            core().io()[addr] = value;
        } else if (HRAM.contains(addr)) {
            addr -= HRAM.start();
            core().hram()[addr] = value;
        } else if (addr == IE) {
            core().ie(value);
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

    private boolean isBooting() {
        boolean mapped = readByte(BOOT) == 0x00;
        return core().isBootEnabled() && mapped;
    }

}
