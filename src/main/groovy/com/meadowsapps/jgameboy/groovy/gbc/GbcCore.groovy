package com.meadowsapps.jgameboy.groovy.gbc

import com.meadowsapps.jgameboy.groovy.core.EmulatorCore
import com.meadowsapps.jgameboy.groovy.core.util.InitializationException
import com.meadowsapps.jgameboy.groovy.gbc.element.apu.GbcApu
import com.meadowsapps.jgameboy.groovy.gbc.element.cpu.GbcCpu
import com.meadowsapps.jgameboy.groovy.gbc.element.gpu.GbcGpu
import com.meadowsapps.jgameboy.groovy.gbc.element.mmu.GbcMmu
import groovy.transform.CompileStatic

/**
 * Created by dmeadows on 3/2/2017.
 */
@CompileStatic
class GbcCore implements EmulatorCore {

    private GbcApu apu

    private GbcCpu cpu

    private GbcGpu gpu

    private GbcMmu mmu

    @Override
    void initialize() throws InitializationException {
        apu = new GbcApu(this)
        cpu = new GbcCpu(this)
        gpu = new GbcGpu(this)
        mmu = new GbcMmu(this)
    }

    @Override
    GbcApu apu() {
        return apu
    }

    @Override
    GbcCpu cpu() {
        return cpu
    }

    @Override
    GbcGpu gpu() {
        return gpu
    }

    @Override
    GbcMmu mmu() {
        return mmu
    }

}
