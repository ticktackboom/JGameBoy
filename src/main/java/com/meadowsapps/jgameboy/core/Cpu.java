package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/11/2017.
 */
public interface Cpu {

    void execute(int numInstructions);

    int execute(int opcode, int operand1, int operand2);

    int read(int addr);

    void write(int value, int addr);
}
