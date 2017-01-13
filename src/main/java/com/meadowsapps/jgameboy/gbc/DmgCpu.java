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

            switch (opcode) {
                /* NOP */
                case 0x00: {
                    PC.inc();
                    break;
                }

                /* LD BC,d16 */
                case 0x01: {
                    B.write(operand2);
                    C.write(operand1);
                    PC.add(3);
                    break;
                }

                /* LD (BC),A */
                case 0x02: {
                    int addr = B.read() << 8 | C.read();
                    // write(Register.addr, A.read());
                    PC.inc();
                    break;
                }

                /* INC BC */
                case 0x03: {
                    if (C.read() + 1 == 0x100) {
                        C.write(0);
                        if (B.read() + 1 == 0x100) {
                            B.write(0);
                        } else {
                            B.inc();
                        }
                    } else {
                        C.inc();
                    }
                    PC.inc();
                    break;
                }

                /* INC B */
                case 0x04: {
                    int value = B.read();

                    F.set(N_FLAG, 0);
                    F.set(H_FLAG, value == 0xFF || value == 0x0F);
                    F.set(Z_FLAG, value == 0xFF);

                    if (value == 0xFF) {
                        B.write(0x00);
                    } else {
                        B.inc();
                    }
                    PC.inc();
                    break;
                }

                /* DEC B */
                case 0x05: {
                    int value = B.read();

                    F.set(N_FLAG, 1);
                    F.set(H_FLAG, value == 0x00 || value == 0x10);
                    F.set(Z_FLAG, value == 0x01);

                    if (B.read() == 0x00) {
                        B.write(0xFF);
                    } else {
                        B.dec();
                    }
                    PC.inc();
                    break;
                }

                // LD B,d8
                case 0x06: {
                    B.write(operand1);
                    PC.add(2);
                    break;
                }

                // RLCA
                case 0x07: {
                    int bit7 = A.get(7);
                    A.shift(LEFT, 1);
                    F.set(H_FLAG, 0);
                    F.set(N_FLAG, 0);
                    F.set(C_FLAG, bit7);
                    A.set(0, bit7);
                    PC.inc();
                    break;
                }

                // LD (a16),SP
                case 0x08:
                    int addr = (operand2 << 8) + operand1;
                    // write(Register.addr, SP.read());
                    PC.add(3);
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
                    C.inc();
                    break;

                // DEC C
                case 0x0D:
                    C.dec();
                    break;

                // LD C,d8
                case 0x0E:
//                    length = 2;
                    break;

                // RRCA
                case 0x0F:
                    break;

                // STOP 0
                case 0x10:
//                    length = 2;
                    break;

                // LD DE,d16
                case 0x11:
//                    length = 3;
                    break;

                // LD (DE),A
                case 0x12:
                    break;

                // INC DE
                case 0x13:
                    break;

                // INC D
                case 0x14:
                    D.inc();
                    break;

                // DEC D
                case 0x15:
                    D.dec();
                    break;

                // LD D,d8
                case 0x16:
//                    length = 2;
                    break;
            }

//            Register.Register.add(PC, length);
        }
    }
}
