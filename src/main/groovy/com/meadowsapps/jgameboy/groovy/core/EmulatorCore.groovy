package com.meadowsapps.jgameboy.groovy.core

import com.meadowsapps.jgameboy.groovy.core.element.apu.Apu
import com.meadowsapps.jgameboy.groovy.core.element.cpu.Cpu
import com.meadowsapps.jgameboy.groovy.core.element.gpu.Gpu
import com.meadowsapps.jgameboy.groovy.core.element.mmu.Mmu
import com.meadowsapps.jgameboy.groovy.core.util.Initializable
import groovy.transform.PackageScope

/**
 * Created by dmeadows on 3/2/2017.
 */
@PackageScope
interface EmulatorCore extends Initializable {

    Apu apu()

    Cpu cpu()

    Gpu gpu()

    Mmu mmu()

}
