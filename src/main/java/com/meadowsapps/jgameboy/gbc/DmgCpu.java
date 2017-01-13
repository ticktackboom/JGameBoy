package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.Cpu;
import com.meadowsapps.jgameboy.Register;

/**
 * Created by Dylan on 1/6/17.
 */
public class DmgCpu implements Cpu, Constants {

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
            int opcode = 0x03; // memory.read(PC.read());
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
                    B.write(operand2);
                    C.write(operand1);
                    break;

                // LD (BC),A
                case 0x02:
                    int addr = B.read() << 8 | C.read();
                    // write(addr, A.read());
                    break;

                // INC BC
                case 0x03:
                    if (C.read() + 1 == 0x100) {
                        C.write(0);
                        if (B.read() + 1 == 0x100) {
                            B.write(0);
                        } else {
                            Register.inc(B);
                        }
                    } else {
                        Register.inc(C);
                    }
                    break;

                // INC B
                case 0x04:
                    int f = F.read();
                    // zero out CARRY_FLAG
                    f &= CARRY_FLAG;

                    int value = B.read();
                    switch (value) {
                        case 0xFF:
                            f |= HALF_CARRY_FLAG + ZERO_FLAG;
                            break;
                        case 0x0F:
                            f |= HALF_CARRY_FLAG;
                            break;
                    }
                    value = (value == 0xFF) ? 0 : value + 1;
                    B.write(value);
                    break;

                // DEC B
                case 0x05:
                    Register.dec(B);
                    break;

                // LD B,d8
                case 0x06:
                    length = 2;
                    B.write(operand1);
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
        cpu.execute(0x100);

    }
}
