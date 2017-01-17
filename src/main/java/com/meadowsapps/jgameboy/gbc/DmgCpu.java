package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.core.*;

/**
 * Emulated CPU found inside of the Nintendo GameBoy.
 * Custom 8-bit Sharp LR35902 based on the Intel 8080
 * and the Z80 microprocessors.
 */
public class DmgCpu extends AbstractCpu implements Constants {

    /**
     * Accumulator Register
     */
    private Register8Bit A;

    /**
     * Status Register
     */
    private Register8Bit F;

    /**
     * General purpose Register
     */
    private Register8Bit B, C, D, E, H, L;

    /**
     * Stack Pointer Register
     */
    private Register16Bit SP;

    /**
     * Program Counter Register
     */
    private Register16Bit PC;

    /**
     * Bit index of the Zero Status Flag
     */
    public static final int Z_FLAG = 7;

    /**
     * Bit index of the Subtract Status Flag
     */
    public static final int N_FLAG = 6;

    /**
     * Bit index of the Half Carry Status Flag
     */
    public static final int H_FLAG = 5;

    /**
     * Bit index of the Carry Status Flag
     */
    public static final int C_FLAG = 4;

    /**
     * Initializes the CPU's registers
     */
    public DmgCpu(GbcCore core) {
        super(core);
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

    /**
     * Executes the specified number of instructions. If <code>numInstructions</code>
     * equals -1, then the CPU will execute infinitely until <code>interrupted</code>
     * is set to <code>true</code>.
     *
     * @param numInstructions the number of instructions to execute
     */
    @Override
    public void execute(int numInstructions) {
        for (int r = 0; r != numInstructions; r++) {
            int opcode = read(PC.read());
            int operand1 = read(PC.read() + 1);
            int operand2 = read(PC.read() + 2);
            int length = execute(opcode, operand1, operand2);
            PC.add(length);
        }
    }

    /**
     * Executes the given opcode with the available operands and returns
     * the length of the instruction to add to the <code>PC</code> register.
     *
     * @param opcode   opcode to execute
     * @param operand1 operand1 potentially used for the opcode
     * @param operand2 operand2 potentially used for the opcode
     * @return the length of the instruction
     */
    @Override
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
                write(addr, A.read());
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
                write(addr, SP.read());
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
                int value = read(addr);
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
                write(A.read(), addr);
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
                int value = read(addr);
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
                write(A.read(), addr);
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
                int value = read(addr);
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
                write(A.read(), addr);
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

            // DEC (HL)
            case 0x35: {
                dec_addr(H, L);
                break;
            }

            // LD (HL),d8
            case 0x36: {
                int addr = (H.read() << 8) + L.read();
                write(d8, addr);
                length = 2;
                break;
            }

            // SCF
            case 0x37: {
                F.set(N_FLAG, 0);
                F.set(H_FLAG, 0);
                F.set(C_FLAG, 1);
                break;
            }

            // JR C,r8
            case 0x38: {
                if (F.isSet(C_FLAG)) {
                    PC.add(r8);
                }
                length = 2;
                break;
            }

            // ADD HL,SP
            case 0x39: {
                int hl = (H.read() << 8) + L.read();
                int sp = SP.read();
                int sum = hl + sp;
                H.write(sum >> 8);
                L.write(sum & 0xFF);

                F.set(N_FLAG, 0);
                F.set(H_FLAG, ((hl & 0x0FFF) + (sp & 0x0FFF)) > 0xFFF);
                F.set(C_FLAG, sum > 0xFFFF);
                break;
            }

            // LD A,(HL-)
            case 0x3A: {
                int addr = (H.read() << 8) + L.read();
                int value = read(addr);
                A.write(value);
                dec(H, L);
                break;
            }

            // DEC SP
            case 0x3B: {
                dec(SP);
                break;
            }

            // INC A
            case 0x3C: {
                inc(A);
                break;
            }

            // DEC A
            case 0x3D: {
                dec(A);
                break;
            }

            // LD A,d8
            case 0x3E: {
                ld_d8(d8, A);
                length = 2;
                break;
            }

            // CCF
            case 0x3F: {
                F.set(N_FLAG, 0);
                F.set(H_FLAG, 0);
                F.flip(C_FLAG);
                break;
            }

            // LD B,B
            case 0x40: {
                ld(B, B);
                break;
            }

            // LD B,C
            case 0x41: {
                ld(B, C);
                break;
            }

            // LD B,D
            case 0x42: {
                ld(B, D);
                break;
            }

            // LD B,E
            case 0x43: {
                ld(B, E);
                break;
            }

            // LD B,H
            case 0x44: {
                ld(B, H);
                break;
            }

            // LD B,L
            case 0x45: {
                ld(B, L);
                break;
            }

            // LD B,(HL)
            case 0x46: {
                ld_from_addr(B, H, L);
                break;
            }

            // LD B,A
            case 0x47: {
                ld(B, A);
                break;
            }

            // LD C,B
            case 0x48: {
                ld(C, B);
                break;
            }

            // LD C,C
            case 0x49: {
                ld(C, C);
                break;
            }

            // LD C,D
            case 0x4A: {
                ld(C, D);
                break;
            }

            // LD C,E
            case 0x4B: {
                ld(C, E);
                break;
            }

            // LD C,H
            case 0x4C: {
                ld(C, H);
                break;
            }

            // LD C,L
            case 0x4D: {
                ld(C, L);
                break;
            }

            // LD C,(HL)
            case 0x4E: {
                ld_from_addr(C, H, L);
                break;
            }

            // LD C,A
            case 0x4F: {
                ld(C, A);
                break;
            }

            // LD D,B
            case 0x50: {
                ld(D, B);
                break;
            }

            // LD D,C
            case 0x51: {
                ld(D, C);
                break;
            }

            // LD D,D
            case 0x52: {
                ld(D, D);
                break;
            }

            // LD D,E
            case 0x53: {
                ld(D, E);
                break;
            }

            // LD D,H
            case 0x54: {
                ld(D, H);
                break;
            }

            // LD D,L
            case 0x55: {
                ld(D, L);
                break;
            }

            // LD D,(HL)
            case 0x56: {
                ld_from_addr(D, H, L);
                break;
            }

            // LD D,A
            case 0x57: {
                ld(D, A);
                break;
            }

            // LD E,B
            case 0x58: {
                ld(E, B);
                break;
            }

            // LD E,C
            case 0x59: {
                ld(E, C);
                break;
            }

            // LD E,D
            case 0x5A: {
                ld(E, D);
                break;
            }

            // LD E,E
            case 0x5B: {
                ld(E, E);
                break;
            }

            // LD E,H
            case 0x5C: {
                ld(E, H);
                break;
            }

            // LD E,L
            case 0x5D: {
                ld(E, L);
                break;
            }

            // LD E,(HL)
            case 0x5E: {
                ld_from_addr(E, H, L);
                break;
            }

            // LD E,A
            case 0x5F: {
                ld(E, A);
                break;
            }

            // LD H,B
            case 0x60: {
                ld(H, B);
                break;
            }

            // LD H,C
            case 0x61: {
                ld(H, C);
                break;
            }

            // LD H,D
            case 0x62: {
                ld(H, D);
                break;
            }

            // LD H,E
            case 0x63: {
                ld(H, E);
                break;
            }

            // LD H,H
            case 0x64: {
                ld(H, H);
                break;
            }

            // LD H,L
            case 0x65: {
                ld(H, L);
                break;
            }

            // LD H,(HL)
            case 0x66: {
                ld_from_addr(H, H, L);
                break;
            }

            // LD H,A
            case 0x67: {
                ld(H, A);
                break;
            }

            // LD L,B
            case 0x68: {
                ld(L, B);
                break;
            }

            // LD L,C
            case 0x69: {
                ld(L, C);
                break;
            }

            // LD L,D
            case 0x6A: {
                ld(L, D);
                break;
            }

            // LD L,E
            case 0x6B: {
                ld(L, E);
                break;
            }

            // LD L,H
            case 0x6C: {
                ld(L, H);
                break;
            }

            // LD L,L
            case 0x6D: {
                ld(L, L);
                break;
            }

            // LD L,(HL)
            case 0x6E: {
                ld_from_addr(L, H, L);
                break;
            }

            // LD L,A
            case 0x6F: {
                ld(L, A);
                break;
            }

            // LD (HL),B
            case 0x70: {
                ld_to_addr(H, L, B);
                break;
            }

            // LD (HL),C
            case 0x71: {
                ld_to_addr(H, L, C);
                break;
            }

            // LD (HL),D
            case 0x72: {
                ld_to_addr(H, L, D);
                break;
            }

            // LD (HL),E
            case 0x73: {
                ld_to_addr(H, L, E);
                break;
            }

            // LD (HL),H
            case 0x74: {
                ld_to_addr(H, L, H);
                break;
            }

            // LD (HL),L
            case 0x75: {
                ld_to_addr(H, L, L);
                break;
            }

            // HALT
            case 0x76: {
                break;
            }

            // LD (HL),A
            case 0x77: {
                ld_to_addr(H, L, A);
                break;
            }

            // LD A,B
            case 0x78: {
                ld(A, B);
                break;
            }

            // LD A,C
            case 0x79: {
                ld(A, C);
                break;
            }

            // LD A,D
            case 0x7A: {
                ld(A, D);
                break;
            }

            // LD A,E
            case 0x7B: {
                ld(A, E);
                break;
            }

            // LD A,H
            case 0x7C: {
                ld(A, H);
                break;
            }

            // LD A,L
            case 0x7D: {
                ld(A, L);
                break;
            }

            // LD A,(HL)
            case 0x7E: {
                ld_from_addr(A, H, L);
                break;
            }

            // LD A,A
            case 0x7F: {
                ld(A, A);
                break;
            }

            // XOR A
            case 0xAF: {
                xor(A);
                break;
            }

            default:
                throw new OpCodeException(opcode);
        }
        return length;
    }

    private int executeCB() {
        return -1;
    }

    /**
     * Increments the 8-Bit register <code>r</code>. If the current value equals
     * 0xFF then the value rolls over to 0.
     * </br>
     * <b>Flag Alteration:</b>
     * <ul>
     * <li><code>Z_FLAG</code>: Set if <code>r</code>'s value before incrementing equals
     * the maximum value of an 8-Bit Register (0xFF)</li>
     * <li><code>N_FLAG</code>: Reset to 0</li>
     * <li><code>H_FLAG</code>: Set if <code>r</code>'s value before incrementing equals
     * 0xFF or 0x0F</li>
     * <li><code>C_FLAG</code>: Unaffected</li>
     * </ul>
     *
     * @param r the register to increment
     */
    private void inc(Register8Bit r) {
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

    /**
     * Increments the 16-Bit register <code>r</code>.
     * </br>
     * <b>Flag Alteration:</b>
     * <ul>
     * <li><code>Z_FLAG</code>: Unaffected</li>
     * <li><code>N_FLAG</code>: Unaffected</li>
     * <li><code>H_FLAG</code>: Unaffected</li>
     * <li><code>C_FLAG</code>: Unaffected</li>
     * </ul>
     *
     * @param r the register to increment
     */
    private void inc(Register16Bit r) {
        r.inc();
    }

    /**
     * Combines the two 8-Bit registers, <code>r1</code> and <code>r2</code>,
     * to behave as a 16-Bit register and increments the value.
     * </br>
     * <b>Flag Alteration:</b>
     * <ul>
     * <li><code>Z_FLAG</code>: Unaffected</li>
     * <li><code>N_FLAG</code>: Unaffected</li>
     * <li><code>H_FLAG</code>: Unaffected</li>
     * <li><code>C_FLAG</code>: Unaffected</li>
     * </ul>
     *
     * @param r1 Register that contains the upper 8 bytes of the value to increment
     * @param r2 Register that contains the lower 8 bytes of the value to increment
     */
    private void inc(Register8Bit r1, Register8Bit r2) {
        int value = (r1.read() << 8) + r2.read();
        value++;
        r1.write(value >> 8);
        r2.write(value & 0xFF);
    }

    /**
     * Increments the value located at the address given by the 16-Bit register
     * <code>r</code>. If the value before incrementing equals 0xFF, then the value
     * rolls over to 0.
     * </br>
     * <b>Flag Alteration:</b>
     * <ul>
     * <li><code>Z_FLAG</code>: Set if the value before incrementing equals 0xFF.</li>
     * <li><code>N_FLAG</code>: Reset to 0</li>
     * <li><code>H_FLAG</code>: Set if the value before incrementing equals 0xFF or 0x0F</li>
     * <li><code>C_FLAG</code>: Unaffected</li>
     * </ul>
     *
     * @param r the register containing the address of the value to increment
     */
    private void inc_addr(Register16Bit r) {
        int addr = r.read();
        int value = 0; // = read(addr);

        F.set(N_FLAG, 0);
        F.set(H_FLAG, value == 0xFF || value == 0x0F);
        F.set(Z_FLAG, value == 0xFF);
        if (value == 0xFF) {
            value = 0x00;
        } else {
            value++;
        }
        value++;
        // write(value, addr);
    }

    /**
     * Combines the two 8-Bit registers, <code>r1</code> and <code>r2</code>,
     * to behave as a 16-Bit register and increments the value.
     * </br>
     * <b>Flag Alteration:</b>
     * <ul>
     * <li><code>Z_FLAG</code>: Unaffected</li>
     * <li><code>N_FLAG</code>: Unaffected</li>
     * <li><code>H_FLAG</code>: Unaffected</li>
     * <li><code>C_FLAG</code>: Unaffected</li>
     * </ul>
     *
     * @param r1 Register that contains the upper 8 bytes of the value to increment
     * @param r2 Register that contains the lower 8 bytes of the value to increment
     */
    private void inc_addr(Register8Bit r1, Register8Bit r2) {
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

    private void dec(Register8Bit r) {
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

    private void dec(Register16Bit r) {
        r.dec();
    }

    private void dec(Register8Bit r1, Register8Bit r2) {
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

    private void ld(Register8Bit r1, Register8Bit r2) {
        int value = r2.read();
        r1.write(value);
    }

    private void ld_to_addr(Register16Bit r1, Register r2) {

    }

    private void ld_to_addr(Register8Bit r1_1, Register8Bit r1_2, Register8Bit r2) {

    }

    private void ld_from_addr(Register8Bit r1, Register16Bit r2) {
        int addr = r2.read();
        int value = read(addr);
        r1.write(value);
    }

    private void ld_from_addr(Register8Bit r1, Register8Bit r2_1, Register8Bit r2_2) {
        int addr = (r2_1.read() << 8) + r2_2.read();
        int value = read(addr);
        r1.write(value);
    }

    private void add(Register8Bit r) {
        int value = A.read();
        F.set(N_FLAG, 0);

        A.add(r.read());
    }

    private void add(Register16Bit r1, Register16Bit r2) {
        int value1 = r1.read();
        int value2 = r2.read();
        int sum = value1 + value2;
        r1.write(sum);

        F.set(N_FLAG, 0);
        F.set(H_FLAG, ((value1 & 0x0FFF) + (value2 & 0x0FFF)) > 0xFFF);
        F.set(C_FLAG, sum > 0xFFFF);
    }

    private void add(Register16Bit r1, Register8Bit r2_1, Register8Bit r2_2) {
        int value1 = r1.read();
        int value2 = (r2_1.read() << 8) + r2_2.read();
        int sum = value1 + value2;
        r1.write(sum);

        F.set(N_FLAG, 0);
        F.set(H_FLAG, ((value1 & 0x0FFF) + (value2 & 0x0FFF)) > 0xFFF);
        F.set(C_FLAG, sum > 0xFFFF);
    }

    private void add(Register8Bit r1_1, Register8Bit r1_2, Register16Bit r2) {
        int value1 = (r1_1.read() << 8) + r1_2.read();
        int value2 = r2.read();
        int sum = value1 + value2;
        r1_1.write(sum >> 8);
        r1_2.write(sum & 0xFF);

        F.set(N_FLAG, 0);
        F.set(H_FLAG, ((value1 & 0x0FFF) + (value2 & 0x0FFF)) > 0xFFF);
        F.set(C_FLAG, sum > 0xFFFF);
    }

    private void add(Register8Bit r1_1, Register8Bit r1_2, Register8Bit r2_1, Register8Bit r2_2) {
        int value1 = (r1_1.read() << 8) + r1_2.read();
        int value2 = (r2_1.read() << 8) + r2_2.read();
        int sum = value1 + value2;
        r1_1.write(sum >> 8);
        r1_2.write(sum & 0xFF);

        F.set(N_FLAG, 0);
        F.set(H_FLAG, ((value1 & 0x0FFF) + (value2 & 0x0FFF)) > 0xFFF);
        F.set(C_FLAG, sum > 0xFFFF);
    }

    private void sub(Register8Bit r) {
        A.subtract(r.read());
    }

    private void sub(Register8Bit r1_1, Register8Bit r1_2, Register8Bit r2_1, Register8Bit r2_2) {
        int value1 = (r1_1.read() << 8) + r1_2.read();
        int value2 = (r2_1.read() << 8) + r2_2.read();
        int difference = value1 - value2;
        r1_1.write(difference >> 8);
        r1_2.write(difference & 0xFF);
    }

    private void xor(Register8Bit r) {
        int value1 = A.read();
        int value2 = r.read();
        int result = value1 ^ value2;
        A.write(result);

        F.set(Z_FLAG, result == 0);
        F.set(N_FLAG, 0);
        F.set(H_FLAG, 0);
        F.set(C_FLAG, 0);
    }
}
