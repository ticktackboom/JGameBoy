package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.Cpu;
import com.meadowsapps.jgameboy.Register;

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
        A = new Register8Bit();
        F = new Register8Bit();
        B = new Register8Bit();
        C = new Register8Bit();
        D = new Register8Bit();
        E = new Register8Bit();
        H = new Register8Bit();
        L = new Register8Bit();
        SP = new Register16Bit();
        PC = new Register16Bit();
    }

    public void execute(int numInstructions) {
        for (int r = 0; r != numInstructions; r++) {
            int opcode = 1; // memory.read(PC.read());
            int operand1 = 2; // memory.read(PC.read() + 1);
            int operand2 = 3; // memory.read(PC.read() + 2);

            int length = 1;
            switch (opcode) {
                // NOP
                case 0x00:
                    break;

                // LD BC,d16
                case 0x01:
                    length = 3;
                    B.write(operand1);
                    C.write(operand2);
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

    public static void main(String[] args) {
        DmgCpu cpu = new DmgCpu();
        int value = 256;
        System.out.println(value - (value >> 8));
        System.out.println(value >> 8);
    }
}
