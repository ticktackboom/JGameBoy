package com.meadowsapps.jgameboy.groovy.gbc.element.apu

import com.meadowsapps.jgameboy.groovy.core.element.apu.Apu
import com.meadowsapps.jgameboy.groovy.core.util.InitializationException
import com.meadowsapps.jgameboy.groovy.gbc.element.GbcCoreElement
import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/2/2017.
 */
@InheritConstructors
class GbcApu extends GbcCoreElement implements Apu {
    @Override
    void initialize() throws InitializationException {

    }

    @Override
    void reset() {

    }
}
