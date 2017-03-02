package com.meadowsapps.jgameboy.core.element.mmu;

import com.meadowsapps.jgameboy.core.element.CoreElement;

/**
 * Created by dmeadows on 1/17/2017.
 */
public interface Mmu extends CoreElement {

    int read(int addr);

    void write(int value, int addr);

}
