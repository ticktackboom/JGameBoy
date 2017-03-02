package com.meadowsapps.jgameboy.groovy.core

import com.meadowsapps.jgameboy.core.CoreType

/**
 * Created by dmeadows on 3/2/2017.
 */
class EmulatorCoreFactory {

    private static EmulatorCoreFactory factory = new EmulatorCoreFactory();

    private EmulatorCoreFactory() {
    }

    EmulatorCore getCore(CoreType type) {
        EmulatorCore core = null
        switch (type) {
            case CoreType.GAMEBOY:
            case CoreType.GAMEBOY_COLOR:
                break
            case CoreType.GAMEBOY_ADVANCE:
                break
        }
        return core
    }

    static EmulatorCoreFactory getFactory() {
        return factory
    }
}
