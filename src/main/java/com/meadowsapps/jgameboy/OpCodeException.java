package com.meadowsapps.jgameboy;

/**
 * Created by Dylan on 1/14/17.
 */
public class OpCodeException extends RuntimeException {

    public OpCodeException(int opcode) {
        super("OpCode not defined: 0x" + Integer.toHexString(opcode));
    }
}
