package com.meadowsapps.jgameboy.groovy.gbc.element

import com.meadowsapps.jgameboy.groovy.core.element.CoreElement
import com.meadowsapps.jgameboy.groovy.gbc.GbcCore
import com.meadowsapps.jgameboy.groovy.gbc.element.apu.GbcApu
import com.meadowsapps.jgameboy.groovy.gbc.element.cpu.GbcCpu
import com.meadowsapps.jgameboy.groovy.gbc.element.gpu.GbcGpu
import com.meadowsapps.jgameboy.groovy.gbc.element.mmu.GbcMmu
import com.meadowsapps.jgameboy.groovy.gbc.util.GbcConstants
import groovy.transform.CompileStatic

/**
 * Created by dmeadows on 3/2/2017.
 */
@CompileStatic
abstract class GbcCoreElement implements CoreElement, GbcConstants {

    private GbcCore core

    GbcCoreElement(GbcCore core) {
        this.core = core
    }

    @Override
    GbcApu apu() {
        return core.apu()
    }

    @Override
    GbcCpu cpu() {
        return core.cpu()
    }

    @Override
    GbcGpu gpu() {
        return core.gpu()
    }

    @Override
    GbcMmu mmu() {
        return core.mmu()
    }
}
