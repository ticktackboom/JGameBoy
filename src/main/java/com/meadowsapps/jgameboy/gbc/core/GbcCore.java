package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.EmulatorCore;
import javafx.scene.input.KeyCode;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class GbcCore implements EmulatorCore {

    private final GbcCpu cpu;

    private final GbcGpu gpu;

    private final GbcMmu mmu;

    private final GbcDisplay display;

    private final GbcCartridge cartridge;

    private final GbcJoypad joypad;

    public GbcCore() {
        cpu = new GbcCpu(this);
        gpu = new GbcGpu(this);
        mmu = new GbcMmu(this);
        display = new GbcDisplay(this);
        cartridge = new GbcCartridge(this);
        joypad = new GbcJoypad(this);
    }

    @Override
    public void initialize() {
        cpu.initialize();
        gpu.initialize();
        mmu.initialize();
        display.initialize();
        cartridge.initialize();
        joypad.initialize();

        GbcButton.A.map(KeyCode.Z);
        GbcButton.B.map(KeyCode.X);
        GbcButton.UP.map(KeyCode.UP);
        GbcButton.DOWN.map(KeyCode.DOWN);
        GbcButton.LEFT.map(KeyCode.LEFT);
        GbcButton.RIGHT.map(KeyCode.RIGHT);
        GbcButton.START.map(KeyCode.ENTER);
        GbcButton.SELECT.map(KeyCode.BACK_SPACE);
    }

    @Override
    public void reset() {
        cpu.reset();
        gpu.reset();
        mmu.reset();
        display.reset();
        cartridge.reset();
        joypad.reset();
    }

    @Override
    public GbcCpu cpu() {
        return cpu;
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
    public GbcDisplay display() {
        return display;
    }

    @Override
    public GbcCartridge cartridge() {
        return cartridge;
    }

    @Override
    public GbcJoypad joypad() {
        return joypad;
    }
}
