package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.JGameBoyView;
import com.meadowsapps.jgameboy.core.AbstractEmulatorCore;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class GbcCore extends AbstractEmulatorCore {

    private boolean booting;

    private final GbcCpu cpu;

    private final GbcGpu gpu;

    private final GbcMmu mmu;

    private final GbcDisplay display;

    private final GbcCartridge cartridge;

    private final GbcJoypad joypad;

    public static final int FRAME_CYCLES = 0x11250;

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
        booting = true;

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
    public void run() {
        int cpuClockAcc = 0;
        while (isRunning()) {
            while (cpuClockAcc < FRAME_CYCLES) {
                cpu.step();
                gpu.step();
                GraphicsContext context = JGameBoyView.getView().getContext();
                gpu.draw(context);
                cpuClockAcc += cpu.getClock().m();
            }
            cpuClockAcc = 0;
        }
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

    public boolean isBooting() {
        return booting;
    }
}
