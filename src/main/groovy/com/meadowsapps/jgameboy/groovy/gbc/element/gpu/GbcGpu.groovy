package com.meadowsapps.jgameboy.groovy.gbc.element.gpu

import com.meadowsapps.jgameboy.groovy.core.element.gpu.Gpu
import com.meadowsapps.jgameboy.groovy.core.util.InitializationException
import com.meadowsapps.jgameboy.groovy.gbc.element.GbcCoreElement
import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/2/2017.
 */
@InheritConstructors
class GbcGpu extends GbcCoreElement implements Gpu {
    @Override
    void initialize() throws InitializationException {

    }

    @Override
    void reset() {

    }
}
