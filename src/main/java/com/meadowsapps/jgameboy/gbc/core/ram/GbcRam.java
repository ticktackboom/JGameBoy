package com.meadowsapps.jgameboy.gbc.core.ram;

import com.meadowsapps.jgameboy.core.ram.Ram;
import com.meadowsapps.jgameboy.gbc.core.AbstractGbcCoreElement;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;

/**
 * Created by Dylan on 2/11/17.
 */
public class GbcRam extends AbstractGbcCoreElement implements Ram {

    private int[] ram;

    public GbcRam(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
        ram = new int[0x2000];
    }

    @Override
    public void reset() {
        ram = new int[0x2000];
    }

    @Override
    public int read(int addr) {
        int rv = -1;
        switch (addr & 0xF000) {
            case 0xC000:
            case 0xD000:
                addr -= 0xC000;
                rv = ram[addr];
                break;
            case 0xE000:
            case 0xF000:
                if (addr < 0xFE00) {
                    addr -= 0xE000;
                    rv = ram[addr];
                }
                break;
        }
        return rv;
    }

    @Override
    public void write(int value, int addr) {
        switch (addr & 0xF000) {
            case 0xC000:
            case 0xD000:
                addr -= 0xC000;
                ram[addr] = value;
                break;
            case 0xE000:
            case 0xF000:
                if (addr < 0xFE00) {
                    addr -= 0xE000;
                    ram[addr] = value;
                }
                break;
        }
    }

}
