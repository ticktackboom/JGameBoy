package com.meadowsapps.jgameboy.gbc;

/**
 * Created by Dylan on 1/6/17.
 */
public interface Constants {
    Register A = new Register();
    Register F = new Register();
    Register B = new Register();
    Register C = new Register();
    Register D = new Register();
    Register E = new Register();
    Register SP = new Register(0xFFFE);
    Register PC = new Register(0x100);

    /**
     * Zero flag
     */
    short ZERO_FLAG = 0x80;
    /**
     * Subtract/negative flag
     */
    short SUBTRACT_FLAG = 0x40;
    /**
     * Half carry flag
     */
    short HALF_CARRY_FLAG = 0x20;
    /**
     * Carry flag
     */
    short CARRY_FLAG = 0x10;
}
