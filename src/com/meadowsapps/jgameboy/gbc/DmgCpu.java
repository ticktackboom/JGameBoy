package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.Cpu;

/**
 * Created by Dylan on 1/6/17.
 */
public class DmgCpu implements Cpu, Constants {

    public void execute(int opcode) {
        int length = 1;
        switch (opcode) {
            case 0x00:
                // NOP
                break;
            case 0x01:
                // LD BC,d16
                length = 3;
                break;
            case 0x02:
                // LD (BC),A
                break;
            case 0x03:
                // INC BC
                break;
            case 0x04:
                // INC B
                Register.inc(B);
                break;
            case 0x05:
                // DEC B
                Register.dec(B);
                break;
            case 0x06:
                // LD B,d8
                length = 2;
                break;
            case 0x07:
                // RLCA
                break;
            case 0x08:
                // LD (a16),SP
                length = 3;
                break;
            case 0x09:
                // ADD HL,BC
                break;
            case 0x0A:
                // LD A,(BC)
                break;
            case 0x0B:
                // DEC BC
                break;
            case 0x0C:
                // INC C
                Register.inc(C);
                break;
            case 0x0D:
                // DEC C
                Register.dec(C);
                break;
            case 0x0E:
                // LD C,d8
                length = 2;
                break;
            case 0x0F:
                // RRCA
                break;
        }

        Register.add(PC, length);
    }

}
