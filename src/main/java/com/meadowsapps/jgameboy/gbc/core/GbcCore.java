package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.AbstractEmulatorCore;
import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.gbc.core.element.GbcConstants;
import com.meadowsapps.jgameboy.gbc.core.element.apu.GbcApu;
import com.meadowsapps.jgameboy.gbc.core.element.cartridge.GbcCartridge;
import com.meadowsapps.jgameboy.gbc.core.element.cpu.GbcCpu;
import com.meadowsapps.jgameboy.gbc.core.element.gpu.GbcGpu;
import com.meadowsapps.jgameboy.gbc.core.element.io.GbcButton;
import com.meadowsapps.jgameboy.gbc.core.element.io.GbcJoypad;
import com.meadowsapps.jgameboy.gbc.core.element.mmu.GbcMmu;
import javafx.scene.input.KeyCode;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class GbcCore extends AbstractEmulatorCore implements GbcConstants {

    private int[] bios;

    private int[] ram;

    private int[] vram;

    private int[] oam;

    private int[] hram;

    private int[] io;

    private int ie;

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

            int read;
            byte[] buffer = new byte[1024];
            InputStream input = getClass().getClassLoader().getResourceAsStream("gbc/DMG_ROM.bin");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            buffer = output.toByteArray();

            bios = new int[BIOS.size()];
            for (int i = 0; i < buffer.length; i++) {
                bios[i] = buffer[i] & 0xFF;
            }

            ram = new int[RAM.size()];
            vram = new int[VRAM.size()];
            oam = new int[OAM.size()];
            hram = new int[HRAM.size()];
            io = new int[IO.size()];
            boot = true;

            GbcButton.A.map(KeyCode.Z);
            GbcButton.B.map(KeyCode.X);
            GbcButton.UP.map(KeyCode.UP);
            GbcButton.DOWN.map(KeyCode.DOWN);
            GbcButton.LEFT.map(KeyCode.LEFT);
            GbcButton.RIGHT.map(KeyCode.RIGHT);
            GbcButton.START.map(KeyCode.ENTER);
            GbcButton.SELECT.map(KeyCode.BACK_SPACE);
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

        ram = new int[RAM.size()];
        vram = new int[VRAM.size()];
        oam = new int[OAM.size()];
        hram = new int[HRAM.size()];
        io = new int[IO.size()];
    }

    @Override
    public void run() {
        cpu.step();
        apu.step();
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

    public int[] bios() {
        return bios;
    }

    public int[] ram() {
        return ram;
    }

    public int[] vram() {
        return vram;
    }

    public int[] oam() {
        return oam;
    }

    public int[] hram() {
        return hram;
    }

    public int[] io() {
        return io;
    }

    public int ie() {
        return ie;
    }

    public void ie(int ie) {
        this.ie = ie;
    }

    public boolean isBootEnabled() {
        return boot;
    }
}
