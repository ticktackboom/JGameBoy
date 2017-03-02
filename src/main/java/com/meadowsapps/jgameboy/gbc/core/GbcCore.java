package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.AbstractEmulatorCore;
import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.gbc.core.element.GbcConstants;
import com.meadowsapps.jgameboy.gbc.core.element.apu.GbcApu;
import com.meadowsapps.jgameboy.gbc.core.element.cartridge.GbcCartridge;
import com.meadowsapps.jgameboy.gbc.core.element.cpu.GbcCpu;
import com.meadowsapps.jgameboy.gbc.core.element.gpu.GbcGpu;
import com.meadowsapps.jgameboy.gbc.core.element.io.GbcJoypad;
import com.meadowsapps.jgameboy.gbc.core.element.mmu.GbcMmu;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class GbcCore extends AbstractEmulatorCore implements GbcConstants {

    private boolean boot;

    private final GbcCpu cpu;

    private final GbcApu apu;

    private final GbcGpu gpu;

    private final GbcMmu mmu;

    private final GbcCartridge cartridge;

    private final GbcJoypad joypad;

    public GbcCore() {
        cpu = new GbcCpu(this);
        apu = new GbcApu(this);
        gpu = new GbcGpu(this);
        mmu = new GbcMmu(this);
        cartridge = new GbcCartridge(this);
        joypad = new GbcJoypad(this);
    }

    @Override
    public void initialize() throws InitializationException {
        try {
            cpu.initialize();
            apu.initialize();
            gpu.initialize();
            mmu.initialize();
            cartridge.initialize();
            joypad.initialize();
            boot = false;
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }

    @Override
    public void reset() {
        cpu.reset();
        apu.reset();
        gpu.reset();
        mmu.reset();
        cartridge.reset();
        joypad.reset();
    }

    @Override
    public void handle(long now) {
        boolean complete = false;
        while (!complete) {
            cpu.step();

//            gpu.step();
            apu.step();

            cpu.handleInterrupts();
        }
    }

    @Override
    public GbcCpu cpu() {
        return cpu;
    }

    @Override
    public GbcApu apu() {
        return apu;
    }

    @Override
    public GbcGpu gpu() {
        return gpu;
    }

    @Override
    public GbcMmu mmu() {
        return mmu;
    }

    @Override
    public GbcCartridge cartridge() {
        return cartridge;
    }

    @Override
    public GbcJoypad joypad() {
        return joypad;
    }

    public boolean isBootEnabled() {
        return boot;
    }
}
