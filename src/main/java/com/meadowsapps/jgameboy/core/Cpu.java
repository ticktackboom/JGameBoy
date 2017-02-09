package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/11/2017.
 */
public interface Cpu extends CoreElement, Steppable {

    void execute(int opcode);

}
