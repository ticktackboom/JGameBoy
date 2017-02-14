package com.meadowsapps.jgameboy.core;

import java.io.File;

/**
 * Created by Dylan on 2/6/17.
 */
public enum CoreType {
    GAMEBOY,
    GAMEBOY_COLOR,
    GAMEBOY_ADVANCE;

    public static CoreType getType(File file) {
        CoreType rv = null;
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
