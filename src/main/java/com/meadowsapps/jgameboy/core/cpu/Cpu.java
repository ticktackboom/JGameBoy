package com.meadowsapps.jgameboy.core.cpu;

import com.meadowsapps.jgameboy.core.CoreElement;

/**
 * Created by dmeadows on 1/11/2017.
 */
public interface Cpu extends CoreElement {

    void execute(int opcode);

}
