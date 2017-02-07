package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/17/2017.
 */
public abstract class AbstractCpu implements Cpu {

    private EmulatorCore core;

    protected AbstractCpu(EmulatorCore core) {
        this.core = core;
    }

    @Override
    public EmulatorCore getCore() {
        return core;
    }
}
