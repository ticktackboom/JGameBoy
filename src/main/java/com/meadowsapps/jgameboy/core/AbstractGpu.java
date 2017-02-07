package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 2/7/2017.
 */
public abstract class AbstractGpu implements Gpu {

    private EmulatorCore core;

    public AbstractGpu(EmulatorCore core) {
        this.core = core;
    }

    @Override
    public EmulatorCore getCore() {
        return core;
    }
}
