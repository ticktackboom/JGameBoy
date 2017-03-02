package com.meadowsapps.jgameboy.groovy.core.element

import com.meadowsapps.jgameboy.groovy.core.element.apu.Apu
import com.meadowsapps.jgameboy.groovy.core.element.cpu.Cpu
import com.meadowsapps.jgameboy.groovy.core.element.gpu.Gpu
import com.meadowsapps.jgameboy.groovy.core.element.mmu.Mmu
import com.meadowsapps.jgameboy.groovy.core.util.Initializable
import com.meadowsapps.jgameboy.groovy.core.util.Resettable

/**
 * Created by dmeadows on 3/2/2017.
 */
interface CoreElement extends Initializable, Resettable {

    Apu apu()

    Cpu cpu()

    Gpu gpu()

    Mmu mmu()

}