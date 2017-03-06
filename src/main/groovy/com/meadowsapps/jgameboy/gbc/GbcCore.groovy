package com.meadowsapps.jgameboy.gbc

/**
 * Created by dmeadows on 3/6/2017.
 */
class GbcCore {

    private GbcCpu cpu

    private GbcMmu mmu

    GbcCore() {
        cpu = new GbcCpu(this)
        cpu.initialize()
        mmu = new GbcMmu(this)
        mmu.initialize()
    }

    GbcCpu cpu() {
        return cpu
    }

    GbcMmu mmu() {
        return mmu
    }

}
