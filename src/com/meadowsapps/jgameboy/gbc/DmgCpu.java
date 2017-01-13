package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.Cpu;

/**
 * Created by Dylan on 1/6/17.
 */
public class DmgCpu implements Cpu {

    /**
     * 8-Bit Registers
     */
    private Register A, F, B, C, D, E, H, L;

    /**
     * 16-Bit Registers
     */
    private Register SP, PC;

    public DmgCpu() {
        A = new Register();
        F = new Register();
        B = new Register();
        C = new Register();
        D = new Register();
        E = new Register();
        H = new Register();
        L = new Register();
        SP = new Register();
        PC = new Register();
    }

    public void execute(int numInstructions) {
        for (int r = 0; r != numInstructions; r++) {
            int opcode = 1; // memory.read(PC.read());

            int length = 1;
            switch (opcode) {
                // NOP
                case 0x00:
                    break;

                // LD BC,d16
                case 0x01:
                    length = 3;
                    break;

                // LD (BC),A
                case 0x02:
                    break;

                // INC BC
                case 0x03:
                    break;

                // INC B
                case 0x04:
                    Register.inc(B);
                    break;

                // DEC B
                case 0x05:
                    Register.dec(B);
                    break;

                // LD B,d8
                case 0x06:
                    length = 2;
                    break;

                // RLCA
                case 0x07:
                    break;

                // LD (a16),SP
                case 0x08:
                    length = 3;
                    break;

                // ADD HL,BC
                case 0x09:
                    break;

                // LD A,(BC)
                case 0x0A:
                    break;

                // DEC BC
                case 0x0B:
                    break;

                // INC C
                case 0x0C:
                    Register.inc(C);
                    break;

                // DEC C
                case 0x0D:
                    Register.dec(C);
                    break;

                // LD C,d8
                case 0x0E:
                    length = 2;
                    break;

                // RRCA
                case 0x0F:
                    break;

                // STOP 0
                case 0x10:
                    length = 2;
                    break;

                // LD DE,d16
                case 0x11:
                    length = 3;
                    break;

                // LD (DE),A
                case 0x12:
                    break;

                // INC DE
                case 0x13:
                    break;

                // INC D
                case 0x14:
                    Register.inc(D);
                    break;

                // DEC D
                case 0x15:
                    Register.dec(D);
                    break;

                // LD D,d8
                case 0x16:
                    length = 2;
                    break;
            }

            Register.add(PC, length);
        }
    }

}
