package com.meadowsapps.jgameboy.gbc

/**
 * Created by dmeadows on 3/6/2017.
 */
class GbcCoreElement {

    private GbcCore core

    GbcCoreElement(GbcCore core) {
        this.core = core
    }

    GbcCpu cpu() {
        return core.cpu()
    }

    GbcMmu mmu() {
        return core.mmu()
    }

}