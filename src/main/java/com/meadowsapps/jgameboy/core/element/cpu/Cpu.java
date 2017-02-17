package com.meadowsapps.jgameboy.core.element.cpu;

import com.meadowsapps.jgameboy.core.element.CoreElement;
import com.meadowsapps.jgameboy.core.element.Steppable;

/**
 * Created by dmeadows on 1/11/2017.
 */
public interface Cpu extends CoreElement, Steppable {

    void execute(int opcode);

}
