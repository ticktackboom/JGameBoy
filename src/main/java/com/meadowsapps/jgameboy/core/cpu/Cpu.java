package com.meadowsapps.jgameboy.core.cpu;

import com.meadowsapps.jgameboy.core.CoreElement;
import com.meadowsapps.jgameboy.core.Steppable;

/**
 * Created by dmeadows on 1/11/2017.
 */
public interface Cpu extends CoreElement, Steppable {

    void execute(int opcode);

}
