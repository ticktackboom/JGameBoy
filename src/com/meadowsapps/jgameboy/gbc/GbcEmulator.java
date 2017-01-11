package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.Cpu;
import com.meadowsapps.jgameboy.Emulator;
import com.meadowsapps.jgameboy.Ram;

/**
 * Created by dmeadows on 1/11/2017.
 */
public class GbcEmulator implements Emulator {

    private GbcRam ram;

    private DmgCpu cpu;

    @Override
    public Cpu getCpu() {
        return cpu;
    }

    @Override
    public Ram getRam() {
        return ram;
    }
}
