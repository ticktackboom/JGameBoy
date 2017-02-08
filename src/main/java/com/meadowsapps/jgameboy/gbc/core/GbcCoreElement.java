package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.CoreElement;

/**
 * Created by dmeadows on 2/7/2017.
 */
public interface GbcCoreElement extends CoreElement {

    GbcCpu cpu();

    GbcGpu gpu();

    GbcMmu mmu();

    GbcCartridge cartridge();
}
