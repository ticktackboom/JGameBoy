package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.core.EmulatorCore;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class CoreFactory {

    public static EmulatorCore getCore() {
        return new GbcCore();
    }

}
