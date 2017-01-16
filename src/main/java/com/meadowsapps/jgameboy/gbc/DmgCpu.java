package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.*;

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
            int length = execute(opcode, operand1, operand2);
            PC.add(length);
        }
    }

    public int execute(int opcode, int operand1, int operand2) {
        int length = 1;

        int d8 = operand1;
        int d16 = (operand2 << 8) + operand1;
        int r8 = operand1;

        switch (opcode) {
            // NOP
            case 0x00: {
                break;
            }

            // LD BC,d16
            case 0x01: {
                ld_d16(d16, B, C);
                length = 3;
                break;
            }

            // LD (BC),A
            case 0x02: {
                int addr = (B.read() << 8) + C.read();
                // write(Register.addr, A.read());
                break;
            }

            // INC BC
            case 0x03: {
                inc(B, C);
                break;
            }

            // INC B
            case 0x04: {
                inc(B);
                break;
            }

            // DEC B
            case 0x05: {
                dec(B);
                break;
            }

            // LD B,d8
            case 0x06: {
                ld_d8(d8, B);
                length = 2;
                break;
            }

            // RLCA
            case 0x07: {
                int bit7 = A.get(7);
                A.shift(LEFT, 1);
                F.set(C_FLAG, bit7);
                F.set(Z_FLAG, 0);
                F.set(H_FLAG, 0);
                F.set(N_FLAG, 0);
                A.set(0, bit7);
                break;
            }

            // LD (a16),SP
            case 0x08: {
                int addr = (operand2 << 8) + operand1;
                // write(add, SP.read());
                length = 3;
                break;
            }

            // ADD HL,BC
            case 0x09: {
                add(H, L, B, C);
                break;
            }

            // LD A,(BC)
            case 0x0A: {
                int addr = (B.read() << 8) + C.read();
                int value = 0; // = read(addr);
                A.write(value);
                break;
            }

            // DEC BC
            case 0x0B: {
                dec(B, C);
                break;
            }

            // INC C
            case 0x0C: {
                inc(C);
                break;
            }

            // DEC C
            case 0x0D: {
                dec(C);
                break;
            }

            // LD C,d8
            case 0x0E: {
                C.write(operand1);
                length = 2;
                break;
            }

            // RRCA
            case 0x0F: {
                int bit0 = A.get(0);
                A.shift(RIGHT, 1);
                F.set(C_FLAG, bit0);
                A.set(7, bit0);

                F.set(Z_FLAG, 0);
                F.set(H_FLAG, 0);
                F.set(N_FLAG, 0);
                break;
            }

            // STOP 0
            case 0x10: {
                // TODO: there something that is done here
                length = 2;
                break;
            }

            // LD DE,d16
            case 0x11: {
                ld_d16(d16, D, E);
                break;
            }

            // LD (DE),A
            case 0x12: {
                int addr = (D.read() << 8) + E.read();
                // write(A.read(), addr);
                break;
            }

            // INC DE
            case 0x13: {
                inc(D, E);
                break;
            }

            // INC D
            case 0x14: {
                inc(D);
                break;
            }

            // DEC D
            case 0x15: {
                dec(D);
                break;
            }

            // LD D,d8
            case 0x16: {
                ld_d8(d8, D);
                break;
            }

            // RLA
            case 0x17: {
                int bit7 = A.get(7);
                A.shift(LEFT, 1);
                int bitC = F.get(C_FLAG);
                F.set(C_FLAG, bit7);
                A.set(0, bitC);

                F.set(Z_FLAG, 0);
                F.set(N_FLAG, 0);
                F.set(H_FLAG, 0);
                break;
            }

            // JR r8
            case 0x18: {
                PC.add(r8);
                length = 2;
                break;
            }

            // ADD HL,DE
            case 0x19: {
                add(H, L, D, E);
                break;
            }

            // LD A,(DE)
            case 0x1A: {
                int addr = (D.read() << 8) + E.read();
                int value = 0; // = read(addr);
                A.write(value);
                break;
            }

            // DEC DE
            case 0x1B: {
                dec(D, E);
                break;
            }

            // INC E
            case 0x1C: {
                inc(E);
                break;
            }

            // DEC E
            case 0x1D: {
                dec(E);
                break;
            }

            // LD E,d8
            case 0x1E: {
                ld_d8(d8, E);
                break;
            }

            // RRA
            case 0x1F: {
                int bit0 = A.get(0);
                A.shift(RIGHT, 1);
                int bitC = F.get(C_FLAG);
                F.set(C_FLAG, bit0);
                A.set(7, bitC);

                F.set(Z_FLAG, 0);
                F.set(N_FLAG, 0);
                F.set(H_FLAG, 0);
                break;
            }

            // JR NZ,r8
            case 0x20: {
                if (!F.isSet(Z_FLAG)) {
                    PC.add(r8);
                }
                length = 2;
                break;
            }

            // LD HL,d16
            case 0x21: {
                ld_d16(d16, H, L);
                length = 3;
                break;
            }

            // LD (HL+),A
            case 0x22: {
                int addr = (H.read() << 8) + L.read();
                // write(A.read(), addr);
                inc(H, L);
                break;
            }

            // INC HL
            case 0x23: {
                inc(H, L);
                break;
            }

            // INC H
            case 0x24: {
                inc(H);
                break;
            }

            // DEC H
            case 0x25: {
                dec(H);
                break;
            }

            // LD H,d8
            case 0x26: {
                ld_d8(d8, H);
                length = 2;
                break;
            }

            // DAA
            case 0x27: {
                int correctionFactor = 0;
                if (A.read() > 0x99 || F.isSet(C_FLAG)) {
                    correctionFactor = 0x60 + (correctionFactor & 0x0F);
                    F.set(C_FLAG, 1);
                } else {
                    correctionFactor = 0x00;
                    F.set(C_FLAG, 0);
                }

                if ((A.read() & 0x0F) > 0x09 || F.isSet(H_FLAG)) {
                    correctionFactor = (correctionFactor & 0xF0) + 0x06;
                } else {
                    correctionFactor = (correctionFactor & 0xF0) + 0x00;
                }

                if (!F.isSet(N_FLAG)) {
                    A.add(correctionFactor);
                } else {
                    A.subtract(correctionFactor);
                }
                break;
            }

            // JR Z,r8
            case 0x28: {
                if (F.isSet(Z_FLAG)) {
                    PC.add(r8);
                }
                length = 2;
                break;
            }

            // ADD HL,HL
            case 0x29: {
                add(H, L, H, L);
                break;
            }

            // LD A,(HL+)
            case 0x2A: {
                int addr = (H.read() << 8) + L.read();
                int value = 0; // = read(addr);
                A.write(value);
                inc(H, L);
                break;
            }

            // DEC HL
            case 0x2B: {
                dec(H, L);
                break;
            }

            // INC L
            case 0x2C: {
                inc(L);
                break;
            }

            // DEC L
            case 0x2D: {
                dec(L);
                break;
            }

            // LD L,d8
            case 0x2E: {
                ld_d8(d8, L);
                length = 2;
                break;
            }

            // CPL
            case 0x2F: {
                A.invert();

                F.set(N_FLAG, 1);
                F.set(H_FLAG, 1);
                break;
            }

            // JR NC,r8
            case 0x30: {
                if (F.isSet(C_FLAG)) {
                    PC.add(r8);
                }
                length = 2;
                break;
            }

            // LD SP,d16
            case 0x31: {
                ld_d16(d16, SP);
                length = 3;
                break;
            }

            // LD (HL-),A
            case 0x32: {
                int addr = (H.read() << 8) + L.read();
                // write(A.read(), addr);
                dec(H, L);
                break;
            }

            // INC SP
            case 0x33: {
                inc(SP);
                break;
            }

            // INC (HL)
            case 0x34: {
                inc_addr(H, L);
                break;
            }

            default:
                throw new OpCodeException(opcode);
        }

        return length;
    }

    public int read(int address) {
        return Mmu.read(address);
    }

    public void write(int value, int address) {
        Mmu.write(value, address);
    }

    private void inc(Register r) {
        int value = r.read();

        F.set(N_FLAG, 0);
        F.set(H_FLAG, value == 0xFF || value == 0x0F);
        F.set(Z_FLAG, value == 0xFF);

        if (value == 0xFF) {
            r.write(0x00);
        } else {
            r.inc();
        }
    }

    private void inc(Register r1, Register r2) {
        int value = (r1.read() << 8) + r2.read();
        value++;
        r1.write(value >> 8);
        r2.write(value & 0xFF);
    }

    private void inc_addr(Register r) {
        int addr = r.read();
        int value = 0; // = read(addr);
        value++;
        // write(value, addr);
    }

    private void inc_addr(Register r1, Register r2) {
        int addr = (r1.read() << 8) + r2.read();
        int value = 0; // = read(addr);

        F.set(N_FLAG, 0);
        F.set(H_FLAG, value == 0xFF || value == 0x0F);
        F.set(Z_FLAG, value == 0xFF);

        if (value == 0xFF) {
            value = 0x00;
        } else {
            value++;
        }

        // write(value, addr);
    }

    private void dec(Register r) {
        int value = r.read();

        F.set(N_FLAG, 1);
        F.set(H_FLAG, value == 0x00 || value == 0x10);
        F.set(Z_FLAG, value == 0x01);

        if (value == 0x00) {
            r.write(0xFF);
        } else {
            r.dec();
        }
    }

    private void dec(Register r1, Register r2) {
        int value = (r1.read() << 8) + r2.read();
        value--;
        r1.write(value >> 8);
        r2.write(value & 0xFF);
    }

    private void dec_addr(Register r1, Register r2) {
        int addr = (r1.read() << 8) + r2.read();
        int value = 0; // = read(addr);

        F.set(N_FLAG, 1);
        F.set(H_FLAG, value == 0x00 || value == 0x10);
        F.set(Z_FLAG, value == 0x01);

        if (value == 0x00) {
            value = 0xFF;
        } else {
            value--;
        }

        // write(value, addr);
    }

    private void ld_d8(int d8, Register r) {
        r.write(d8);
    }

    private void ld_d16(int d16, Register r) {
        r.write(d16);
    }

    private void ld_d16(int d16, Register r1, Register r2) {
        r1.write(d16 >> 8);
        r2.write(d16 & 0xFF);
    }

    private void add(Register r) {
        int value = A.read();
        F.set(N_FLAG, 0);

        A.add(r.read());
    }

    private void add(Register r1_1, Register r1_2, Register r2_1, Register r2_2) {
        int value1 = (r1_1.read() << 8) + r1_2.read();
        int value2 = (r2_1.read() << 8) + r2_2.read();
        int sum = value1 + value2;
        r1_1.write(sum >> 8);
        r1_2.write(sum & 0xFF);

        F.set(N_FLAG, 0);
        F.set(H_FLAG, ((value1 & 0x0FFF) + (value2 & 0x0FFF)) > 0xFFF);
        F.set(C_FLAG, sum > 0xFFFF);
    }

    private void sub(Register r) {
        A.subtract(r.read());
    }

    private void sub(Register r1_1, Register r1_2, Register r2_1, Register r2_2) {
        int value1 = (r1_1.read() << 8) + r1_2.read();
        int value2 = (r2_1.read() << 8) + r2_2.read();
        int difference = value1 - value2;
        r1_1.write(difference >> 8);
        r1_2.write(difference & 0xFF);
    }
}
