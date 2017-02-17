package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.JGameBoy;
import com.meadowsapps.jgameboy.JGameBoyFrame;
import com.meadowsapps.jgameboy.core.AbstractEmulatorCore;
import com.meadowsapps.jgameboy.gbc.core.apu.GbcApu;
import com.meadowsapps.jgameboy.gbc.core.cartridge.GbcCartridge;
import com.meadowsapps.jgameboy.gbc.core.cpu.GbcCpu;
import com.meadowsapps.jgameboy.gbc.core.gpu.GbcGpu;
import com.meadowsapps.jgameboy.gbc.core.io.GbcButton;
import com.meadowsapps.jgameboy.gbc.core.io.GbcJoypad;
import com.meadowsapps.jgameboy.gbc.core.mmu.GbcMmu;
import com.meadowsapps.jgameboy.gbc.core.ram.GbcRam;
import javafx.scene.input.KeyCode;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class GbcCore extends AbstractEmulatorCore implements GbcConstants {

    private boolean booting;

    private final GbcCpu cpu;

    private final GbcApu apu;

    private final GbcGpu gpu;

    private final GbcRam ram;

    private final GbcMmu mmu;

    private final GbcCartridge cartridge;

    private final GbcJoypad joypad;

    public static final int FRAME_CYCLES = 0x11250;

    public GbcCore() {
        cpu = new GbcCpu(this);
        apu = new GbcApu(this);
        gpu = new GbcGpu(this);
        ram = new GbcRam(this);
        mmu = new GbcMmu(this);
        cartridge = new GbcCartridge(this);
        joypad = new GbcJoypad(this);
    }

    @Override
    public void initialize() {
        cpu.initialize();
        apu.initialize();
        gpu.initialize();
        ram.initialize();
        mmu.initialize();
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
        apu.reset();
        gpu.reset();
        ram.reset();
        mmu.reset();
        cartridge.reset();
        joypad.reset();
    }

    @Override
    public void run() {
        if (isBooting()) {
            mmu().writeByte(0x00, BOOT);
        }

        int cpuClockAcc = 0;
        while (isRunning()) {
            while (cpuClockAcc < FRAME_CYCLES) {
                cpu.step();
                int cycles = cpu.getClock().m();
                apu.updateSound(cycles);
                JGameBoyFrame frame = JGameBoy.getInstance().getFrame();
                frame.repaint();
                cpuClockAcc += cycles;
            }
            cpuClockAcc = 0;
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
    public GbcRam ram() {
        return ram;
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

    public boolean isBooting() {
        return booting;
    }

}
