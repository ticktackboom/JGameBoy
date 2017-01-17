package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/17/2017.
 */
public abstract class AbstractMmu implements Mmu {

    private EmulatorCore core;

    protected AbstractMmu(EmulatorCore core) {
        this.core = core;
    }

    protected EmulatorCore getCore() {
        return core;
    }
}
