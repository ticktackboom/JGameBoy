package com.meadowsapps.jgameboy.core.ram;

import com.meadowsapps.jgameboy.core.CoreElement;

/**
 * Created by Dylan on 2/11/17.
 */
public interface Ram extends CoreElement {

    int read(int addr);

    void write(int value, int addr);
}
