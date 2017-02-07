package com.meadowsapps.jgameboy.core;

/**
 * Created by Dylan on 2/6/17.
 */
public abstract class AbstractCartridge implements Cartridge {

    private EmulatorCore core;

    public AbstractCartridge(EmulatorCore core) {
        this.core = core;
    }

    public EmulatorCore getCore() {
        return core;
    }
}
