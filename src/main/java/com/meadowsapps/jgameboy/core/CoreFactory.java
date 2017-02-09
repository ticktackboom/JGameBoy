package com.meadowsapps.jgameboy.core;

import com.meadowsapps.jgameboy.gbc.core.GbcCore;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class CoreFactory {

    private static CoreFactory factory = new CoreFactory();

    private CoreFactory() {
    }

    public static CoreFactory getFactory() {
        return factory;
    }

    public EmulatorCore getCore(CoreType type) {
        EmulatorCore rv = null;
        switch (type) {
            case GAMEBOY:
            case GAMEBOY_COLOR:
                rv = new GbcCore();
                break;
            case GAMEBOY_ADVANCE:
                break;
        }
        rv.initialize();
        return rv;
    }

}
