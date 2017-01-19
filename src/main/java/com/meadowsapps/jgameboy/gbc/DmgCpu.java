package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.core.AbstractCpu;
import com.meadowsapps.jgameboy.core.Register16Bit;
import com.meadowsapps.jgameboy.core.Register8Bit;

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
    public static final int Z_BIT = 7;

    /**
     * Bit index of the Subtract Status Flag
     */
    public static final int N_BIT = 6;

    /**
     * Bit index of the Half Carry Status Flag
     */
    public static final int H_BIT = 5;

    /**
     * Bit index of the Carry Status Flag
     */
    public static final int C_BIT = 4;

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
            int opcode = readByte(PC.read());
            int operand1 = readByte(PC.read() + 1);
            int operand2 = readByte(PC.read() + 2);
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
        int a8 = operand1;
        int a16 = (operand2 << 8) + operand1;
        int r8 = (byte) operand1;

        switch (opcode) {
            // NOP
            case 0x00: {
                break;
            }

            // LD BC,d16
            case 0x01: {
                ld(B, C, d16);
                length = 3;
                break;
            }

            // LD (BC),A
            case 0x02: {
                int addr = getAddress(B, C);
                ld(addr, getValue(A));
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
                ld(B, d8);
                length = 2;
                break;
            }

            // RLCA
            case 0x07: {
                int bit7 = A.get(7);
                A.shift(LEFT, 1);
                F.set(C_BIT, bit7);
                F.set(Z_BIT, 0);
                F.set(H_BIT, 0);
                F.set(N_BIT, 0);
                A.set(0, bit7);
                break;
            }

            // LD (a16),SP
            case 0x08: {
                ld(a16, getValue(SP));
                length = 3;
                break;
            }

            // ADD HL,BC
            case 0x09: {
                add(H, L, getValue(B, C));
                break;
            }

            // LD A,(BC)
            case 0x0A: {
                int addr = getAddress(B, C);
                ld(A, getValue(addr));
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
                ld(C, d8);
                length = 2;
                break;
            }

            // RRCA
            case 0x0F: {
                int bit0 = A.get(0);
                A.shift(RIGHT, 1);
                F.set(C_BIT, bit0);
                A.set(7, bit0);

                F.set(Z_BIT, 0);
                F.set(H_BIT, 0);
                F.set(N_BIT, 0);
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
                ld(D, E, d16);
                length = 3;
                break;
            }

            // LD (DE),A
            case 0x12: {
                int addr = getAddress(D, E);
                ld(addr, getValue(A));
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
                ld(D, d8);
                length = 2;
                break;
            }

            // RLA
            case 0x17: {
                int bit7 = A.get(7);
                A.shift(LEFT, 1);
                int bitC = F.get(C_BIT);
                F.set(C_BIT, bit7);
                A.set(0, bitC);

                F.set(Z_BIT, 0);
                F.set(N_BIT, 0);
                F.set(H_BIT, 0);
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
                add(H, L, getValue(D, E));
                break;
            }

            // LD A,(DE)
            case 0x1A: {
                int addr = getAddress(D, E);
                ld(A, getValue(addr));
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
                ld(E, d8);
                length = 2;
                break;
            }

            // RRA
            case 0x1F: {
                int bit0 = A.get(0);
                A.shift(RIGHT, 1);
                int bitC = F.get(C_BIT);
                F.set(C_BIT, bit0);
                A.set(7, bitC);

                F.set(Z_BIT, 0);
                F.set(N_BIT, 0);
                F.set(H_BIT, 0);
                break;
            }

            // JR NZ,r8
            case 0x20: {
                if (!F.isSet(Z_BIT)) {
                    PC.add(r8);
                }
                length = 2;
                break;
            }

            // LD HL,d16
            case 0x21: {
                ld(H, L, d16);
                length = 3;
                break;
            }

            // LD (HL+),A
            case 0x22: {
                int addr = getAddress(H, L);
                ld(addr, getValue(A));
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
                ld(H, d8);
                length = 2;
                break;
            }

            // DAA
            case 0x27: {
                int correctionFactor = 0;
                if (A.read() > 0x99 || F.isSet(C_BIT)) {
                    correctionFactor = 0x60 + (correctionFactor & 0x0F);
                    F.set(C_BIT, 1);
                } else {
                    correctionFactor = 0x00;
                    F.set(C_BIT, 0);
                }

                if ((A.read() & 0x0F) > 0x09 || F.isSet(H_BIT)) {
                    correctionFactor = (correctionFactor & 0xF0) + 0x06;
                } else {
                    correctionFactor = (correctionFactor & 0xF0) + 0x00;
                }

                if (!F.isSet(N_BIT)) {
                    A.add(correctionFactor);
                } else {
                    A.subtract(correctionFactor);
                }
                break;
            }

            // JR Z,r8
            case 0x28: {
                if (F.isSet(Z_BIT)) {
                    PC.add(r8);
                }
                length = 2;
                break;
            }

            // ADD HL,HL
            case 0x29: {
                add(H, L, getValue(H, L));
                break;
            }

            // LD A,(HL+)
            case 0x2A: {
                int addr = getAddress(H, L);
                ld(A, getValue(addr));
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
                ld(L, d8);
                length = 2;
                break;
            }

            // CPL
            case 0x2F: {
                A.invert();
                F.set(N_BIT, 1);
                F.set(H_BIT, 1);
                break;
            }

            // JR NC,r8
            case 0x30: {
                if (!F.isSet(C_BIT)) {
                    PC.add(r8);
                }
                length = 2;
                break;
            }

            // LD SP,d16
            case 0x31: {
                ld(SP, d16);
                length = 3;
                break;
            }

            // LD (HL-),A
            case 0x32: {
                int addr = getAddress(H, L);
                ld(addr, getValue(A));
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
                int addr = getAddress(H, L);
                inc(addr);
                break;
            }

            // DEC (HL)
            case 0x35: {
                int addr = getAddress(H, L);
                dec(addr);
                break;
            }

            // LD (HL),d8
            case 0x36: {
                int addr = getAddress(H, L);
                ld(addr, d8);
                length = 2;
                break;
            }

            // SCF
            case 0x37: {
                F.set(N_BIT, 0);
                F.set(H_BIT, 0);
                F.set(C_BIT, 1);
                break;
            }

            // JR C,r8
            case 0x38: {
                if (F.isSet(C_BIT)) {
                    PC.add(r8);
                }
                length = 2;
                break;
            }

            // ADD HL,SP
            case 0x39: {
                add(H, L, getValue(SP));
                break;
            }

            // LD A,(HL-)
            case 0x3A: {
                int addr = getAddress(H, L);
                ld(A, getValue(addr));
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
                ld(A, d8);
                length = 2;
                break;
            }

            // CCF
            case 0x3F: {
                F.set(N_BIT, 0);
                F.set(H_BIT, 0);
                F.flip(C_BIT);
                break;
            }

            // LD B,B
            case 0x40: {
                ld(B, getValue(B));
                break;
            }

            // LD B,C
            case 0x41: {
                ld(B, getValue(C));
                break;
            }

            // LD B,D
            case 0x42: {
                ld(B, getValue(D));
                break;
            }

            // LD B,E
            case 0x43: {
                ld(B, getValue(E));
                break;
            }

            // LD B,H
            case 0x44: {
                ld(B, getValue(H));
                break;
            }

            // LD B,L
            case 0x45: {
                ld(B, getValue(L));
                break;
            }

            // LD B,(HL)
            case 0x46: {
                int addr = getAddress(H, L);
                ld(B, getValue(addr));
                break;
            }

            // LD B,A
            case 0x47: {
                int value = getValue(A);
                ld(B, value);
                break;
            }

            // LD C,B
            case 0x48: {
                int value = getValue(B);
                ld(C, value);
                break;
            }

            // LD C,C
            case 0x49: {
                int value = getValue(C);
                ld(C, value);
                break;
            }

            // LD C,D
            case 0x4A: {
                int value = getValue(D);
                ld(C, value);
                break;
            }

            // LD C,E
            case 0x4B: {
                int value = getValue(E);
                ld(C, value);
                break;
            }

            // LD C,H
            case 0x4C: {
                int value = getValue(H);
                ld(C, value);
                break;
            }

            // LD C,L
            case 0x4D: {
                int value = getValue(L);
                ld(C, value);
                break;
            }

            // LD C,(HL)
            case 0x4E: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                ld(C, value);
                break;
            }

            // LD C,A
            case 0x4F: {
                int value = getValue(A);
                ld(C, value);
                break;
            }

            // LD D,B
            case 0x50: {
                int value = getValue(B);
                ld(D, value);
                break;
            }

            // LD D,C
            case 0x51: {
                int value = getValue(C);
                ld(D, value);
                break;
            }

            // LD D,D
            case 0x52: {
                int value = getValue(D);
                ld(D, value);
                break;
            }

            // LD D,E
            case 0x53: {
                int value = getValue(E);
                ld(D, value);
                break;
            }

            // LD D,H
            case 0x54: {
                int value = getValue(H);
                ld(D, value);
                break;
            }

            // LD D,L
            case 0x55: {
                int value = getValue(L);
                ld(D, value);
                break;
            }

            // LD D,(HL)
            case 0x56: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                ld(D, value);
                break;
            }

            // LD D,A
            case 0x57: {
                int value = getValue(A);
                ld(D, value);
                break;
            }

            // LD E,B
            case 0x58: {
                int value = getValue(B);
                ld(E, value);
                break;
            }

            // LD E,C
            case 0x59: {
                int value = getValue(C);
                ld(E, value);
                break;
            }

            // LD E,D
            case 0x5A: {
                int value = getValue(D);
                ld(E, value);
                break;
            }

            // LD E,E
            case 0x5B: {
                int value = getValue(E);
                ld(E, value);
                break;
            }

            // LD E,H
            case 0x5C: {
                int value = getValue(H);
                ld(E, value);
                break;
            }

            // LD E,L
            case 0x5D: {
                int value = getValue(L);
                ld(E, value);
                break;
            }

            // LD E,(HL)
            case 0x5E: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                ld(E, value);
                break;
            }

            // LD E,A
            case 0x5F: {
                int value = getValue(A);
                ld(E, value);
                break;
            }

            // LD H,B
            case 0x60: {
                int value = getValue(B);
                ld(H, value);
                break;
            }

            // LD H,C
            case 0x61: {
                int value = getValue(C);
                ld(H, value);
                break;
            }

            // LD H,D
            case 0x62: {
                int value = getValue(D);
                ld(H, value);
                break;
            }

            // LD H,E
            case 0x63: {
                int value = getValue(E);
                ld(H, value);
                break;
            }

            // LD H,H
            case 0x64: {
                int value = getValue(H);
                ld(H, value);
                break;
            }

            // LD H,L
            case 0x65: {
                int value = getValue(L);
                ld(H, value);
                break;
            }

            // LD H,(HL)
            case 0x66: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                ld(H, value);
                break;
            }

            // LD H,A
            case 0x67: {
                int value = getValue(A);
                ld(H, value);
                break;
            }

            // LD L,B
            case 0x68: {
                int value = getValue(B);
                ld(L, value);
                break;
            }

            // LD L,C
            case 0x69: {
                int value = getValue(C);
                ld(L, value);
                break;
            }

            // LD L,D
            case 0x6A: {
                int value = getValue(D);
                ld(L, value);
                break;
            }

            // LD L,E
            case 0x6B: {
                int value = getValue(E);
                ld(L, value);
                break;
            }

            // LD L,H
            case 0x6C: {
                int value = getValue(H);
                ld(L, value);
                break;
            }

            // LD L,L
            case 0x6D: {
                int value = getValue(L);
                ld(L, value);
                break;
            }

            // LD L,(HL)
            case 0x6E: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                ld(L, value);
                break;
            }

            // LD L,A
            case 0x6F: {
                int value = getValue(A);
                ld(L, value);
                break;
            }

            // LD (HL),B
            case 0x70: {
                int addr = getAddress(H, L);
                int value = getValue(B);
                ld(addr, value);
                break;
            }

            // LD (HL),C
            case 0x71: {
                int addr = getAddress(H, L);
                int value = getValue(C);
                ld(addr, value);
                break;
            }

            // LD (HL),D
            case 0x72: {
                int addr = getAddress(H, L);
                int value = getValue(D);
                ld(addr, value);
                break;
            }

            // LD (HL),E
            case 0x73: {
                int addr = getAddress(H, L);
                int value = getValue(E);
                ld(addr, value);
                break;
            }

            // LD (HL),H
            case 0x74: {
                int addr = getAddress(H, L);
                int value = getValue(H);
                ld(addr, value);
                break;
            }

            // LD (HL),L
            case 0x75: {
                int addr = getAddress(H, L);
                int value = getValue(L);
                ld(addr, value);
                break;
            }

            // HALT
            case 0x76: {
                break;
            }

            // LD (HL),A
            case 0x77: {
                int addr = getAddress(H, L);
                int value = getValue(A);
                ld(addr, value);
                break;
            }

            // LD A,B
            case 0x78: {
                int value = getValue(B);
                ld(A, value);
                break;
            }

            // LD A,C
            case 0x79: {
                int value = getValue(C);
                ld(A, value);
                break;
            }

            // LD A,D
            case 0x7A: {
                int value = getValue(D);
                ld(A, value);
                break;
            }

            // LD A,E
            case 0x7B: {
                int value = getValue(E);
                ld(A, value);
                break;
            }

            // LD A,H
            case 0x7C: {
                int value = getValue(H);
                ld(A, value);
                break;
            }

            // LD A,L
            case 0x7D: {
                int value = getValue(L);
                ld(A, value);
                break;
            }

            // LD A,(HL)
            case 0x7E: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                ld(A, value);
                break;
            }

            // LD A,A
            case 0x7F: {
                int value = getValue(A);
                ld(A, value);
                break;
            }

            // ADD A,B
            case 0x80: {
                int value = getValue(B);
                add(A, value);
                break;
            }

            // ADD A,C
            case 0x81: {
                int value = getValue(C);
                add(A, value);
                break;
            }

            // ADD A,D
            case 0x82: {
                int value = getValue(D);
                add(A, value);
                break;
            }

            // ADD A,E
            case 0x83: {
                int value = getValue(E);
                add(A, value);
                break;
            }

            // ADD A,H
            case 0x84: {
                int value = getValue(H);
                add(A, value);
                break;
            }

            // ADD A,L
            case 0x85: {
                int value = getValue(L);
                add(A, value);
                break;
            }

            // ADD A,(HL)
            case 0x86: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                add(A, value);
                break;
            }

            // ADD A,A
            case 0x87: {
                int value = getValue(A);
                add(A, value);
                break;
            }

            // ADC A,B
            case 0x88: {
                int value = getValue(B);
                adc(A, value);
                break;
            }

            // ADC A,C
            case 0x89: {
                int value = getValue(C);
                adc(A, value);
                break;
            }

            // ADC A,D
            case 0x8A: {
                int value = getValue(D);
                adc(A, value);
                break;
            }

            // ADC A,E
            case 0x8B: {
                int value = getValue(E);
                adc(A, value);
                break;
            }

            // ADC A,H
            case 0x8C: {
                int value = getValue(H);
                adc(A, value);
                break;
            }

            // ADC A,L
            case 0x8D: {
                int value = getValue(L);
                adc(A, value);
                break;
            }

            // ADC A,(HL)
            case 0x8E: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                adc(A, value);
                break;
            }

            // ADC A,A
            case 0x8F: {
                int value = getValue(A);
                adc(A, value);
                break;
            }

            // SUB B
            case 0x90: {
                int value = getValue(B);
                sub(A, value);
                break;
            }

            // SUB C
            case 0x91: {
                int value = getValue(C);
                sub(A, value);
                break;
            }

            // SUB D
            case 0x92: {
                int value = getValue(D);
                sub(A, value);
                break;
            }

            // SUB E
            case 0x93: {
                int value = getValue(E);
                sub(A, value);
                break;
            }

            // SUB H
            case 0x94: {
                int value = getValue(H);
                sub(A, value);
                break;
            }

            // SUB L
            case 0x95: {
                int value = getValue(L);
                sub(A, value);
                break;
            }

            // SUB (HL)
            case 0x96: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                sub(A, value);
                break;
            }

            // SUB A
            case 0x97: {
                int value = getValue(A);
                sub(A, value);
                break;
            }

            // SBC A,B
            case 0x98: {
                int value = getValue(B);
                sbc(A, value);
                break;
            }

            // SBC A,C
            case 0x99: {
                int value = getValue(C);
                sbc(A, value);
                break;
            }

            // SBC A,D
            case 0x9A: {
                int value = getValue(D);
                sbc(A, value);
                break;
            }

            // SBC A,E
            case 0x9B: {
                int value = getValue(E);
                sbc(A, value);
                break;
            }

            // SBC A,H
            case 0x9C: {
                int value = getValue(H);
                sbc(A, value);
                break;
            }

            // SBC A,L
            case 0x9D: {
                int value = getValue(L);
                sbc(A, value);
                break;
            }

            // SBC A,(HL)
            case 0x9E: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                sbc(A, value);
                break;
            }

            // SBC A,A
            case 0x9F: {
                int value = getValue(A);
                sbc(A, value);
                break;
            }

            // AND B
            case 0xA0: {
                int value = getValue(B);
                and(A, value);
                break;
            }

            // AND C
            case 0xA1: {
                int value = getValue(C);
                and(A, value);
                break;
            }

            // AND D
            case 0xA2: {
                int value = getValue(D);
                and(A, value);
                break;
            }

            // AND E
            case 0xA3: {
                int value = getValue(E);
                and(A, value);
                break;
            }

            // AND H
            case 0xA4: {
                int value = getValue(H);
                and(A, value);
                break;
            }

            // AND L
            case 0xA5: {
                int value = getValue(L);
                and(A, value);
                break;
            }

            // AND (HL)
            case 0xA6: {
                int addr = getAddress(H, L);
                int value = getValue(addr);
                and(A, value);
                break;
            }

            // AND A
            case 0xA7: {
                int value = getValue(A);
                and(A, value);
                break;
            }

            // XOR B
            case 0xA8: {
                int value = getValue(B);
                xor(A, value);
                break;
            }

            // XOR C
            case 0xA9: {
                xor(A, getValue(C));
                break;
            }

            // XOR D
            case 0xAA: {
                xor(A, getValue(D));
                break;
            }

            // XOR E
            case 0xAB: {
                xor(A, getValue(E));
                break;
            }

            // XOR H
            case 0xAC: {
                xor(A, getValue(H));
                break;
            }

            // XOR L
            case 0xAD: {
                xor(A, getValue(L));
                break;
            }

            // XOR (HL)
            case 0xAE: {
                int addr = getAddress(H, L);
                xor(A, getValue(addr));
                break;
            }

            // XOR A
            case 0xAF: {
                xor(A, getValue(A));
                break;
            }

            // OR B
            case 0xB0: {
                or(A, B);
                break;
            }

            // OR C
            case 0xB1: {
                or(A, C);
                break;
            }

            // OR D
            case 0xB2: {
                or(A, D);
                break;
            }

            // OR E
            case 0xB3: {
                or(A, E);
                break;
            }

            // OR H
            case 0xB4: {
                or(A, H);
                break;
            }

            // OR L
            case 0xB5: {
                or(A, L);
                break;
            }

            // OR (HL)
            case 0xB6: {
                int addr = getAddress(H, L);
                orByteFromAddress(A, addr);
                break;
            }

            // OR A
            case 0xB7: {
                or(A, A);
                break;
            }

            // CP B
            case 0xB8: {
                cp(A, B);
                break;
            }

            // CP C
            case 0xB9: {
                cp(A, C);
                break;
            }

            // CP D
            case 0xBA: {
                cp(A, D);
                break;
            }

            // CP E
            case 0xBB: {
                cp(A, E);
                break;
            }

            // CP H
            case 0xBC: {
                cp(A, H);
                break;
            }

            // CP L
            case 0xBD: {
                cp(A, L);
                break;
            }

            // CP (HL)
            case 0xBE: {
                int addr = getAddress(H, L);
                cpByteFromAddress(A, addr);
                break;
            }

            // CP A
            case 0xBF: {
                cp(A, A);
                break;
            }

            // RET NZ
            case 0xC0: {
                if (!F.isSet(Z_BIT)) {
                    int value = popImpl();
                    PC.write(value);
                }
                break;
            }

            // POP BC
            case 0xC1: {
                pop(B, C);
                break;
            }

            // JP NZ,a16
            case 0xC2: {
                if (!F.isSet(Z_BIT)) {
                    PC.write(a16);
                }
                length = 3;
                break;
            }

            // JP a16
            case 0xC3: {
                PC.write(a16);
                length = 3;
                break;
            }

            // CALL NZ,a16
            case 0xC4: {
                if (!F.isSet(Z_BIT)) {
                    int value = PC.read() + 3;
                    pushImpl(value);
                    PC.write(a16);
                }
                length = 3;
                break;
            }

            // PUSH BC
            case 0xC5: {
                int value = getValue(B, C);
                push(value);
                break;
            }

            // ADD A,d8
            case 0xC6: {
                add(A, d8);
                break;
            }

            // REST 00H
            case 0xC7: {
                int value = PC.read() + 1;
                pushImpl(value);
                PC.write(0x0000);
                break;
            }

            // RET Z
            case 0xC8: {
                if (F.isSet(Z_BIT)) {
                    int value = popImpl();
                    PC.write(value);
                }
                break;
            }

            // RET
            case 0xC9: {
                int value = popImpl();
                PC.write(value);
                break;
            }

            // Z,a16
            case 0xCA: {
                if (F.isSet(Z_BIT)) {
                    PC.write(a16);
                }
                length = 3;
                break;
            }

            // PREFIX CB
            case 0xCB: {
                // TODO: there is something that is done here
                length = executeCB() + 1;
                break;
            }

            // CALL Z,a16
            case 0xCC: {
                if (F.isSet(Z_BIT)) {
                    int value = PC.read() + 3;
                    pushImpl(value);
                    PC.write(a16);
                }
                break;
            }

            // CALL a16
            case 0xCD: {
                int value = PC.read() + 3;
                pushImpl(value);
                PC.write(a16);
                break;
            }

            // ADC A,d8
            case 0xCE: {
                adc(A, d8);
                length = 2;
                break;
            }

            // RST 08H
            case 0xCF: {
                int value = PC.read() + 1;
                pushImpl(value);
                PC.write(0x0008);
                break;
            }

            // RET NC
            case 0xD0: {
                if (!F.isSet(N_BIT)) {
                    int value = popImpl();
                    PC.write(value);
                }
                break;
            }

            // POP DE
            case 0xD1: {
                pop(D, E);
                break;
            }

            // JP NC,a16
            case 0xD2: {
                if (!F.isSet(C_BIT)) {
                    PC.write(a16);
                }
                length = 3;
                break;
            }

            // BLANK
            case 0xD3: {
                length = 0;
                break;
            }

            // CALL NC,a16
            case 0xD4: {
                if (!F.isSet(C_BIT)) {
                    int value = PC.read() + 3;
                    pushImpl(value);
                    PC.write(a16);
                }
                length = 3;
                break;
            }

            // PUSH DE
            case 0xD5: {
                int value = getValue(D, E);
                push(value);
                break;
            }

            // SUB d8
            case 0xD6: {
                sub(A, d8);
                length = 2;
                break;
            }

            // RST 10H
            case 0xD7: {
                int value = PC.read() + 1;
                pushImpl(value);
                PC.write(0x0010);
                break;
            }

            // RET C
            case 0xD8: {
                if (F.isSet(C_BIT)) {
                    int value = popImpl();
                    PC.write(value);
                }
                break;
            }

            // RETI
            case 0xD9: {
                int value = popImpl();
                PC.write(value);
                // TODO: enable interrupts
                break;
            }

            // JP C,a16
            case 0xDA: {
                if (F.isSet(C_BIT)) {
                    PC.write(a16);
                }
                length = 3;
                break;
            }

            // BLANK
            case 0xDB: {
                length = 0;
                break;
            }

            // CALL C,a16
            case 0xDC: {
                if (F.isSet(C_BIT)) {
                    int value = PC.read() + 3;
                    pushImpl(value);
                    PC.write(a16);
                }
                length = 3;
                break;
            }

            // BLANK
            case 0xDD: {
                length = 0;
                break;
            }

            // SBC A,d8
            case 0xDE: {
                sbc(A, d8);
                length = 2;
                break;
            }

            // RST 18H
            case 0xDF: {
                int value = PC.read() + 1;
                pushImpl(value);
                PC.write(0x0018);
                break;
            }

            // LDH (a8),A
            case 0xE0: {
                int addr = 0xFF00 + a8;
                writeByte(A.read(), addr);
                length = 2;
                break;
            }

            // POP HL
            case 0xE1: {
                pop(H, L);
                break;
            }

            // LD (C),A
            case 0xE2: {
                int addr = 0xFF00 + C.read();
                writeByte(A.read(), addr);
                length = 2;
                break;
            }

            // BLANK
            case 0xE3: {
                length = 0;
                break;
            }

            // BLANK
            case 0xE4: {
                length = 0;
                break;
            }

            // PUSH HL
            case 0xE5: {
                int value = getValue(H, L);
                push(value);
                break;
            }

            // AND d8
            case 0xE6: {
                and(A, d8);
                length = 2;
                break;
            }

            // RST 20H
            case 0xE7: {
                int value = PC.read() + 1;
                pushImpl(value);
                PC.write(0x0020);
                break;
            }

            // ADD SP,r8
            case 0xE8: {

                break;
            }

            // JP (HL)
            case 0xE9: {

                break;
            }

            // LD (a16),A
            case 0xEA: {

                break;
            }

            // BLANK
            case 0xEB: {
                length = 0;
                break;
            }

            // BLANK
            case 0xEC: {
                length = 0;
                break;
            }

            // BLANK
            case 0xED: {
                length = 0;
                break;
            }

            // XOR d8
            case 0xEE: {

                break;
            }

            // RST 28H
            case 0xEF: {

                break;
            }

            // LDH A,(a8)
            case 0xF0: {

                break;
            }

            // POP AF
            case 0xF1: {

                break;
            }

            // LD A,(C)
            case 0xF2: {

                break;
            }

            // DI
            case 0xF3: {

                break;
            }

            // BLANK
            case 0xF4: {
                length = 0;
                break;
            }

            // PUSH AF
            case 0xF5: {

                break;
            }

            // OR d8
            case 0xF6: {

                break;
            }

            // RST 30H
            case 0xF7: {

                break;
            }

            // LD HL,SP+r8
            case 0xF8: {

                break;
            }

            // LD SP,HL
            case 0xF9: {

                break;
            }

            // LD A,(a16)
            case 0xFA: {

                break;
            }

            // EI
            case 0xFB: {

                break;
            }

            // BLANK
            case 0xFC: {
                length = 0;
                break;
            }

            // BLANK
            case 0xFD: {
                length = 0;
                break;
            }

            // CP d8
            case 0xFE: {

                break;
            }

            // RST 38H
            case 0xFF: {

                break;
            }

            default:
                String message = "Opcode not defined: %OPCODE%";
                String hex = String.format("%2s", Integer.toHexString(opcode));
                hex = hex.replace(" ", "0").toUpperCase();
                hex = "0x" + hex;
                throw new IllegalArgumentException(message.replace("%OPCODE%", hex));
        }
        return length;
    }

    private int executeCB() {
        return 2;
    }

    /**
     * Stores <code>value</code> into the memory location, <code>addr</code>.
     *
     * @param addr  the memory location to store <code>value</code>
     * @param value the value to store
     */
    private void ld(int addr, int value) {
        writeByte(value, addr);
    }

    /**
     * Stores <code>value</code> into the 8-bit register, <code>r</code>.
     *
     * @param r     the register to store <code>value</code>
     * @param value the value to store
     */
    private void ld(Register8Bit r, int value) {
        r.write(value);
    }

    /**
     * Stores <code>value</code> into the 16-bit register, <code>r</code>.
     *
     * @param r     the register to store <code>value</code>
     * @param value the value to store
     */
    private void ld(Register16Bit r, int value) {
        r.write(value);
    }

    /**
     * Stores <code>value</code> across the two 8-bit registers, <code>r1</code>
     * and <code>r2</code>.
     *
     * @param r1    the register to store the hi bytes from <code>value</code>
     * @param r2    the register to store the lo bytes from <code>value</code>
     * @param value the value to store
     */
    private void ld(Register8Bit r1, Register8Bit r2, int value) {
        r1.write(value >> 8);
        r2.write(value & 0xFF);
    }

    /**
     * Pops the value from the top of the stack and returns the value.
     *
     * @return the value popped from the top of the stack
     */
    private int popImpl() {
        try {
            int addr = getAddress(SP);
            int value = readWord(addr);
            return value;
        } finally {
            SP.add(2);
        }
    }

    /**
     * Pops the top value from the stack and stores it across the two 8-bit register,
     * <code>r1</code> and <code>r2</code>.
     *
     * @param r1 the register to store the hi bytes from the value
     * @param r2 the register to store the lo bytes from the value
     */
    private void pop(Register8Bit r1, Register8Bit r2) {
        int value = popImpl();
        r1.write(value >> 8);
        r2.write(value & 0xFF);
    }

    /**
     * Pushes <code>value</code> to the top of the stack
     *
     * @param value the value to push on the stack
     */
    private void pushImpl(int value) {
        SP.subtract(2);
        int addr = getAddress(SP);
        writeWord(value, addr);
    }

    /**
     * Pushes <code>value</code> on top of the stack
     *
     * @param value the value to push on the stack
     */
    private void push(int value) {
        pushImpl(value);
    }

    /**
     * Increments the value in memory stored at <code>addr</code>. If the current value equals
     * 0xFF then the value rolls over to 0.
     * </br>
     * <b>Flag Alteration:</b>
     * <ul>
     * <li><code>Z_BIT</code>: Set if the value before incrementing equals 0xFF</li>
     * <li><code>N_BIT</code>: Reset to 0</li>
     * <li><code>H_BIT</code>: Set if the value before incrementing equals 0xFF or 0x0F</li>
     * <li><code>C_BIT</code>: Unaffected</li>
     * </ul>
     *
     * @param addr the location in memory of the value to increment
     */
    private void inc(int addr) {
        int value = readByte(addr);
        F.set(N_BIT, 0);
        F.set(H_BIT, value == 0xFF || value == 0x0F);
        F.set(Z_BIT, value == 0xFF);
        value = (value + 1) & 0xFF;
        writeByte(value, addr);
    }

    /**
     * Increments the 8-Bit register <code>r</code>. If the current value equals
     * 0xFF then the value rolls over to 0.
     * </br>
     * <b>Flag Alteration:</b>
     * <ul>
     * <li><code>Z_BIT</code>: Set if <code>r</code>'s value before incrementing equals
     * the maximum value of an 8-Bit Register (0xFF)</li>
     * <li><code>N_BIT</code>: Reset to 0</li>
     * <li><code>H_BIT</code>: Set if <code>r</code>'s value before incrementing equals
     * 0xFF or 0x0F</li>
     * <li><code>C_BIT</code>: Unaffected</li>
     * </ul>
     *
     * @param r the register to increment
     */
    private void inc(Register8Bit r) {
        int value = r.read();
        F.set(N_BIT, 0);
        F.set(H_BIT, value == 0xFF || value == 0x0F);
        F.set(Z_BIT, value == 0xFF);
        r.inc();
    }

    /**
     * Increments the 16-Bit register <code>r</code>.
     * </br>
     * <b>Flag Alteration:</b>
     * <ul>
     * <li><code>Z_BIT</code>: Unaffected</li>
     * <li><code>N_BIT</code>: Unaffected</li>
     * <li><code>H_BIT</code>: Unaffected</li>
     * <li><code>C_BIT</code>: Unaffected</li>
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
     * <li><code>Z_BIT</code>: Unaffected</li>
     * <li><code>N_BIT</code>: Unaffected</li>
     * <li><code>H_BIT</code>: Unaffected</li>
     * <li><code>C_BIT</code>: Unaffected</li>
     * </ul>
     *
     * @param r1 Register that contains the hi bytes of the value to increment
     * @param r2 Register that contains the lo bytes of the value to increment
     */
    private void inc(Register8Bit r1, Register8Bit r2) {
        int value = (r1.read() << 8) + r2.read();
        value = (value + 1) & 0xFFFF;
        r1.write(value >> 8);
        r2.write(value & 0xFF);
    }

    private void dec(int addr) {
        int value = readByte(addr);
        F.set(N_BIT, 1);
        F.set(H_BIT, value == 0x00 || value == 0x10);
        F.set(Z_BIT, value == 0x01);
        value = (value - 1) & 0xFF;
        writeByte(value, addr);
    }

    private void dec(Register8Bit r) {
        int value = r.read();
        F.set(N_BIT, 1);
        F.set(H_BIT, value == 0x00 || value == 0x10);
        F.set(Z_BIT, value == 0x01);
        r.dec();
    }

    private void dec(Register16Bit r) {
        int value = r.read();
        if (value == 0) {
            value = 0xFFFF;
        } else {
            value--;
        }
        r.write(value);
    }

    private void dec(Register8Bit r1, Register8Bit r2) {
        int value = (r1.read() << 8) + r2.read();
        if (value == 0) {
            value = 0xFFFF;
        } else {
            value--;
        }
        r1.write(value >> 8);
        r2.write(value & 0xFF);
    }

    private void add(Register8Bit r, int value) {
        int value1 = r.read();
        int value2 = value;
        int sum = value1 + value2;
        r.write(sum);

        F.set(Z_BIT, sum == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, ((value1 & 0x0F) + (value2 & 0x0F)) > 0xF);
        F.set(C_BIT, sum > 0xFF);
    }

    private void add(Register16Bit r, int value) {
        int value1 = r.read();
        int value2 = value;
        int sum = value1 + value2;
        r.write(sum);
    }

    private void add(Register8Bit r1, Register8Bit r2, int value) {
        int value1 = (r1.read() << 8) + r2.read();
        int value2 = value;
        int sum = value1 + value2;
        r1.write(sum >> 8);
        r2.write(sum & 0xFF);
    }

    private void adc(Register8Bit r, int value) {
        int carryBit = F.get(C_BIT);
        int value1 = r.read();
        int value2 = value + carryBit;
        int sum = value1 + value2;
        r.write(sum);

        F.set(Z_BIT, sum == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, ((value1 & 0x0F) + (value2 & 0x0F)) > 0xF);
        F.set(C_BIT, sum > 0xFF);
    }

    private void sub(Register8Bit r, int value) {
        int value1 = r.read();
        int value2 = value;
        int difference = value1 - value2;
        r.write(difference);

        F.set(Z_BIT, difference == 0x00);
        F.set(N_BIT, 1);
        F.set(H_BIT, (difference & 0xF) > (value1 & 0xF));
        F.set(C_BIT, difference < 0);
    }

    private void sub(Register16Bit r, int value) {
        int value1 = r.read();
        int value2 = value;
        int difference = value1 - value2;
        r.write(difference);
    }

    private void sub(Register8Bit r1, Register8Bit r2, int value) {
        int value1 = (r1.read() << 8) + r2.read();
        int value2 = value;
        int difference = value1 - value2;
        r1.write(difference >> 8);
        r2.write(difference & 0xFF);
    }

    private void sbc(Register8Bit r, int value) {
        int carryBit = F.get(C_BIT);
        int value1 = r.read();
        int value2 = value + carryBit;
        int difference = value1 - value2;

        F.set(Z_BIT, difference == 0x00);
        F.set(N_BIT, 1);
        F.set(H_BIT, (difference & 0xF) > (value1 & 0xF));
        F.set(C_BIT, difference < 0);
    }

    private void and(Register8Bit r, int value) {
        int value1 = r.read();
        int value2 = value;
        int result = value1 & value2;
        r.write(result);

        F.set(Z_BIT, result == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 1);
        F.set(C_BIT, 0);
    }

    private void xor(Register8Bit r, int value) {
        int value1 = r.read();
        int value2 = value;
        int result = value1 ^ value2;
        r.write(result);

        F.set(Z_BIT, result == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, 0);
    }


    /**
     * Reads and returns the value found at the memory location, <code>addr</code>.
     *
     * @param addr the memory location of the desired value
     * @return the value in memory located at address <code>addr</code>
     */
    private int getValue(int addr) {
        return readByte(addr);
    }

    /**
     * Reads and returns the value from the register.
     *
     * @param r the 8-bit register to read the value from
     * @return the value stored in the 8-bit register, <code>r</code>
     */
    private int getValue(Register8Bit r) {
        return r.read();
    }

    /**
     * Reads and returns the value from the register.
     *
     * @param r the 16-bit register to read the value from
     * @return the value stored in the 16-bit register, <code>r</code>
     */
    private int getValue(Register16Bit r) {
        return r.read();
    }

    /**
     * Reads and returns the value stored across the two 8-bit registers,
     * <code>r1</code> and <code>r2</code>.
     *
     * @param r1 the 8-bit register that stores the hi bytes
     * @param r2 the 8-bit register that stores the lo bytes
     * @return the value stored across <code>r1</code> and <code>r2</code>
     */
    private int getValue(Register8Bit r1, Register8Bit r2) {
        return (r1.read() << 8) + r2.read();
    }

    private int orImpl(int value1, int value2) {
        int result = value1 | value2;
        F.set(Z_BIT, result == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, 0);
        return result;
    }

    private void or(Register8Bit r1, Register8Bit r2) {
        int result = orImpl(r1.read(), r2.read());
        r1.write(result);
    }

    private void orByteFromAddress(Register8Bit r, int addr) {
        int result = orImpl(r.read(), readByte(addr));
        r.write(result);
    }

    private void cpImpl(int value1, int value2) {
        int difference = value1 - value2;
        F.set(Z_BIT, difference == 0x00);
        F.set(N_BIT, 1);
        F.set(H_BIT, (difference & 0xF) > (value1 & 0xF));
        F.set(C_BIT, difference < 0);
    }

    private void cp(Register8Bit r1, Register8Bit r2) {
        cpImpl(r1.read(), r2.read());
    }

    private void cpByteFromAddress(Register8Bit r, int addr) {
        cpImpl(r.read(), readByte(addr));
    }

    private int getAddress(Register16Bit r) {
        return r.read();
    }

    private int getAddress(Register8Bit r1, Register8Bit r2) {
        return (r1.read() << 8) + r2.read();
    }
}
