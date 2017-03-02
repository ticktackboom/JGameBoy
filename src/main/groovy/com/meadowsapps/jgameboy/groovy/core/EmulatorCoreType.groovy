package com.meadowsapps.jgameboy.groovy.core

/**
 * Created by dmeadows on 3/2/2017.
 */
enum EmulatorCoreType {
    GAMEBOY,
    GAMEBOY_COLOR,
    GAMEBOY_ADVANCE

    static EmulatorCoreType getType(File file) {
        EmulatorCoreType rv = null;
        if (file != null) {
            String name = file.getName().toLowerCase();
            if (name.endsWith("gb")) {
                rv = GAMEBOY;
            } else if (name.endsWith("gbc")) {
                rv = GAMEBOY_COLOR;
            } else if (name.endsWith("gba")) {
                rv = GAMEBOY_ADVANCE;
            }
        }
        return rv;
    }

}