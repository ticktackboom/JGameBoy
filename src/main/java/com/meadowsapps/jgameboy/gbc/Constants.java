package com.meadowsapps.jgameboy.gbc;

/**
 * Created by Dylan on 1/13/17.
 */
public interface Constants {
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
