package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Cpu;
import com.meadowsapps.jgameboy.core.Register16Bit;
import com.meadowsapps.jgameboy.core.Register8Bit;

/**
 * Emulated CPU found inside of the Nintendo GameBoy.
 * Custom 8-bit Sharp LR35902 based on the Intel 8080
 * and the Z80 microprocessors.
 */
public class GbcCpu extends AbstractGbcCoreElement implements Cpu {

    private boolean halted;

    private boolean interruptsEnabled;

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
     * Internal clock
     */
    private GbcCpuClock clock;

    /**
     * Opcode Table containing information about each opcode
     */
    private GbcOpcodeTable table;

    /**
     * Bit index of the Zero Status Flag
     */
    private static final int Z_BIT = 7;

    /**
     * Bit index of the Subtract Status Flag
     */
    private static final int N_BIT = 6;

    /**
     * Bit index of the Half Carry Status Flag
     */
    private static final int H_BIT = 5;

    /**
     * Bit index of the Carry Status Flag
     */
    private static final int C_BIT = 4;

    /**
     * Initializes the CPU's registers
     */
    GbcCpu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
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
        clock = new GbcCpuClock();
        table = new GbcOpcodeTable();
    }

    @Override
    public void reset() {
        halted = false;
        interruptsEnabled = false;

        A.write(0);
        F.write(0xB0);
        int b = 0x0013 >> 8;
        B.write(b);
        int c = 0x0013 & 0xFF;
        C.write(c);
        int d = 0x00D8 >> 8;
        D.write(d);
        int e = 0x00D8 & 0xFF;
        E.write(e);
        int h = 0x014D >> 8;
        H.write(h);
        int l = 0x014D & 0xFF;
        L.write(l);
        SP.write(0xFFFE);
        PC.write(0x100);

        clock.m(0);
        clock.t(0);
    }

    @Override
    public void step() {
        // clock.reset()
        if (!halted) {
            // check for interrupt
            if (interruptsEnabled) {
                int ieFlag = mmu().readByte(INTERRUPT_ENABLED_FLAG);
                int iFlag = mmu().readByte(INTERRUPT_FLAG);
                int interrupt = iFlag & ieFlag;
                if (interrupt != 0x00) {
                    switch (interrupt) {
                        case V_BLANK_IRQ:
                            mmu().writeByte(INTERRUPT_FLAG, iFlag & 0xFE);
                            pushImpl(PC.read());
                            PC.write(V_BLANK_IR);
                            break;
                        case LCD_IRQ:
                            mmu().writeByte(INTERRUPT_FLAG, iFlag & 0xFD);
                            pushImpl(PC.read());
                            PC.write(LCD_IR);
                            break;
                        case TIMER_OVERFLOW_IRQ:
                            mmu().writeByte(INTERRUPT_FLAG, iFlag & 0xFB);
                            pushImpl(PC.read());
                            PC.write(TIMER_OVERFLOW_IR);
                            break;
                        case JOYPAD_HILO_IRQ:
                            mmu().writeByte(INTERRUPT_FLAG, iFlag & 0xEF);
                            pushImpl(PC.read());
                            PC.write(JOYPAD_HILO_IR);
                            break;
                    }
                    interruptsEnabled = false;
                }
            }

            // fetch
            int opcode = mmu().readByte(PC.read());
            // decode and execute
            execute(opcode);
        } else {
            clock.m(1);
        }
    }

    /**
     * Executes the given opcode with the available operands and returns
     * the length of the instruction to add to the <code>PC</code> register.
     *
     * @param opcode opcode to execute
     * @return the length of the instruction
     */
    @Override
    public void execute(int opcode) {
        Opcode op = table.lookup(opcode);
        int operand1 = mmu().readByte(PC.read() + 1);
        int operand2 = mmu().readByte(PC.read() + 2);

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
                break;
            }

            // LD (BC),A
            case 0x02: {
                int addr = getAddress(B, C);
                ld(addr, A.read());
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
                break;
            }

            // RLCA
            case 0x07: {
                int bit7 = A.get(7);
                A.shift(LEFT, 1);
                A.set(0, bit7);

                F.set(Z_BIT, 0);
                F.set(H_BIT, 0);
                F.set(N_BIT, 0);
                F.set(C_BIT, bit7);
                break;
            }

            // LD (a16),SP
            case 0x08: {
                ld(a16, SP.read());
                break;
            }

            // ADD HL,BC
            case 0x09: {
                int value = (B.read() << 8) + C.read();
                add(H, L, value);
                break;
            }

            // LD A,(BC)
            case 0x0A: {
                int addr = getAddress(B, C);
                ld(A, mmu().readByte(addr));
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
                break;
            }

            // RRCA
            case 0x0F: {
                int bit0 = A.get(0);
                A.shift(RIGHT, 1);
                A.set(7, bit0);

                F.set(Z_BIT, 0);
                F.set(H_BIT, 0);
                F.set(N_BIT, 0);
                F.set(C_BIT, bit0);
                break;
            }

            // STOP 0
            case 0x10: {
                // TODO: there something that is done here
                break;
            }

            // LD DE,d16
            case 0x11: {
                ld(D, E, d16);
                break;
            }

            // LD (DE),A
            case 0x12: {
                int addr = getAddress(D, E);
                ld(addr, A.read());
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
                break;
            }

            // RLA
            case 0x17: {
                int bit7 = A.get(7);
                A.shift(LEFT, 1);
                int bitC = F.get(C_BIT);
                A.set(0, bitC);

                F.set(Z_BIT, 0);
                F.set(N_BIT, 0);
                F.set(H_BIT, 0);
                F.set(C_BIT, bit7);
                break;
            }

            // JR r8
            case 0x18: {
                PC.add(r8);
                break;
            }

            // ADD HL,DE
            case 0x19: {
                int value = (D.read() << 8) + E.read();
                add(H, L, value);
                break;
            }

            // LD A,(DE)
            case 0x1A: {
                int addr = getAddress(D, E);
                ld(A, mmu().readByte(addr));
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
                break;
            }

            // LD HL,d16
            case 0x21: {
                ld(H, L, d16);
                break;
            }

            // LD (HL+),A
            case 0x22: {
                int addr = getAddress(H, L);
                ld(addr, A.read());
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
                    correctionFactor = (correctionFactor & 0xF0);
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
                break;
            }

            // ADD HL,HL
            case 0x29: {
                int value = (H.read() << 8) + L.read();
                add(H, L, value);
                break;
            }

            // LD A,(HL+)
            case 0x2A: {
                int addr = getAddress(H, L);
                ld(A, mmu().readByte(addr));
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
                break;
            }

            // LD SP,d16
            case 0x31: {
                ld(SP, d16);
                break;
            }

            // LD (HL-),A
            case 0x32: {
                int addr = getAddress(H, L);
                ld(addr, A.read());
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
                break;
            }

            // ADD HL,SP
            case 0x39: {
                add(H, L, SP.read());
                break;
            }

            // LD A,(HL-)
            case 0x3A: {
                int addr = getAddress(H, L);
                ld(A, mmu().readByte(addr));
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
                ld(B, B.read());
                break;
            }

            // LD B,C
            case 0x41: {
                ld(B, C.read());
                break;
            }

            // LD B,D
            case 0x42: {
                ld(B, D.read());
                break;
            }

            // LD B,E
            case 0x43: {
                ld(B, E.read());
                break;
            }

            // LD B,H
            case 0x44: {
                ld(B, H.read());
                break;
            }

            // LD B,L
            case 0x45: {
                ld(B, L.read());
                break;
            }

            // LD B,(HL)
            case 0x46: {
                int addr = getAddress(H, L);
                ld(B, mmu().readByte(addr));
                break;
            }

            // LD B,A
            case 0x47: {
                ld(B, A.read());
                break;
            }

            // LD C,B
            case 0x48: {
                ld(C, B.read());
                break;
            }

            // LD C,C
            case 0x49: {
                ld(C, C.read());
                break;
            }

            // LD C,D
            case 0x4A: {
                ld(C, D.read());
                break;
            }

            // LD C,E
            case 0x4B: {
                ld(C, E.read());
                break;
            }

            // LD C,H
            case 0x4C: {
                ld(C, H.read());
                break;
            }

            // LD C,L
            case 0x4D: {
                ld(C, L.read());
                break;
            }

            // LD C,(HL)
            case 0x4E: {
                int addr = getAddress(H, L);
                ld(C, mmu().readByte(addr));
                break;
            }

            // LD C,A
            case 0x4F: {
                ld(C, A.read());
                break;
            }

            // LD D,B
            case 0x50: {
                ld(D, B.read());
                break;
            }

            // LD D,C
            case 0x51: {
                ld(D, C.read());
                break;
            }

            // LD D,D
            case 0x52: {
                ld(D, D.read());
                break;
            }

            // LD D,E
            case 0x53: {
                ld(D, E.read());
                break;
            }

            // LD D,H
            case 0x54: {
                ld(D, H.read());
                break;
            }

            // LD D,L
            case 0x55: {
                ld(D, L.read());
                break;
            }

            // LD D,(HL)
            case 0x56: {
                int addr = getAddress(H, L);
                ld(D, mmu().readByte(addr));
                break;
            }

            // LD D,A
            case 0x57: {
                ld(D, A.read());
                break;
            }

            // LD E,B
            case 0x58: {
                ld(E, B.read());
                break;
            }

            // LD E,C
            case 0x59: {
                ld(E, C.read());
                break;
            }

            // LD E,D
            case 0x5A: {
                ld(E, D.read());
                break;
            }

            // LD E,E
            case 0x5B: {
                int value = E.read();
                ld(E, value);
                break;
            }

            // LD E,H
            case 0x5C: {
                int value = H.read();
                ld(E, value);
                break;
            }

            // LD E,L
            case 0x5D: {
                int value = L.read();
                ld(E, value);
                break;
            }

            // LD E,(HL)
            case 0x5E: {
                int addr = getAddress(H, L);
                int value = mmu().readByte(addr);
                ld(E, value);
                break;
            }

            // LD E,A
            case 0x5F: {
                int value = A.read();
                ld(E, value);
                break;
            }

            // LD H,B
            case 0x60: {
                int value = B.read();
                ld(H, value);
                break;
            }

            // LD H,C
            case 0x61: {
                int value = C.read();
                ld(H, value);
                break;
            }

            // LD H,D
            case 0x62: {
                int value = D.read();
                ld(H, value);
                break;
            }

            // LD H,E
            case 0x63: {
                int value = E.read();
                ld(H, value);
                break;
            }

            // LD H,H
            case 0x64: {
                int value = H.read();
                ld(H, value);
                break;
            }

            // LD H,L
            case 0x65: {
                int value = L.read();
                ld(H, value);
                break;
            }

            // LD H,(HL)
            case 0x66: {
                int addr = getAddress(H, L);
                int value = mmu().readByte(addr);
                ld(H, value);
                break;
            }

            // LD H,A
            case 0x67: {
                int value = A.read();
                ld(H, value);
                break;
            }

            // LD L,B
            case 0x68: {
                int value = B.read();
                ld(L, value);
                break;
            }

            // LD L,C
            case 0x69: {
                int value = C.read();
                ld(L, value);
                break;
            }

            // LD L,D
            case 0x6A: {
                int value = D.read();
                ld(L, value);
                break;
            }

            // LD L,E
            case 0x6B: {
                int value = E.read();
                ld(L, value);
                break;
            }

            // LD L,H
            case 0x6C: {
                int value = H.read();
                ld(L, value);
                break;
            }

            // LD L,L
            case 0x6D: {
                int value = L.read();
                ld(L, value);
                break;
            }

            // LD L,(HL)
            case 0x6E: {
                int addr = getAddress(H, L);
                int value = mmu().readByte(addr);
                ld(L, value);
                break;
            }

            // LD L,A
            case 0x6F: {
                int value = A.read();
                ld(L, value);
                break;
            }

            // LD (HL),B
            case 0x70: {
                int addr = getAddress(H, L);
                int value = B.read();
                ld(addr, value);
                break;
            }

            // LD (HL),C
            case 0x71: {
                int addr = getAddress(H, L);
                int value = C.read();
                ld(addr, value);
                break;
            }

            // LD (HL),D
            case 0x72: {
                int addr = getAddress(H, L);
                int value = D.read();
                ld(addr, value);
                break;
            }

            // LD (HL),E
            case 0x73: {
                int addr = getAddress(H, L);
                int value = E.read();
                ld(addr, value);
                break;
            }

            // LD (HL),H
            case 0x74: {
                int addr = getAddress(H, L);
                int value = H.read();
                ld(addr, value);
                break;
            }

            // LD (HL),L
            case 0x75: {
                int addr = getAddress(H, L);
                int value = L.read();
                ld(addr, value);
                break;
            }

            // HALT
            case 0x76: {
                halted = true;
                break;
            }

            // LD (HL),A
            case 0x77: {
                int addr = getAddress(H, L);
                int value = A.read();
                ld(addr, value);
                break;
            }

            // LD A,B
            case 0x78: {
                int value = B.read();
                ld(A, value);
                break;
            }

            // LD A,C
            case 0x79: {
                int value = C.read();
                ld(A, value);
                break;
            }

            // LD A,D
            case 0x7A: {
                int value = D.read();
                ld(A, value);
                break;
            }

            // LD A,E
            case 0x7B: {
                int value = E.read();
                ld(A, value);
                break;
            }

            // LD A,H
            case 0x7C: {
                int value = H.read();
                ld(A, value);
                break;
            }

            // LD A,L
            case 0x7D: {
                int value = L.read();
                ld(A, value);
                break;
            }

            // LD A,(HL)
            case 0x7E: {
                int addr = getAddress(H, L);
                int value = mmu().readByte(addr);
                ld(A, value);
                break;
            }

            // LD A,A
            case 0x7F: {
                int value = A.read();
                ld(A, value);
                break;
            }

            // ADD A,B
            case 0x80: {
                int value = B.read();
                add(A, value);
                break;
            }

            // ADD A,C
            case 0x81: {
                int value = C.read();
                add(A, value);
                break;
            }

            // ADD A,D
            case 0x82: {
                int value = D.read();
                add(A, value);
                break;
            }

            // ADD A,E
            case 0x83: {
                int value = E.read();
                add(A, value);
                break;
            }

            // ADD A,H
            case 0x84: {
                int value = H.read();
                add(A, value);
                break;
            }

            // ADD A,L
            case 0x85: {
                int value = L.read();
                add(A, value);
                break;
            }

            // ADD A,(HL)
            case 0x86: {
                int addr = getAddress(H, L);
                int value = mmu().readByte(addr);
                add(A, value);
                break;
            }

            // ADD A,A
            case 0x87: {
                int value = A.read();
                add(A, value);
                break;
            }

            // ADC A,B
            case 0x88: {
                int value = B.read();
                adc(A, value);
                break;
            }

            // ADC A,C
            case 0x89: {
                int value = C.read();
                adc(A, value);
                break;
            }

            // ADC A,D
            case 0x8A: {
                int value = D.read();
                adc(A, value);
                break;
            }

            // ADC A,E
            case 0x8B: {
                int value = E.read();
                adc(A, value);
                break;
            }

            // ADC A,H
            case 0x8C: {
                int value = H.read();
                adc(A, value);
                break;
            }

            // ADC A,L
            case 0x8D: {
                int value = L.read();
                adc(A, value);
                break;
            }

            // ADC A,(HL)
            case 0x8E: {
                int addr = getAddress(H, L);
                int value = mmu().readByte(addr);
                adc(A, value);
                break;
            }

            // ADC A,A
            case 0x8F: {
                int value = A.read();
                adc(A, value);
                break;
            }

            // SUB B
            case 0x90: {
                int value = B.read();
                sub(A, value);
                break;
            }

            // SUB C
            case 0x91: {
                int value = C.read();
                sub(A, value);
                break;
            }

            // SUB D
            case 0x92: {
                int value = D.read();
                sub(A, value);
                break;
            }

            // SUB E
            case 0x93: {
                int value = E.read();
                sub(A, value);
                break;
            }

            // SUB H
            case 0x94: {
                int value = H.read();
                sub(A, value);
                break;
            }

            // SUB L
            case 0x95: {
                int value = L.read();
                sub(A, value);
                break;
            }

            // SUB (HL)
            case 0x96: {
                int addr = getAddress(H, L);
                int value = mmu().readByte(addr);
                sub(A, value);
                break;
            }

            // SUB A
            case 0x97: {
                int value = A.read();
                sub(A, value);
                break;
            }

            // SBC A,B
            case 0x98: {
                int value = B.read();
                sbc(A, value);
                break;
            }

            // SBC A,C
            case 0x99: {
                int value = C.read();
                sbc(A, value);
                break;
            }

            // SBC A,D
            case 0x9A: {
                int value = D.read();
                sbc(A, value);
                break;
            }

            // SBC A,E
            case 0x9B: {
                int value = E.read();
                sbc(A, value);
                break;
            }

            // SBC A,H
            case 0x9C: {
                int value = H.read();
                sbc(A, value);
                break;
            }

            // SBC A,L
            case 0x9D: {
                int value = L.read();
                sbc(A, value);
                break;
            }

            // SBC A,(HL)
            case 0x9E: {
                int addr = getAddress(H, L);
                int value = mmu().readByte(addr);
                sbc(A, value);
                break;
            }

            // SBC A,A
            case 0x9F: {
                int value = A.read();
                sbc(A, value);
                break;
            }

            // AND B
            case 0xA0: {
                int value = B.read();
                and(A, value);
                break;
            }

            // AND C
            case 0xA1: {
                int value = C.read();
                and(A, value);
                break;
            }

            // AND D
            case 0xA2: {
                int value = D.read();
                and(A, value);
                break;
            }

            // AND E
            case 0xA3: {
                int value = E.read();
                and(A, value);
                break;
            }

            // AND H
            case 0xA4: {
                int value = H.read();
                and(A, value);
                break;
            }

            // AND L
            case 0xA5: {
                int value = L.read();
                and(A, value);
                break;
            }

            // AND (HL)
            case 0xA6: {
                int addr = getAddress(H, L);
                int value = mmu().readByte(addr);
                and(A, value);
                break;
            }

            // AND A
            case 0xA7: {
                int value = A.read();
                and(A, value);
                break;
            }

            // XOR B
            case 0xA8: {
                int value = B.read();
                xor(A, value);
                break;
            }

            // XOR C
            case 0xA9: {
                xor(A, C.read());
                break;
            }

            // XOR D
            case 0xAA: {
                xor(A, D.read());
                break;
            }

            // XOR E
            case 0xAB: {
                xor(A, E.read());
                break;
            }

            // XOR H
            case 0xAC: {
                xor(A, H.read());
                break;
            }

            // XOR L
            case 0xAD: {
                xor(A, L.read());
                break;
            }

            // XOR (HL)
            case 0xAE: {
                int addr = getAddress(H, L);
                xor(A, mmu().readByte(addr));
                break;
            }

            // XOR A
            case 0xAF: {
                xor(A, A.read());
                break;
            }

            // OR B
            case 0xB0: {
                or(A, B.read());
                break;
            }

            // OR C
            case 0xB1: {
                or(A, C.read());
                break;
            }

            // OR D
            case 0xB2: {
                or(A, D.read());
                break;
            }

            // OR E
            case 0xB3: {
                or(A, E.read());
                break;
            }

            // OR H
            case 0xB4: {
                or(A, H.read());
                break;
            }

            // OR L
            case 0xB5: {
                or(A, L.read());
                break;
            }

            // OR (HL)
            case 0xB6: {
                int addr = getAddress(H, L);
                or(A, mmu().readByte(addr));
                break;
            }

            // OR A
            case 0xB7: {
                or(A, A.read());
                break;
            }

            // CP B
            case 0xB8: {
                cp(A, B.read());
                break;
            }

            // CP C
            case 0xB9: {
                cp(A, C.read());
                break;
            }

            // CP D
            case 0xBA: {
                cp(A, D.read());
                break;
            }

            // CP E
            case 0xBB: {
                cp(A, E.read());
                break;
            }

            // CP H
            case 0xBC: {
                cp(A, H.read());
                break;
            }

            // CP L
            case 0xBD: {
                cp(A, L.read());
                break;
            }

            // CP (HL)
            case 0xBE: {
                int addr = getAddress(H, L);
                cp(A, mmu().readByte(addr));
                break;
            }

            // CP A
            case 0xBF: {
                cp(A, A.read());
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
                break;
            }

            // JP a16
            case 0xC3: {
                PC.write(a16);
                break;
            }

            // CALL NZ,a16
            case 0xC4: {
                if (!F.isSet(Z_BIT)) {
                    int value = PC.read() + 3;
                    pushImpl(value);
                    PC.write(a16);
                }

                break;
            }

            // PUSH BC
            case 0xC5: {
                int value = (B.read() << 8) + C.read();
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
                break;
            }

            // PREFIX CB
            case 0xCB: {
                op = table.lookupCB(operand1);
                executeCB(operand1);
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
                break;
            }

            // BLANK
            case 0xD3: {
                break;
            }

            // CALL NC,a16
            case 0xD4: {
                if (!F.isSet(C_BIT)) {
                    int value = PC.read() + 3;
                    pushImpl(value);
                    PC.write(a16);
                }
                break;
            }

            // PUSH DE
            case 0xD5: {
                int value = (D.read() << 8) + E.read();
                push(value);
                break;
            }

            // SUB d8
            case 0xD6: {
                sub(A, d8);
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
                break;
            }

            // BLANK
            case 0xDB: {
                break;
            }

            // CALL C,a16
            case 0xDC: {
                if (F.isSet(C_BIT)) {
                    int value = PC.read() + 3;
                    pushImpl(value);
                    PC.write(a16);
                }
                break;
            }

            // BLANK
            case 0xDD: {
                break;
            }

            // SBC A,d8
            case 0xDE: {
                sbc(A, d8);
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
                ld(addr, A.read());
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
                mmu().writeByte(A.read(), addr);
                break;
            }

            // BLANK
            case 0xE3: {
                break;
            }

            // BLANK
            case 0xE4: {
                break;
            }

            // PUSH HL
            case 0xE5: {
                int value = (H.read() << 8) + L.read();
                push(value);
                break;
            }

            // AND d8
            case 0xE6: {
                and(A, d8);
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
                add(SP, r8);
                F.set(Z_BIT, 0);
                break;
            }

            // JP (HL)
            case 0xE9: {
                int addr = getAddress(H, L);
                PC.write(addr);
                break;
            }

            // LD (a16),A
            case 0xEA: {
                ld(a16, A.read());
                break;
            }

            // BLANK
            case 0xEB: {
                break;
            }

            // BLANK
            case 0xEC: {
                break;
            }

            // BLANK
            case 0xED: {
                break;
            }

            // XOR d8
            case 0xEE: {
                xor(A, d8);
                break;
            }

            // RST 28H
            case 0xEF: {
                int value = PC.read() + 1;
                pushImpl(value);
                PC.write(0x0028);
                break;
            }

            // LDH A,(a8)
            case 0xF0: {
                int addr = 0xFF00 + a8;
                ld(A, mmu().readByte(addr));
                break;
            }

            // POP AF
            case 0xF1: {
                pop(A, F);
                break;
            }

            // LD A,(C)
            case 0xF2: {
                int addr = 0xFF00 + C.read();
                ld(A, mmu().readByte(addr));
                break;
            }

            // DI
            case 0xF3: {
                interruptsEnabled = false;
                break;
            }

            // BLANK
            case 0xF4: {
                break;
            }

            // PUSH AF
            case 0xF5: {
                int value = (A.read() << 8) + F.read();
                push(value);
                break;
            }

            // OR d8
            case 0xF6: {
                or(A, d8);
                break;
            }

            // RST 30H
            case 0xF7: {
                int value = PC.read() + 1;
                pushImpl(value);
                PC.write(0x0030);
                break;
            }

            // LD HL,SP+r8
            case 0xF8: {
                break;
            }

            // LD SP,HL
            case 0xF9: {
                int value = (H.read() << 8) + L.read();
                ld(SP, value);
                break;
            }

            // LD A,(a16)
            case 0xFA: {
                ld(A, mmu().readByte(a16));
                break;
            }

            // EI
            case 0xFB: {
                interruptsEnabled = true;
                break;
            }

            // BLANK
            case 0xFC: {
                break;
            }

            // BLANK
            case 0xFD: {
                break;
            }

            // CP d8
            case 0xFE: {
                cp(A, d8);
                break;
            }

            // RST 38H
            case 0xFF: {
                int value = PC.read() + 1;
                pushImpl(value);
                PC.write(0x0038);
                break;
            }

            default:
                String message = "Opcode not defined: %OPCODE%";
                String hex = String.format("%2s", Integer.toHexString(opcode));
                hex = hex.replace(" ", "0").toUpperCase();
                hex = "0x" + hex;
                throw new IllegalArgumentException(message.replace("%OPCODE%", hex));
        }

        System.out.println(op);
        PC.add(op.getLength());
        clock.m(op.getTiming());
    }

    /**
     * Executes the prefix CB operation and returns the returns the length
     * of the instructions to add to the <code>PC</code> register.
     *
     * @return the length of the instruction
     */
    private void executeCB(int operation) {
        switch (operation) {
            // RLC B
            case 0x00: {
                rlc(B);
                break;
            }

            // RLC C
            case 0x01: {
                rlc(C);
                break;
            }

            // RLC D
            case 0x02: {
                rlc(D);
                break;
            }

            // RLC E
            case 0x03: {
                rlc(E);
                break;
            }

            // RLC H
            case 0x04: {
                rlc(H);
                break;
            }

            // RLC L
            case 0x05: {
                rlc(L);
                break;
            }

            // RLC (HL)
            case 0x06: {
                int addr = getAddress(H, L);
                rlc(addr);
                break;
            }

            // RLC A
            case 0x07: {
                rlc(A);
                break;
            }

            // RRC B
            case 0x08: {
                rrc(B);
                break;
            }

            // RRC C
            case 0x09: {
                rrc(C);
                break;
            }

            // RRC D
            case 0x0A: {
                rrc(D);
                break;
            }

            // RRC E
            case 0x0B: {
                rrc(E);
                break;
            }

            // RRC H
            case 0x0C: {
                rrc(H);
                break;
            }

            // RRC L
            case 0x0D: {
                rrc(L);
                break;
            }

            // RRC (HL)
            case 0x0E: {
                int addr = getAddress(H, L);
                rrc(addr);
                break;
            }

            // RRC A
            case 0x0F: {
                rrc(A);
                break;
            }

            // RL B
            case 0x10: {
                rl(B);
                break;
            }

            // RL C
            case 0x11: {
                rl(C);
                break;
            }

            // RL D
            case 0x12: {
                rl(D);
                break;
            }

            // RL E
            case 0x13: {
                rl(E);
                break;
            }

            // RL H
            case 0x14: {
                rl(H);
                break;
            }

            // RL L
            case 0x15: {
                rl(L);
                break;
            }

            // RL (HL)
            case 0x16: {
                int addr = getAddress(H, L);
                rl(addr);
                break;
            }

            // RL A
            case 0x17: {
                rl(A);
                break;
            }

            // RR B
            case 0x18: {
                rr(B);
                break;
            }

            // RR C
            case 0x19: {
                rr(C);
                break;
            }

            // RR D
            case 0x1A: {
                rr(D);
                break;
            }

            // RR E
            case 0x1B: {
                rr(E);
                break;
            }

            // RR H
            case 0x1C: {
                rr(H);
                break;
            }

            // RR L
            case 0x1D: {
                rr(L);
                break;
            }

            // RR (HL)
            case 0x1E: {
                int addr = getAddress(H, L);
                rr(addr);
                break;
            }

            // RR A
            case 0x1F: {
                rr(A);
                break;
            }

            // SLA B
            case 0x20: {
                sla(B);
                break;
            }

            // SLA C
            case 0x21: {
                sla(C);
                break;
            }

            // SLA D
            case 0x22: {
                sla(D);
                break;
            }

            // SLA E
            case 0x23: {
                sla(E);
                break;
            }

            // SLA H
            case 0x24: {
                sla(H);
                break;
            }

            // SLA L
            case 0x25: {
                sla(H);
                break;
            }

            // SLA (HL)
            case 0x26: {
                int addr = getAddress(H, L);
                sla(addr);
                break;
            }

            // SLA A
            case 0x27: {
                sla(A);
                break;
            }

            // SRA B
            case 0x28: {
                sra(B);
                break;
            }

            // SRA C
            case 0x29: {
                sra(C);
                break;
            }

            // SRA D
            case 0x2A: {
                sra(D);
                break;
            }

            // SRA E
            case 0x2B: {
                sra(E);
                break;
            }

            // SRA H
            case 0x2C: {
                sra(H);
                break;
            }

            // SRA L
            case 0x2D: {
                sra(L);
                break;
            }

            // SRA (HL)
            case 0x2E: {
                int addr = getAddress(H, L);
                sra(addr);
                break;
            }

            // SRA A
            case 0x2F: {
                sra(A);
                break;
            }

            // SWAP B
            case 0x30: {
                swap(B);
                break;
            }

            // SWAP C
            case 0x31: {
                swap(C);
                break;
            }

            // SWAP D
            case 0x32: {
                swap(D);
                break;
            }

            // SWAP E
            case 0x33: {
                swap(E);
                break;
            }

            // SWAP H
            case 0x34: {
                swap(H);
                break;
            }

            // SWAP L
            case 0x35: {
                swap(L);
                break;
            }

            // SWAP (HL)
            case 0x36: {
                int addr = getAddress(H, L);
                swap(addr);
                break;
            }

            // SWAP A
            case 0x37: {
                swap(A);
                break;
            }

            // SRL B
            case 0x38: {
                srl(B);
                break;
            }

            // SRL C
            case 0x39: {
                srl(C);
                break;
            }

            // SRL D
            case 0x3A: {
                srl(D);
                break;
            }

            // SRL E
            case 0x3B: {
                srl(E);
                break;
            }

            // SRL H
            case 0x3C: {
                srl(H);
                break;
            }

            // SRL L
            case 0x3D: {
                srl(L);
                break;
            }

            // SRL (HL)
            case 0x3E: {
                int addr = getAddress(H, L);
                srl(addr);
                break;
            }

            // SRL A
            case 0x3F: {
                srl(A);
                break;
            }

            // BIT 0,B
            case 0x40: {
                bit(0, B);
                break;
            }

            // BIT 0,C
            case 0x41: {
                bit(0, C);
                break;
            }

            // BIT 0,D
            case 0x42: {
                bit(0, D);
                break;
            }

            // BIT 0,E
            case 0x43: {
                bit(0, E);
                break;
            }

            // BIT 0,H
            case 0x44: {
                bit(0, H);
                break;
            }

            // BIT 0,L
            case 0x45: {
                bit(0, L);
                break;
            }

            // BIT 0,(HL)
            case 0x46: {
                int addr = getAddress(H, L);
                bit(0, addr);
                break;
            }

            // BIT 0,A
            case 0x47: {
                bit(0, A);
                break;
            }

            // BIT 1,B
            case 0x48: {
                bit(1, B);
                break;
            }

            // BIT 1,C
            case 0x49: {
                bit(1, C);
                break;
            }

            // BIT 1,D
            case 0x4A: {
                bit(1, D);
                break;
            }

            // BIT 1,E
            case 0x4B: {
                bit(1, E);
                break;
            }

            // BIT 1,H
            case 0x4C: {
                bit(1, H);
                break;
            }

            // BIT 1,L
            case 0x4D: {
                bit(1, L);
                break;
            }

            // BIT 1,(HL)
            case 0x4E: {
                int addr = getAddress(H, L);
                bit(1, addr);
                break;
            }

            // BIT 1,A
            case 0x4F: {
                bit(1, A);
                break;
            }

            // BIT 2,B
            case 0x50: {
                bit(2, B);
                break;
            }

            // BIT 2,C
            case 0x51: {
                bit(2, C);
                break;
            }

            // BIT 2,D
            case 0x52: {
                bit(2, D);
                break;
            }

            // BIT 2,E
            case 0x53: {
                bit(2, E);
                break;
            }

            // BIT 2,H
            case 0x54: {
                bit(2, H);
                break;
            }

            // BIT 2,L
            case 0x55: {
                bit(2, L);
                break;
            }

            // BIT 2,(HL)
            case 0x56: {
                int addr = getAddress(H, L);
                bit(2, addr);
                break;
            }

            // BIT 2,A
            case 0x57: {
                bit(2, A);
                break;
            }

            // BIT 3,B
            case 0x58: {
                bit(3, B);
                break;
            }

            // BIT 3,C
            case 0x59: {
                bit(3, C);
                break;
            }

            // BIT 3,D
            case 0x5A: {
                bit(3, D);
                break;
            }

            // BIT 3,E
            case 0x5B: {
                bit(3, E);
                break;
            }

            // BIT 3,H
            case 0x5C: {
                bit(3, H);
                break;
            }

            // BIT 3,L
            case 0x5D: {
                bit(3, L);
                break;
            }

            // BIT 3,(HL)
            case 0x5E: {
                int addr = getAddress(H, L);
                bit(3, addr);
                break;
            }

            // BIT 3,A
            case 0x5F: {
                bit(3, A);
                break;
            }

            // BIT 4,B
            case 0x60: {
                bit(4, B);
                break;
            }

            // BIT 4,C
            case 0x61: {
                bit(4, C);
                break;
            }

            // BIT 4,D
            case 0x62: {
                bit(4, D);
                break;
            }

            // BIT 4,E
            case 0x63: {
                bit(4, E);
                break;
            }

            // BIT 4,H
            case 0x64: {
                bit(4, H);
                break;
            }

            // BIT 4,L
            case 0x65: {
                bit(4, L);
                break;
            }

            // BIT 4,(HL)
            case 0x66: {
                int addr = getAddress(H, L);
                bit(4, addr);
                break;
            }

            // BIT 4,A
            case 0x67: {
                bit(4, A);
                break;
            }

            // BIT 5,B
            case 0x68: {
                bit(5, B);
                break;
            }

            // BIT 5,C
            case 0x69: {
                bit(5, C);
                break;
            }

            // BIT 5,D
            case 0x6A: {
                bit(5, D);
                break;
            }

            // BIT 5,E
            case 0x6B: {
                bit(5, E);
                break;
            }

            // BIT 5,H
            case 0x6C: {
                bit(5, H);
                break;
            }

            // BIT 5,L
            case 0x6D: {
                bit(5, L);
                break;
            }

            // BIT 5,(HL)
            case 0x6E: {
                int addr = getAddress(H, L);
                bit(5, addr);
                break;
            }

            // BIT 5,A
            case 0x6F: {
                bit(5, A);
                break;
            }

            // BIT 6,B
            case 0x70: {
                bit(6, B);
                break;
            }

            // BIT 6,C
            case 0x71: {
                bit(6, C);
                break;
            }

            // BIT 6,D
            case 0x72: {
                bit(6, D);
                break;
            }

            // BIT 6,E
            case 0x73: {
                bit(6, E);
                break;
            }

            // BIT 6,H
            case 0x74: {
                bit(6, H);
                break;
            }

            // BIT 6,L
            case 0x75: {
                bit(6, L);
                break;
            }

            // BIT 6,(HL)
            case 0x76: {
                int addr = getAddress(H, L);
                bit(6, addr);
                break;
            }

            // BIT 6,A
            case 0x77: {
                bit(6, A);
                break;
            }

            // BIT 7,B
            case 0x78: {
                bit(7, B);
                break;
            }

            // BIT 7,C
            case 0x79: {
                bit(7, C);
                break;
            }

            // BIT 7,D
            case 0x7A: {
                bit(7, D);
                break;
            }

            // BIT 7,E
            case 0x7B: {
                bit(7, E);
                break;
            }

            // BIT 7,H
            case 0x7C: {
                bit(7, H);
                break;
            }

            // BIT 7,L
            case 0x7D: {
                bit(7, L);
                break;
            }

            // BIT 7,(HL)
            case 0x7E: {
                int addr = getAddress(H, L);
                bit(7, addr);
                break;
            }

            // BIT 7,A
            case 0x7F: {
                bit(7, A);
                break;
            }

            // RES 0,B
            case 0x80: {
                res(0, B);
                break;
            }

            // RES 0,C
            case 0x81: {
                res(0, C);
                break;
            }

            // RES 0,D
            case 0x82: {
                res(0, D);
                break;
            }

            // RES 0,E
            case 0x83: {
                res(0, E);
                break;
            }

            // RES 0,H
            case 0x84: {
                res(0, H);
                break;
            }

            // RES 0,L
            case 0x85: {
                res(0, L);
                break;
            }

            // RES 0,(HL)
            case 0x86: {
                int addr = getAddress(H, L);
                res(0, addr);
                break;
            }

            // RES 0,A
            case 0x87: {
                res(0, A);
                break;
            }

            // RES 1,B
            case 0x88: {
                res(1, B);
                break;
            }

            // RES 1,C
            case 0x89: {
                res(1, C);
                break;
            }

            // RES 1,D
            case 0x8A: {
                res(1, D);
                break;
            }

            // RES 1,E
            case 0x8B: {
                res(1, E);
                break;
            }

            // RES 1,H
            case 0x8C: {
                res(1, H);
                break;
            }

            // RES 1,L
            case 0x8D: {
                res(1, L);
                break;
            }

            // RES 1,(HL)
            case 0x8E: {
                int addr = getAddress(H, L);
                res(1, addr);
                break;
            }

            // RES 1,A
            case 0x8F: {
                res(1, A);
                break;
            }

            // RES 2,B
            case 0x90: {
                res(2, B);
                break;
            }

            // RES 2,C
            case 0x91: {
                res(2, C);
                break;
            }

            // RES 2,D
            case 0x92: {
                res(2, D);
                break;
            }

            // RES 2,E
            case 0x93: {
                res(2, E);
                break;
            }

            // RES 2,H
            case 0x94: {
                res(2, H);
                break;
            }

            // RES 2,L
            case 0x95: {
                res(2, L);
                break;
            }

            // RES 2,(HL)
            case 0x96: {
                int addr = getAddress(H, L);
                res(2, addr);
                break;
            }

            // RES 2,A
            case 0x97: {
                res(2, A);
                break;
            }

            // RES 3,B
            case 0x98: {
                res(3, B);
                break;
            }

            // RES 3,C
            case 0x99: {
                res(3, C);
                break;
            }

            // RES 3,D
            case 0x9A: {
                res(3, D);
                break;
            }

            // RES 3,E
            case 0x9B: {
                res(3, E);
                break;
            }

            // RES 3,H
            case 0x9C: {
                res(3, H);
                break;
            }

            // RES 3,L
            case 0x9D: {
                res(3, L);
                break;
            }

            // RES 3,(HL)
            case 0x9E: {
                int addr = getAddress(H, L);
                res(3, addr);
                break;
            }

            // RES 3,A
            case 0x9F: {
                res(3, A);
                break;
            }

            // RES 4,B
            case 0xA0: {
                res(4, B);
                break;
            }

            // RES 4,C
            case 0xA1: {
                res(4, C);
                break;
            }

            // RES 4,D
            case 0xA2: {
                res(4, D);
                break;
            }

            // RES 4,E
            case 0xA3: {
                res(4, E);
                break;
            }

            // RES 4,H
            case 0xA4: {
                res(4, H);
                break;
            }

            // RES 4,L
            case 0xA5: {
                res(4, L);
                break;
            }

            // RES 4,(HL)
            case 0xA6: {
                int addr = getAddress(H, L);
                res(4, addr);
                break;
            }

            // RES 4,A
            case 0xA7: {
                res(4, A);
                break;
            }

            // RES 5,B
            case 0xA8: {
                res(5, B);
                break;
            }

            // RES 5,C
            case 0xA9: {
                res(5, C);
                break;
            }

            // RES 5,D
            case 0xAA: {
                res(5, D);
                break;
            }

            // RES 5,E
            case 0xAB: {
                res(5, E);
                break;
            }

            // RES 5,H
            case 0xAC: {
                res(5, H);
                break;
            }

            // RES 5,L
            case 0xAD: {
                res(5, L);
                break;
            }

            // RES 5,(HL)
            case 0xAE: {
                int addr = getAddress(H, L);
                res(5, addr);
                break;
            }

            // RES 5,A
            case 0xAF: {
                res(5, A);
                break;
            }

            // RES 6,B
            case 0xB0: {
                res(6, B);
                break;
            }

            // RES 6,C
            case 0xB1: {
                res(6, C);
                break;
            }

            // RES 6,D
            case 0xB2: {
                res(6, D);
                break;
            }

            // RES 6,E
            case 0xB3: {
                res(6, E);
                break;
            }

            // RES 6,H
            case 0xB4: {
                res(6, H);
                break;
            }

            // RES 6,L
            case 0xB5: {
                res(6, L);
                break;
            }

            // RES 6,(HL)
            case 0xB6: {
                int addr = getAddress(H, L);
                res(6, addr);
                break;
            }

            // RES 6,A
            case 0xB7: {
                res(6, A);
                break;
            }

            // RES 7,B
            case 0xB8: {
                res(7, B);
                break;
            }

            // RES 7,C
            case 0xB9: {
                res(7, C);
                break;
            }

            // RES 7,D
            case 0xBA: {
                res(7, D);
                break;
            }

            // RES 7,E
            case 0xBB: {
                res(7, E);
                break;
            }

            // RES 7,H
            case 0xBC: {
                res(7, H);
                break;
            }

            // RES 7,L
            case 0xBD: {
                res(7, L);
                break;
            }

            // RES 7,(HL)
            case 0xBE: {
                int addr = getAddress(H, L);
                res(7, addr);
                break;
            }

            // RES 7,A
            case 0xBF: {
                res(7, A);
                break;
            }

            // SET 0,B
            case 0xC0: {
                set(0, B);
                break;
            }

            // SET 0,C
            case 0xC1: {
                set(0, C);
                break;
            }

            // SET 0,D
            case 0xC2: {
                set(0, D);
                break;
            }

            // SET 0,E
            case 0xC3: {
                set(0, E);
                break;
            }

            // SET 0,H
            case 0xC4: {
                set(0, H);
                break;
            }

            // SET 0,L
            case 0xC5: {
                set(0, L);
                break;
            }

            // SET 0,(HL)
            case 0xC6: {
                int addr = getAddress(H, L);
                set(0, addr);
                break;
            }

            // SET 0,A
            case 0xC7: {
                set(0, A);
                break;
            }

            // SET 1,B
            case 0xC8: {
                set(1, B);
                break;
            }

            // SET 1,C
            case 0xC9: {
                set(1, C);
                break;
            }

            // SET 1,D
            case 0xCA: {
                set(1, D);
                break;
            }

            // SET 1,E
            case 0xCB: {
                set(1, E);
                break;
            }

            // SET 1,H
            case 0xCC: {
                set(1, H);
                break;
            }

            // SET 1,L
            case 0xCD: {
                set(1, L);
                break;
            }

            // SET 1,(HL)
            case 0xCE: {
                int addr = getAddress(H, L);
                set(1, addr);
                break;
            }

            // SET 1,A
            case 0xCF: {
                set(1, A);
                break;
            }

            // SET 2,B
            case 0xD0: {
                set(2, B);
                break;
            }

            // SET 2,C
            case 0xD1: {
                set(2, C);
                break;
            }

            // SET 2,D
            case 0xD2: {
                set(2, D);
                break;
            }

            // SET 2,E
            case 0xD3: {
                set(2, E);
                break;
            }

            // SET 2,H
            case 0xD4: {
                set(2, H);
                break;
            }

            // SET 2,L
            case 0xD5: {
                set(2, L);
                break;
            }

            // SET 2,(HL)
            case 0xD6: {
                int addr = getAddress(H, L);
                set(2, addr);
                break;
            }

            // SET 2,A
            case 0xD7: {
                set(2, A);
                break;
            }

            // SET 3,B
            case 0xD8: {
                set(3, B);
                break;
            }

            // SET 3,C
            case 0xD9: {
                set(3, C);
                break;
            }

            // SET 3,D
            case 0xDA: {
                set(3, D);
                break;
            }

            // SET 3,E
            case 0xDB: {
                set(3, E);
                break;
            }

            // SET 3,H
            case 0xDC: {
                set(3, H);
                break;
            }

            // SET 3,L
            case 0xDD: {
                set(3, L);
                break;
            }

            // SET 3,(HL)
            case 0xDE: {
                int addr = getAddress(H, L);
                set(3, addr);
                break;
            }

            // SET 3,A
            case 0xDF: {
                set(3, A);
                break;
            }

            // SET 4,B
            case 0xE0: {
                set(4, B);
                break;
            }

            // SET 4,C
            case 0xE1: {
                set(4, C);
                break;
            }

            // SET 4,D
            case 0xE2: {
                set(4, D);
                break;
            }

            // SET 4,E
            case 0xE3: {
                set(4, E);
                break;
            }

            // SET 4,H
            case 0xE4: {
                set(4, H);
                break;
            }

            // SET 4,L
            case 0xE5: {
                set(4, L);
                break;
            }

            // SET 4,(HL)
            case 0xE6: {
                int addr = getAddress(H, L);
                set(4, addr);
                break;
            }

            // SET 4,A
            case 0xE7: {
                set(4, A);
                break;
            }

            // SET 5,B
            case 0xE8: {
                set(5, B);
                break;
            }

            // SET 5,C
            case 0xE9: {
                set(5, C);
                break;
            }

            // SET 5,D
            case 0xEA: {
                set(5, D);
                break;
            }

            // SET 5,E
            case 0xEB: {
                set(5, E);
                break;
            }

            // SET 5,H
            case 0xEC: {
                set(5, H);
                break;
            }

            // SET 5,L
            case 0xED: {
                set(5, L);
                break;
            }

            // SET 5,(HL)
            case 0xEE: {
                int addr = getAddress(H, L);
                set(5, addr);
                break;
            }

            // SET 5,A
            case 0xEF: {
                set(5, A);
                break;
            }

            // SET 6,B
            case 0xF0: {
                set(6, B);
                break;
            }

            // SET 6,C
            case 0xF1: {
                set(6, C);
                break;
            }

            // SET 6,D
            case 0xF2: {
                set(6, D);
                break;
            }

            // SET 6,E
            case 0xF3: {
                set(6, E);
                break;
            }

            // SET 6,H
            case 0xF4: {
                set(6, H);
                break;
            }

            // SET 6,L
            case 0xF5: {
                set(6, L);
                break;
            }

            // SET 6,(HL)
            case 0xF6: {
                int addr = getAddress(H, L);
                set(6, addr);
                break;
            }

            // SET 6,A
            case 0xF7: {
                set(6, A);
                break;
            }

            // SET 7,B
            case 0xF8: {
                set(7, B);
                break;
            }

            // SET 7,C
            case 0xF9: {
                set(7, C);
                break;
            }

            // SET 7,D
            case 0xFA: {
                set(7, D);
                break;
            }

            // SET 7,E
            case 0xFB: {
                set(7, E);
                break;
            }

            // SET 7,H
            case 0xFC: {
                set(7, H);
                break;
            }

            // SET 7,L
            case 0xFD: {
                set(7, L);
                break;
            }

            // SET 7,(HL)
            case 0xFE: {
                int addr = getAddress(H, L);
                set(7, addr);
                break;
            }

            // SET 7,A
            case 0xFF: {
                set(7, A);
                break;
            }
        }
    }

    /**
     * Stores <code>value</code> into the memory location, <code>addr</code>.
     *
     * @param addr  the memory location to store <code>value</code>
     * @param value the value to store
     */
    private void ld(int addr, int value) {
        mmu().writeByte(value, addr);
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
            return mmu().readWord(addr);
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
        mmu().writeWord(value, addr);
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
        int value = mmu().readByte(addr);
        F.set(Z_BIT, value == 0xFF);
        F.set(N_BIT, 0);
        F.set(H_BIT, value == 0xFF || value == 0x0F);
        value = (value + 1) & 0xFF;
        mmu().writeByte(value, addr);
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
        F.set(Z_BIT, value == 0xFF);
        F.set(N_BIT, 0);
        F.set(H_BIT, value == 0xFF || value == 0x0F);
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
        int value = mmu().readByte(addr);
        F.set(Z_BIT, value == 0x01);
        F.set(N_BIT, 1);
        F.set(H_BIT, value == 0x00 || value == 0x10);
        value = (value - 1) & 0xFF;
        mmu().writeByte(value, addr);
    }

    private void dec(Register8Bit r) {
        int value = r.read();
        F.set(Z_BIT, value == 0x01);
        F.set(N_BIT, 1);
        F.set(H_BIT, value == 0x00 || value == 0x10);
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

    private void add(Register8Bit r, int addValue) {
        int value = r.read();
        int sum = value + addValue;
        r.write(sum);

        F.set(Z_BIT, sum == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, ((value & 0x0F) + (addValue & 0x0F)) > 0xF);
        F.set(C_BIT, sum > 0xFF);
    }

    private void add(Register16Bit r, int addValue) {
        int value = r.read();
        int sum = value + addValue;
        r.write(sum);
    }

    private void add(Register8Bit r1, Register8Bit r2, int addValue) {
        int value = (r1.read() << 8) + r2.read();
        int sum = value + addValue;
        r1.write(sum >> 8);
        r2.write(sum & 0xFF);

        F.set(N_BIT, 0);
        F.set(H_BIT, (value & 0x0FFF) + (addValue & 0x0FFF) > 0x0FFF);
        F.set(C_BIT, sum > 0xFFFF);
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

    private void sub(Register8Bit r, int subValue) {
        int value = r.read();
        int difference = value - subValue;
        r.write(difference);

        F.set(Z_BIT, difference == 0x00);
        F.set(N_BIT, 1);
        F.set(H_BIT, (difference & 0xF) > (value & 0xF));
        F.set(C_BIT, difference < 0);
    }

    private void sub(Register16Bit r, int subValue) {
        int value = r.read();
        int difference = value - subValue;
        r.write(difference);
    }

    private void sub(Register8Bit r1, Register8Bit r2, int subValue) {
        int value = (r1.read() << 8) + r2.read();
        int difference = value - subValue;
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

    private void or(Register8Bit r, int value) {
        int value1 = r.read();
        int value2 = value;
        int result = value1 | value2;
        F.set(Z_BIT, result == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, 0);
    }

    private void cp(Register8Bit r, int value) {
        int value1 = r.read();
        int value2 = value;
        int difference = value1 - value2;
        F.set(Z_BIT, difference == 0x00);
        F.set(N_BIT, 1);
        F.set(H_BIT, (difference & 0xF) > (value1 & 0xF));
        F.set(C_BIT, difference < 0);
    }

    private void rlc(int addr) {
        int value = mmu().readByte(addr);
        int bit7 = getBit(7, value);
        value = (value << 1) & 0xFF;
        value = setBit(0, value, bit7);
        mmu().writeByte(value, addr);
        F.set(Z_BIT, value == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit7);
    }

    private void rlc(Register8Bit r) {
        int bit7 = r.get(7);
        r.shift(LEFT, 1);
        r.set(0, bit7);
        F.set(Z_BIT, r.read() == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit7);
    }

    private void rrc(int addr) {
        int value = mmu().readByte(addr);
        int bit0 = getBit(0, value);
        value = (value >> 1) & 0xFF;
        value = setBit(7, value, bit0);
        mmu().writeByte(value, addr);
        F.set(Z_BIT, value == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit0);
    }

    private void rrc(Register8Bit r) {
        int bit0 = r.get(0);
        r.shift(RIGHT, 1);
        r.set(7, bit0);
        F.set(Z_BIT, r.read() == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit0);
    }

    private void rl(int addr) {
        int value = mmu().readByte(addr);
        int bit7 = getBit(7, value);
        int cBit = F.get(C_BIT);
        value = (value << 1) & 0xFF;
        value = setBit(0, value, cBit);
        mmu().writeByte(value, addr);
        F.set(Z_BIT, value == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit7);
    }

    private void rl(Register8Bit r) {
        int bit7 = r.get(7);
        int cBit = F.get(C_BIT);
        r.shift(LEFT, 1);
        r.set(0, cBit);
        F.set(Z_BIT, r.read() == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit7);
    }

    private void rr(int addr) {
        int value = mmu().readByte(addr);
        int bit0 = getBit(0, value);
        int cBit = F.get(C_BIT);
        value = (value >> 1) & 0xFF;
        value = setBit(7, value, cBit);
        mmu().writeByte(value, addr);
        F.set(Z_BIT, value == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit0);
    }

    private void rr(Register8Bit r) {
        int bit0 = r.get(0);
        int cBit = F.get(C_BIT);
        r.shift(RIGHT, 1);
        r.set(7, cBit);
        F.set(Z_BIT, r.read() == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit0);
    }

    private void sla(int addr) {
        int value = mmu().readByte(addr);
        int bit7 = getBit(7, value);
        value = (value << 1) & 0xFF;
        value = setBit(0, value, 0);
        mmu().writeByte(value, addr);
        F.set(Z_BIT, value == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit7);
    }

    private void sla(Register8Bit r) {
        int bit7 = r.get(7);
        r.shift(LEFT, 1);
        r.set(0, 0);
        F.set(Z_BIT, r.read() == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit7);
    }

    private void sra(int addr) {
        int value = mmu().readByte(addr);
        int bit0 = getBit(0, value);
        int bit7 = getBit(7, value);
        value = (value >> 1) & 0xFF;
        value = setBit(7, value, bit7);
        mmu().writeByte(value, addr);
        F.set(Z_BIT, value == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit0);
    }

    private void sra(Register8Bit r) {
        int bit0 = r.get(0);
        int bit7 = r.get(7);
        r.shift(RIGHT, 1);
        r.set(7, bit7);
        F.set(Z_BIT, r.read() == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit0);
    }

    private void swap(int addr) {
        int value = mmu().readByte(addr);
        int hi = (value >> 4);
        int lo = (value & 0xF);
        value = (lo << 4) + hi;
        mmu().writeByte(value, addr);
        F.set(Z_BIT, value == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, 0);
    }

    private void swap(Register8Bit r) {
        int hi = (r.read() >> 4);
        int lo = (r.read() & 0xF);
        r.write((lo << 4) + hi);
        F.set(Z_BIT, r.read() == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, 0);
    }

    private void srl(int addr) {
        int value = mmu().readByte(addr);
        int bit0 = getBit(0, value);
        value = (value >> 1) & 0xFF;
        value = setBit(7, value, 0);
        mmu().writeByte(value, addr);
        F.set(Z_BIT, value == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit0);
    }

    private void srl(Register8Bit r) {
        int bit0 = r.get(0);
        r.shift(RIGHT, 1);
        r.set(7, 0);
        F.set(Z_BIT, r.read() == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 0);
        F.set(C_BIT, bit0);
    }

    private void bit(int bit, int addr) {
        int value = mmu().readByte(addr);
        int b = getBit(bit, value);
        F.set(Z_BIT, b == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 1);
    }

    private void bit(int bit, Register8Bit r) {
        int b = r.get(bit);
        F.set(Z_BIT, b == 0);
        F.set(N_BIT, 0);
        F.set(H_BIT, 1);
    }

    private void res(int bit, int addr) {
        int value = mmu().readByte(addr);
        value = setBit(bit, value, 0);
        mmu().writeByte(value, addr);
    }

    private void res(int bit, Register8Bit r) {
        r.set(bit, 0);
    }

    private void set(int bit, int addr) {
        int value = mmu().readByte(addr);
        value = setBit(bit, value, 1);
        mmu().writeByte(value, addr);
    }

    private void set(int bit, Register8Bit r) {
        r.set(bit, 1);
    }

    private int getBit(int bit, int value) {
        return (value >> bit) & 1;
    }

    private int setBit(int bit, int value, int set) {
        return setBit(bit, value, set == 1);
    }

    private int setBit(int bit, int value, boolean set) {
        value = (set) ? value | (1 << bit) : value & ~(1 << bit);
        return value;
    }

    private int getAddress(Register16Bit r) {
        return r.read();
    }

    private int getAddress(Register8Bit r1, Register8Bit r2) {
        return (r1.read() << 8) + r2.read();
    }

    public GbcCpuClock getClock() {
        return clock;
    }
}
