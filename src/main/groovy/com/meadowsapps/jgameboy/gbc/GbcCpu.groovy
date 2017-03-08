package com.meadowsapps.jgameboy.gbc

import com.meadowsapps.jgameboy.core.util.Register16Bit
import com.meadowsapps.jgameboy.core.util.UInt16
import com.meadowsapps.jgameboy.core.util.UInt8
import com.meadowsapps.jgameboy.core.util.Utilities
import groovy.lang.Closure as Opcode
import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/5/17.
 */
// todo: update cycles - 0x74
@InheritConstructors
class GbcCpu extends GbcCoreElement {

    private boolean debug = true

    private Register16Bit AF

    private Register16Bit BC

    private Register16Bit DE

    private Register16Bit HL

    private Register16Bit SP

    private Register16Bit PC

    private Opcode[] opcodes

    private Opcode[] opcodesCB

    private int cycles

    private static final int Z = 7

    private static final int N = 6

    private static final int H = 5

    private static final int C = 4

    void initialize() {
        AF = new Register16Bit()
        BC = new Register16Bit()
        DE = new Register16Bit()
        HL = new Register16Bit()
        SP = new Register16Bit()
        PC = new Register16Bit()

        long start = System.currentTimeMillis()
        opcodes = new Opcode[0x100]
        opcodesCB = new Opcode[0x100]
        for (int i = 0; i < opcodes.length; i++) {
            String hex = Utilities.toHex(i)
            opcodes[i] = this.&"Opcode${hex}"
        }
        long stop = System.currentTimeMillis()
        println(stop - start)
    }

    void execute() {
        UInt8 opcode = mmu().readByte(PC.word)
        opcodes[opcode.intValue()].call()
    }

    /**
     * NOP
     */
    private void Opcode0x00() {
        cycles = 4
        PC.word++
    }

    /**
     * LD BC,d16
     */
    private void Opcode0x01() {
        UInt16 d16 = mmu().readWord(PC.word + 1)
        BC.word = d16

        cycles = 12
        PC.word += 3
    }

    /**
     * LD (BC),A
     */
    private void Opcode0x02() {
        mmu().writeByte(BC.word, AF.hi)

        cycles = 8
        PC.word++
    }

    /**
     * INC BC
     */
    private void Opcode0x03() {
        BC.word++

        cycles = 8
        PC.word++
    }

    /**
     * INC B
     */
    private void Opcode0x04() {
        BC.hi++

        AF.lo[Z] = BC.hi.equals(0x00)
        AF.lo[N] = 0
        AF.lo[H] = BC.hi.equals(0x00) || BC.hi.equals(0x10)

        cycles = 4
        PC.word++
    }

    /**
     * DEC B
     */
    private void Opcode0x05() {
        BC.hi--

        AF.lo[Z] = BC.hi.equals(0x00)
        AF.lo[N] = 1
        AF.lo[H] = BC.hi.equals(0xFF) || BC.hi.equals(0x0F)

        cycles = 4
        PC.word++
    }

    /**
     * LD B,d8
     */
    private void Opcode0x06() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        BC.hi = d8

        cycles = 8
        PC.word += 2
    }

    /**
     * RLCA
     */
    private void Opcode0x07() {
        int bit7 = AF.hi[7]
        AF.hi = AF.hi << 1
        AF.hi[0] = bit7

        AF.lo[Z] = 0
        AF.lo[H] = 0
        AF.lo[N] = 0
        AF.lo[C] = bit7

        cycles = 4
        PC.word++
    }

    /**
     * LD (a16),SP
     */
    private void Opcode0x08() {
        UInt16 a16 = mmu().readWord(PC.word + 1)
        mmu().writeWord(a16, SP.word)

        cycles = 20
        PC.word += 3
    }

    /**
     * ADD HL,BC
     */
    private void Opcode0x09() {
        int hl = HL.word.intValue()
        int bc = BC.word.intValue()
        HL.word += BC.word

        AF.lo[N] = 0
        AF.lo[H] = (hl & 0x0FFF) + (bc & 0x0FFF) > 0x0FFF
        AF.lo[C] = HL.word.intValue() > 0xFFFF

        cycles = 8
        PC.word++
    }

    /**
     * LD A,(BC)
     */
    private void Opcode0x0A() {
        AF.hi = mmu().readByte(BC.word)

        cycles = 8
        PC.word++
    }

    /**
     * DEC BC
     */
    private void Opcode0x0B() {
        BC.word--

        cycles = 8
        PC.word++
    }

    /**
     * INC C
     */
    private void Opcode0x0C() {
        BC.lo++

        AF.lo[Z] = BC.lo.equals(0x00)
        AF.lo[N] = 0
        AF.lo[H] = BC.lo.equals(0x00) || BC.lo.equals(0x10)

        cycles = 4
        PC.word++
    }

    /**
     * DEC C
     */
    private void Opcode0x0D() {
        BC.lo--

        AF.lo[Z] = BC.lo.equals(0x00)
        AF.lo[N] = 1
        AF.lo[H] = BC.lo.equals(0xFF) | BC.lo.equals(0x0F)

        cycles = 4
        PC.word++
    }

    /**
     * LD C,d8
     */
    private void Opcode0x0E() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        BC.lo = d8

        cycles = 8
        PC.word += 2
    }

    /**
     * RRCA
     */
    private void Opcode0x0F() {
        int bit0 = AF.hi[0]
        AF.hi = AF.hi >> 1
        AF.hi[7] = bit0

        AF.lo[Z] = 0
        AF.lo[H] = 0
        AF.lo[N] = 0
        AF.lo[C] = bit0

        cycles = 4
        PC.word++
    }

    /**
     * STOP 0
     */
    private void Opcode0x10() {
        // todo: ensure this is correct
        if (!debug)
            while (AF.hi.equals(0x00) || mmu().readByte(0xFF00).equals(0x00)); ;

        cycles = 4
        if (mmu().readByte(PC.word + 1).equals(0x00)) {
            PC.word += 2
        } else {
            PC.word++
        }
    }

    /**
     * LD DE,d16
     */
    private void Opcode0x11() {
        UInt16 d16 = mmu().readWord(PC.word + 1)
        DE.word = d16

        cycles = 12
        PC.word += 3
    }

    /**
     * LD (DE),A
     */
    private void Opcode0x12() {
        mmu().writeByte(DE.word, AF.hi)

        cycles = 8
        PC.word++
    }

    /**
     * INC DE
     */
    private void Opcode0x13() {
        DE.word++

        cycles = 8
        PC.word++
    }

    /**
     * INC D
     */
    private void Opcode0x14() {
        DE.hi++

        AF.lo[Z] = DE.hi.equals(0x00)
        AF.lo[N] = 0
        AF.lo[H] = DE.hi.equals(0x00) || DE.hi.equals(0x10)

        cycles = 4
        PC.word++
    }

    /**
     * DEC D
     */
    private void Opcode0x15() {
        DE.hi--

        AF.lo[Z] = DE.hi.equals(0x00)
        AF.lo[N] = 1
        AF.lo[H] = DE.hi.equals(0xFF) || DE.hi.equals(0x0F)

        cycles = 4
        PC.word++
    }

    /**
     * LD D,d8
     */
    private void Opcode0x16() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        BC.hi = d8

        cycles = 8
        PC.word += 2
    }

    /**
     * RLA
     */
    private void Opcode0x17() {
        int bit7 = AF.hi[7]
        int bitC = AF.lo[C]
        AF.hi = AF.hi << 1
        AF.hi[0] = bitC

        AF.lo[Z] = 0
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = bit7

        cycles = 4
        PC.word++
    }

    /**
     * JR r8
     */
    private void Opcode0x18() {
        byte r8 = mmu().readByte(PC.word + 1).byteValue()
        PC.word += r8

        cycles = 12
        PC.word += 2
    }

    /**
     * ADD HL,DE
     */
    private void Opcode0x19() {
        int hl = HL.word.intValue()
        int de = DE.word.intValue()
        HL.word += DE.word

        AF.lo[N] = 0
        AF.lo[H] = (hl & 0x0FFF) + (de & 0x0FFF) > 0x0FFF
        AF.lo[C] = HL.word.intValue() > 0xFFFF

        cycles = 8
        PC.word++
    }

    /**
     * LD A,(DE)
     */
    private void Opcode0x1A() {
        AF.hi = mmu().readByte(DE.word)

        cycles = 8
        PC.word++
    }

    /**
     * DEC DE
     */
    private void Opcode0x1B() {
        DE.word--

        cycles = 8
        PC.word++
    }

    /**
     * INC E
     */
    private void Opcode0x1C() {
        DE.lo++

        AF.lo[Z] = DE.lo.equals(0x00)
        AF.lo[N] = 0
        AF.lo[H] = DE.lo.equals(0x00) || DE.lo.equals(0x10)

        cycles = 4
        PC.word++
    }

    /**
     * DEC E
     */
    private void Opcode0x1D() {
        DE.lo--

        AF.lo[Z] = DE.lo.equals(0x00)
        AF.lo[N] = 1
        AF.lo[H] = DE.lo.equals(0xFF) || DE.lo.equals(0x0F)

        cycles = 4
        PC.word++
    }

    /**
     * LD E,d8
     */
    private void Opcode0x1E() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        DE.lo = d8

        cycles = 8
        PC.word += 2
    }

    /**
     * RRA
     */
    private void Opcode0x1F() {
        int bit0 = AF.hi[0]
        AF.hi = AF.hi >> 1
        int bitC = AF.lo[C]
        AF.hi[7] = bitC

        AF.lo[Z] = 0
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = bit0

        cycles = 4
        PC.word++
    }

    /**
     * JR NZ,r8
     */
    private void Opcode0x20() {
        if (AF.lo[Z] != 1) {
            byte r8 = mmu().readByte(PC.word + 1).byteValue()
            PC.word += r8

            cycles = 12
        } else {
            cycles = 8
        }
        PC.word += 2
    }

    /**
     * LD HL,d16
     */
    private void Opcode0x21() {
        UInt16 d16 = mmu().readWord(PC.word + 1)
        HL.word = d16

        cycles = 12
        PC.word += 3
    }

    /**
     * LD (HL+),A
     */
    private void Opcode0x22() {
        mmu().writeByte(HL.word, AF.hi)
        HL.word++

        cycles = 8
        PC.word++
    }

    /**
     * INC HL
     */
    private void Opcode0x23() {
        HL.word++

        cycles = 8
        PC.word++
    }

    /**
     * INC H
     */
    private void Opcode0x24() {
        HL.hi++

        AF.lo[Z] = HL.hi.equals(0x00)
        AF.lo[N] = 0
        AF.lo[H] = HL.hi.equals(0x00) || HL.hi.equals(0x10)

        cycles = 4
        PC.word++
    }

    /**
     * DEC H
     */
    private void Opcode0x25() {
        HL.hi--

        AF.lo[Z] = HL.hi.equals(0x00)
        AF.lo[N] = 1
        AF.lo[H] = HL.hi.equals(0xFF) || HL.hi.equals(0x0F)

        cycles = 4
        PC.word++
    }

    /**
     * LD H,d8
     */
    private void Opcode0x26() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        HL.hi = d8

        cycles = 8
        PC.word += 2
    }

    /**
     * DAA
     */
    private void Opcode0x27() {
        // todo: 0x27 (DAA) - ensure this is correct
        int correctionFactor = 0
        if (AF.hi.intValue() > 0x99 || AF.lo[C] == 1) {
            correctionFactor = 0x60 + (correctionFactor & 0x0F)
            AF.lo[C] = 1
        } else {
            correctionFactor = 0x00
            AF.lo[C] = 0
        }

        if ((AF.hi.intValue() & 0x0F) > 0x09 || AF.lo[H] == 1) {
            correctionFactor = (correctionFactor & 0xF0) + 0x06
        } else {
            correctionFactor = (correctionFactor & 0xF0)
        }

        if (AF.lo[N] != 1) {
            AF.hi += correctionFactor
        } else {
            AF.hi -= correctionFactor
        }

        cycles = 4
        PC.word++
    }

    /**
     * JR Z,r8
     */
    private void Opcode0x28() {
        if (AF.lo[Z] == 1) {
            byte r8 = mmu().readByte(PC.word + 1).byteValue()
            PC.word += r8

            cycles = 12
        } else {
            cycles = 8
        }
        PC.word += 2
    }

    /**
     * ADD HL,HL
     */
    private void Opcode0x29() {
        int hl = HL.word.intValue()
        HL.word += HL.word

        AF.lo[N] = 0
        AF.lo[H] = (hl & 0x0FFF) + (hl & 0x0FFF) > 0x0FFF
        AF.lo[C] = HL.word.intValue() > 0xFFFF

        cycles = 8
        PC.word++
    }

    /**
     * LD A,(HL+)
     */
    private void Opcode0x2A() {
        AF.hi = mmu().readByte(HL.word)
        HL.word++

        cycles = 8
        PC.word++
    }

    /**
     * DEC HL
     */
    private void Opcode0x2B() {
        HL.word--

        cycles = 8
        PC.word++
    }

    /**
     * INC L
     */
    private void Opcode0x2C() {
        HL.lo++

        AF.lo[Z] = HL.lo.equals(0x00)
        AF.lo[N] = 0
        AF.lo[H] = HL.lo.equals(0x00) || HL.lo.equals(0x10)

        cycles = 4
        PC.word++
    }

    /**
     * DEC L
     */
    private void Opcode0x2D() {
        HL.lo--

        AF.lo[Z] = HL.lo.equals(0x00)
        AF.lo[N] = 1
        AF.lo[H] = HL.lo.equals(0xFF) || HL.lo.equals(0x0F)

        cycles = 4
        PC.word++
    }

    /**
     * LD L,d8
     */
    private void Opcode0x2E() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        HL.lo = d8

        cycles = 8
        PC.word += 2
    }

    /**
     * CPL
     */
    private void Opcode0x2F() {
        AF.hi = ~AF.hi

        AF.lo[N] = 1
        AF.lo[H] = 1

        cycles = 4
        PC.word++
    }

    /**
     * JR NC,r8
     */
    private void Opcode0x30() {
        if (AF.lo[C] != 1) {
            byte r8 = mmu().readByte(PC.word + 1).byteValue()
            PC.word += r8

            cycles = 12
        } else {
            cycles = 8
        }
        PC.word += 2
    }

    /**
     * LD SP,d16
     */
    private void Opcode0x31() {
        UInt16 d16 = mmu().readWord(PC.word + 1)
        SP.word = d16

        cycles = 12
        PC.word += 3
    }

    /**
     * LD (HL-),A
     */
    private void Opcode0x32() {
        mmu().writeByte(HL.word, AF.hi)
        HL.word--

        cycles = 8
        PC.word++
    }

    /**
     * INC SP
     */
    private void Opcode0x33() {
        SP.word++

        cycles = 8
        PC.word++
    }

    /**
     * INC (HL)
     */
    private void Opcode0x34() {
        UInt8 value = mmu().readByte(HL.word) + 1
        mmu().writeByte(HL.word, value)

        AF.lo[Z] = value.equals(0x00)
        AF.lo[N] = 0
        AF.lo[H] = value.equals(0x00) || value.equals(0x10)

        cycles = 12
        PC.word++
    }

    /**
     * DEC (HL)
     */
    private void Opcode0x35() {
        UInt8 value = mmu().readByte(HL.word) - 1
        mmu().writeByte(HL.word, value)

        AF.lo[Z] = value.equals(0x00)
        AF.lo[N] = 1
        AF.lo[H] = value.equals(0xFF) || value.equals(0x0F)

        cycles = 12
        PC.word++
    }

    /**
     * LD (HL),d8
     */
    private void Opcode0x36() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        mmu().writeByte(HL.word, d8)

        cycles = 12
        PC.word += 2
    }

    /**
     * SCF
     */
    private void Opcode0x37() {
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 1

        cycles = 4
        PC.word++
    }

    /**
     * JR C,r8
     */
    private void Opcode0x38() {
        if (AF.lo[C] == 1) {
            byte r8 = mmu().readByte(PC.word + 1).byteValue()
            PC.word += r8

            cycles = 12
        } else {
            cycles = 8
        }
        PC.word += 2
    }

    /**
     * ADD HL,SP
     */
    private void Opcode0x39() {
        int hl = HL.word.intValue()
        int sp = SP.word.intValue()
        HL.word += SP.word

        AF.lo[N] = 0
        AF.lo[H] = (hl & 0x0FFF) + (sp & 0x0FFF) > 0x0FFF
        AF.lo[C] = HL.word.intValue() > 0xFFFF

        cycles = 8
        PC.word++
    }

    /**
     * LD A,(HL-)
     */
    private void Opcode0x3A() {
        AF.hi = mmu().readByte(HL.word)
        HL.word--

        cycles = 8
        PC.word++
    }

    /**
     * DEC SP
     */
    private void Opcode0x3B() {
        SP.word--

        cycles = 8
        PC.word++
    }

    /**
     * INC A
     */
    private void Opcode0x3C() {
        AF.hi++

        AF.lo[Z] = AF.lo.equals(0x00)
        AF.lo[N] = 0
        AF.lo[H] = AF.lo.equals(0x00) || AF.lo.equals(0x10)

        cycles = 4
        PC.word++
    }

    /**
     * DEC A
     */
    private void Opcode0x3D() {
        AF.hi--

        AF.lo[Z] = AF.hi.equals(0x00)
        AF.lo[N] = 1
        AF.lo[H] = AF.hi.equals(0xFF) || AF.hi.equals(0x0F)

        cycles = 4
        PC.word++
    }

    /**
     * LD A,d8
     */
    private void Opcode0x3E() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        AF.hi = d8

        cycles = 8
        PC.word += 2
    }

    /**
     * CCF
     */
    private void Opcode0x3F() {
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = ~AF.lo[C]

        cycles = 4
        PC.word++
    }

    /**
     * LD B,B
     */
    private void Opcode0x40() {
        BC.hi = BC.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD B,C
     */
    private void Opcode0x41() {
        BC.hi = BC.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD B,D
     */
    private void Opcode0x42() {
        BC.hi = DE.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD B,E
     */
    private void Opcode0x43() {
        BC.hi = DE.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD B,H
     */
    private void Opcode0x44() {
        BC.hi = HL.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD B,L
     */
    private void Opcode0x45() {
        BC.hi = HL.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD B,(HL)
     */
    private void Opcode0x46() {
        BC.hi = mmu().readByte(HL.word)

        cycles = 8
        PC.word++
    }

    /**
     * LD B,A
     */
    private void Opcode0x47() {
        BC.hi = AF.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD C,B
     */
    private void Opcode0x48() {
        BC.lo = BC.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD C,C
     */
    private void Opcode0x49() {
        BC.lo = BC.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD C,D
     */
    private void Opcode0x4A() {
        BC.lo = DE.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD C,E
     */
    private void Opcode0x4B() {
        BC.lo = DE.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD C,H
     */
    private void Opcode0x4C() {
        BC.lo = HL.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD C,L
     */
    private void Opcode0x4D() {
        BC.lo = HL.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD C,(HL)
     */
    private void Opcode0x4E() {
        BC.lo = mmu().readByte(HL.word)

        cycles = 8
        PC.word++
    }

    /**
     * LD C,A
     */
    private void Opcode0x4F() {
        BC.lo = AF.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD D,B
     */
    private void Opcode0x50() {
        DE.hi = BC.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD D,C
     */
    private void Opcode0x51() {
        DE.hi = BC.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD D,D
     */
    private void Opcode0x52() {
        DE.hi = DE.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD D,E
     */
    private void Opcode0x53() {
        DE.hi = DE.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD D,H
     */
    private void Opcode0x54() {
        DE.hi = HL.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD D,L
     */
    private void Opcode0x55() {
        DE.hi = HL.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD D,(HL)
     */
    private void Opcode0x56() {
        DE.hi = mmu().readByte(HL.word)

        cycles = 8
        PC.word++
    }

    /**
     * LD D,A
     */
    private void Opcode0x57() {
        DE.hi = AF.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD E,B
     */
    private void Opcode0x58() {
        DE.lo = BC.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD E,C
     */
    private void Opcode0x59() {
        DE.lo = BC.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD E,D
     */
    private void Opcode0x5A() {
        DE.lo = DE.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD E,E
     */
    private void Opcode0x5B() {
        DE.lo = DE.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD E,H
     */
    private void Opcode0x5C() {
        DE.lo = HL.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD E,L
     */
    private void Opcode0x5D() {
        DE.lo = HL.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD E,(HL)
     */
    private void Opcode0x5E() {
        DE.lo = mmu().readByte(HL.word)

        cycles = 8
        PC.word++
    }

    /**
     * LD E,A
     */
    private void Opcode0x5F() {
        DE.lo = AF.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD H,B
     */
    private void Opcode0x60() {
        HL.hi = BC.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD H,C
     */
    private void Opcode0x61() {
        HL.hi = BC.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD H,D
     */
    private void Opcode0x62() {
        HL.hi = DE.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD H,E
     */
    private void Opcode0x63() {
        HL.hi = DE.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD H,H
     */
    private void Opcode0x64() {
        HL.hi = HL.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD H,L
     */
    private void Opcode0x65() {
        HL.hi = HL.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD H,(HL)
     */
    private void Opcode0x66() {
        HL.hi = mmu().readByte(HL.word)

        cycles = 8
        PC.word++
    }

    /**
     * LD H,A
     */
    private void Opcode0x67() {
        HL.hi = AF.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD L,B
     */
    private void Opcode0x68() {
        HL.lo = BC.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD L,C
     */
    private void Opcode0x69() {
        HL.lo = BC.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD L,D
     */
    private void Opcode0x6A() {
        HL.lo = DE.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD L,E
     */
    private void Opcode0x6B() {
        HL.lo = DE.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD L,H
     */
    private void Opcode0x6C() {
        HL.lo = HL.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD L,L
     */
    private void Opcode0x6D() {
        HL.lo = HL.lo

        cycles = 4
        PC.word++
    }

    /**
     * LD L,(HL)
     */
    private void Opcode0x6E() {
        HL.lo = mmu().readByte(HL.word)

        cycles = 8
        PC.word++
    }

    /**
     * LD L,A
     */
    private void Opcode0x6F() {
        HL.lo = AF.hi

        cycles = 4
        PC.word++
    }

    /**
     * LD (HL),B
     */
    private void Opcode0x70() {
        mmu().writeByte(HL.word, BC.hi)

        cycles = 8
        PC.word++
    }

    /**
     * LD (HL),C
     */
    private void Opcode0x71() {
        mmu().writeByte(HL.word, BC.lo)

        cycles = 8
        PC.word++
    }

    /**
     * LD (HL),D
     */
    private void Opcode0x72() {
        mmu().writeByte(HL.word, DE.hi)

        cycles = 8
        PC.word++
    }

    /**
     * LD (HL),E
     */
    private void Opcode0x73() {
        mmu().writeByte(HL.word, DE.lo)

        cycles = 8
        PC.word++
    }

    /**
     * LD (HL),H
     */
    private void Opcode0x74() {
        mmu().writeByte(HL.word, HL.hi)

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * LD (HL),L
     */
    private void Opcode0x75() {
        mmu().writeByte(HL.word, HL.lo)

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * HALT
     */
    private void Opcode0x76() {
        // todo: add opcode operation
        clock.m = 1
        clock.t = 4
    }

    /**
     * LD (HL),A
     */
    private void Opcode0x77() {
        mmu().writeByte(HL.word, AF.hi)

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * LD A,B
     */
    private void Opcode0x78() {
        AF.hi = BC.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD A,C
     */
    private void Opcode0x79() {
        AF.hi = BC.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD A,D
     */
    private void Opcode0x7A() {
        AF.hi = DE.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD A,E
     */
    private void Opcode0x7B() {
        AF.hi = DE.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD A,H
     */
    private void Opcode0x7C() {
        AF.hi = HL.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD A,L
     */
    private void Opcode0x7D() {
        AF.hi = HL.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD A,(HL)
     */
    private void Opcode0x7E() {
        AF.hi = mmu().readByte(HL.word)

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD A,A
     */
    private void Opcode0x7F() {
        AF.hi = AF.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADD A,B
     */
    private void Opcode0x80() {
        int value1 = AF.hi.intValue()
        int value2 = BC.hi.intValue()
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADD A,C
     */
    private void Opcode0x81() {
        int value1 = AF.hi.intValue()
        int value2 = BC.lo.intValue()
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADD A,D
     */
    private void Opcode0x82() {
        int value1 = AF.hi.intValue()
        int value2 = DE.hi.intValue()
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADD A,E
     */
    private void Opcode0x83() {
        int value1 = AF.hi.intValue()
        int value2 = DE.lo.intValue()
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADD A,H
     */
    private void Opcode0x84() {
        int value1 = AF.hi.intValue()
        int value2 = HL.hi.intValue()
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADD A,L
     */
    private void Opcode0x85() {
        int value1 = AF.hi.intValue()
        int value2 = HL.lo.intValue()
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADD A,(HL)
     */
    private void Opcode0x86() {
        int value1 = AF.hi.intValue()
        int value2 = mmu().readByte(HL.word)
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADD A,A
     */
    private void Opcode0x87() {
        int value1 = AF.hi.intValue()
        int value2 = AF.hi.intValue()
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADC A,B
     */
    private void Opcode0x88() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = BC.hi.intValue() + bitC
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADC A,C
     */
    private void Opcode0x89() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = BC.lo.intValue() + bitC
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADC A,D
     */
    private void Opcode0x8A() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = DE.hi.intValue() + bitC
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADC A,E
     */
    private void Opcode0x8B() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = DE.lo.intValue() + bitC
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADC A,H
     */
    private void Opcode0x8C() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = HL.hi.intValue() + bitC
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADC A,L
     */
    private void Opcode0x8D() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = HL.lo.intValue() + bitC
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADC A,(HL)
     */
    private void Opcode0x8E() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = mmu().readByte(HL.word) + bitC
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * ADC A,A
     */
    private void Opcode0x8F() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = AF.hi.intValue() + bitC
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SUB A,B
     */
    private void Opcode0x90() {
        int value1 = AF.hi.intValue()
        int value2 = BC.hi.intValue()
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SUB A,C
     */
    private void Opcode0x91() {
        int value1 = AF.hi.intValue()
        int value2 = BC.lo.intValue()
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SUB A,D
     */
    private void Opcode0x92() {
        int value1 = AF.hi.intValue()
        int value2 = DE.hi.intValue()
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SUB A,E
     */
    private void Opcode0x93() {
        int value1 = AF.hi.intValue()
        int value2 = DE.lo.intValue()
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SUB A,H
     */
    private void Opcode0x94() {
        int value1 = AF.hi.intValue()
        int value2 = HL.hi.intValue()
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SUB A,L
     */
    private void Opcode0x95() {
        int value1 = AF.hi.intValue()
        int value2 = HL.lo.intValue()
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SUB A,(HL)
     */
    private void Opcode0x96() {
        int value1 = AF.hi.intValue()
        int value2 = mmu().readByte(HL.word)
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * SUB A,A
     */
    private void Opcode0x97() {
        int value1 = AF.hi.intValue()
        int value2 = AF.hi.intValue()
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SBC A,B
     */
    private void Opcode0x98() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = BC.hi.intValue() + bitC
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SBC A,C
     */
    private void Opcode0x99() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = BC.lo.intValue() + bitC
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SBC A,D
     */
    private void Opcode0x9A() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = DE.hi.intValue() + bitC
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SBC A,E
     */
    private void Opcode0x9B() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = DE.lo.intValue() + bitC
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SBC A,H
     */
    private void Opcode0x9C() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = HL.hi.intValue() + bitC
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SBC A,L
     */
    private void Opcode0x9D() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = HL.lo.intValue() + bitC
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SBC A,(HL)
     */
    private void Opcode0x9E() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = mmu().readByte(HL.word) + bitC
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * SBC A,A
     */
    private void Opcode0x9F() {
        int bitC = AF.lo[C]
        int value1 = AF.hi.intValue()
        int value2 = AF.hi.intValue() + bitC
        int difference = value1 - value2
        AF.hi = difference

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * AND B
     */
    private void Opcode0xA0() {
        int value1 = AF.hi.intValue()
        int value2 = BC.hi.intValue()
        int result = value1 & value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 1
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * AND C
     */
    private void Opcode0xA1() {
        int value1 = AF.hi.intValue()
        int value2 = BC.lo.intValue()
        int result = value1 & value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 1
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * AND D
     */
    private void Opcode0xA2() {
        int value1 = AF.hi.intValue()
        int value2 = DE.hi.intValue()
        int result = value1 & value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 1
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * AND E
     */
    private void Opcode0xA3() {
        int value1 = AF.hi.intValue()
        int value2 = DE.lo.intValue()
        int result = value1 & value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 1
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * AND H
     */
    private void Opcode0xA4() {
        int value1 = AF.hi.intValue()
        int value2 = HL.hi.intValue()
        int result = value1 & value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 1
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * AND L
     */
    private void Opcode0xA5() {
        int value1 = AF.hi.intValue()
        int value2 = HL.lo.intValue()
        int result = value1 & value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 1
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * AND (HL)
     */
    private void Opcode0xA6() {
        int value1 = AF.hi.intValue()
        int value2 = mmu().readByte(HL.word)
        int result = value1 & value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 1
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * AND A
     */
    private void Opcode0xA7() {
        int value1 = AF.hi.intValue()
        int value2 = AF.hi.intValue()
        int result = value1 & value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 1
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * XOR B
     */
    private void Opcode0xA8() {
        int value1 = AF.hi.intValue()
        int value2 = BC.hi.intValue()
        int result = value1 ^ value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * XOR C
     */
    private void Opcode0xA9() {
        int value1 = AF.hi.intValue()
        int value2 = BC.lo.intValue()
        int result = value1 ^ value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * XOR D
     */
    private void Opcode0xAA() {
        int value1 = AF.hi.intValue()
        int value2 = DE.hi.intValue()
        int result = value1 ^ value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * XOR E
     */
    private void Opcode0xAB() {
        int value1 = AF.hi.intValue()
        int value2 = DE.lo.intValue()
        int result = value1 ^ value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * XOR H
     */
    private void Opcode0xAC() {
        int value1 = AF.hi.intValue()
        int value2 = HL.hi.intValue()
        int result = value1 ^ value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * XOR L
     */
    private void Opcode0xAD() {
        int value1 = AF.hi.intValue()
        int value2 = HL.lo.intValue()
        int result = value1 ^ value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * XOR (HL)
     */
    private void Opcode0xAE() {
        int value1 = AF.hi.intValue()
        int value2 = mmu().readByte(HL.word)
        int result = value1 ^ value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * XOR A
     */
    private void Opcode0xAF() {
        int value1 = AF.hi.intValue()
        int value2 = AF.hi.intValue()
        int result = value1 ^ value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * OR B
     */
    private void Opcode0xB0() {
        int value1 = AF.hi.intValue()
        int value2 = BC.hi.intValue()
        int result = value1 | value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * OR C
     */
    private void Opcode0xB1() {
        int value1 = AF.hi.intValue()
        int value2 = BC.lo.intValue()
        int result = value1 | value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * OR D
     */
    private void Opcode0xB2() {
        int value1 = AF.hi.intValue()
        int value2 = DE.hi.intValue()
        int result = value1 | value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * OR E
     */
    private void Opcode0xB3() {
        int value1 = AF.hi.intValue()
        int value2 = DE.lo.intValue()
        int result = value1 | value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * OR H
     */
    private void Opcode0xB4() {
        int value1 = AF.hi.intValue()
        int value2 = HL.hi.intValue()
        int result = value1 | value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * OR L
     */
    private void Opcode0xB5() {
        int value1 = AF.hi.intValue()
        int value2 = HL.lo.intValue()
        int result = value1 | value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * OR (HL)
     */
    private void Opcode0xB6() {
        int value1 = AF.hi.intValue()
        int value2 = mmu().readByte(HL.word)
        int result = value1 | value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * OR A
     */
    private void Opcode0xB7() {
        int value1 = AF.hi.intValue()
        int value2 = AF.hi.intValue()
        int result = value1 | value2
        AF.hi = result

        AF.lo[Z] = result == 0x00
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 0

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * CP B
     */
    private void Opcode0xB8() {
        int value1 = AF.hi.intValue()
        int value2 = BC.hi.intValue()
        int difference = value1 - value2

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * CP C
     */
    private void Opcode0xB9() {
        int value1 = AF.hi.intValue()
        int value2 = BC.lo.intValue()
        int difference = value1 - value2

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * CP D
     */
    private void Opcode0xBA() {
        int value1 = AF.hi.intValue()
        int value2 = DE.hi.intValue()
        int difference = value1 - value2

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * CP E
     */
    private void Opcode0xBB() {
        int value1 = AF.hi.intValue()
        int value2 = DE.lo.intValue()
        int difference = value1 - value2

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * CP H
     */
    private void Opcode0xBC() {
        int value1 = AF.hi.intValue()
        int value2 = HL.hi.intValue()
        int difference = value1 - value2

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * CP L
     */
    private void Opcode0xBD() {
        int value1 = AF.hi.intValue()
        int value2 = HL.lo.intValue()
        int difference = value1 - value2

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * CP (HL)
     */
    private void Opcode0xBE() {
        int value1 = AF.hi.intValue()
        int value2 = mmu().readByte(HL.word)
        int difference = value1 - value2

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * CP A
     */
    private void Opcode0xBF() {
        int value1 = AF.hi.intValue()
        int value2 = AF.hi.intValue()
        int difference = value1 - value2

        AF.lo[Z] = difference == 0x00
        AF.lo[N] = 1
        AF.lo[H] = (difference & 0x0F) > (value1 & 0x0F)
        AF.lo[C] = difference < 0x00

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * RET NZ
     */
    private void Opcode0xC0() {
        if (AF.lo[Z] != 1) {
            PC.word = mmu().readWord(SP.word)
            SP.word += 2

            clock.m = 1
            clock.t = 20
        } else {
            clock.m = 1
            clock.t = 8

            PC.word++
        }
    }

    /**
     * POP BC
     */
    private void Opcode0xC1() {
        BC.word = mmu().readWord(SP.word)
        SP.word += 2

        clock.m = 1
        clock.t = 12

        PC.word++
    }

    /**
     * JP NZ,a16
     */
    private void Opcode0xC2() {
        if (AF.lo[Z] != 1) {
            UInt16 a16 = mmu().readWord(PC.word + 1)
            PC.word = a16

            clock.m = 3
            clock.t = 16
        } else {
            clock.m = 3
            clock.t = 12

            PC.word += 3
        }
    }

    /**
     * JP a16
     */
    private void Opcode0xC3() {
        UInt16 a16 = mmu().readWord(PC.word + 1)
        PC.word = a16

        clock.m = 3
        clock.t = 16
    }

    /**
     * CALL NZ,a16
     */
    private void Opcode0xC4() {
        if (AF.lo[Z] != 1) {
            UInt16 a16 = mmu().readWord(PC.word + 1)
            PC.word = a16

            clock.m = 3
            clock.t = 16
        } else {
            clock.m = 3
            clock.t = 12

            PC.word += 3
        }
    }

    /**
     * PUSH BC
     */
    private void Opcode0xC5() {
        SP.word -= 2
        mmu().writeWord(SP.word, BC.word)

        clock.m = 1
        clock.t = 16

        PC.word++
    }

    /**
     * ADD A,d8
     */
    private void Opcode0xC6() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        int value1 = AF.hi.intValue()
        int value2 = d8.intValue()
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 2
        clock.t = 8

        PC.word += 2
    }

    /**
     * RST 00H
     */
    private void Opcode0xC7() {
        SP.word--
        mmu().writeWord(SP.word, PC.word + 1)
        PC.word = 0x0000

        clock.m = 1
        clock.t = 16
    }

    /**
     * RET Z
     */
    private void Opcode0xC8() {
        if (AF.lo[Z] == 1) {
            PC.word = mmu().readWord(SP.word)
            SP.word += 2

            clock.m = 1
            clock.t = 20
        } else {
            clock.m = 1
            clock.t = 8

            PC.word++
        }
    }

    /**
     * RET
     */
    private void Opcode0xC9() {
        PC.word = mmu().readWord(SP.word)
        SP.word += 2

        clock.m = 1
        clock.t = 16
    }

    /**
     * JP Z,a16
     */
    private void Opcode0xCA() {
        if (AF.lo[Z] == 1) {
            UInt16 a16 = mmu().readWord(PC.word + 1)
            PC.word = a16

            clock.m = 3
            clock.t = 16
        } else {
            clock.m = 3
            clock.t = 12

            PC.word += 3
        }
    }

    /**
     * PREFIX CB
     */
    private void Opcode0xCB() {
        UInt8 cb = mmu().readByte(PC.word + 1)
        Opcode opcode = opcodesCB[cb.intValue()]
        if (opcode != null) {
            opcode.call()
        }
    }

    /**
     * CALL Z,a16
     */
    private void Opcode0xCC() {
        if (AF.lo[Z] == 1) {
            SP.word -= 2
            mmu().writeWord(SP.word, PC.word + 3)
            UInt16 a16 = mmu().readWord(PC.word + 1)
            PC.word = a16

            // todo: check the clock cycles here
            clock.m = 5
            clock.t = 24
        } else {
            clock.m = 3
            clock.t = 12
        }
    }

    /**
     * CALL a16
     */
    private void Opcode0xCD() {
        SP.word -= 2
        mmu().writeWord(SP.word, PC.word + 3)
        UInt16 a16 = mmu().readWord(PC.word + 1)
        PC.word = a16

        clock.m = 3
        clock.t = 24
    }

    /**
     * ADC A,d8
     */
    private void Opcode0xCE() {
        int bitC = AF.lo[C]
        UInt8 d8 = mmu().readByte(PC.word + 1)
        int value1 = AF.hi.intValue()
        int value2 = d8.intValue() + bitC
        int sum = value1 + value2
        AF.hi = sum

        AF.lo[Z] = sum == 0x00
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 2
        clock.t = 4

        PC.word += 2
    }

    /**
     * RST 08H
     */
    private void Opcode0xCF() {
        SP.word--
        mmu().writeWord(SP.word, PC.word + 1)
        PC.word = 0x0008

        clock.m = 1
        clock.t = 16
    }

    static void main(String[] args) {
        GbcCore core = new GbcCore()
        int length = 0xD0
        long start = System.currentTimeMillis()
        for (int i = 0; i < length; i++) {
            core.cpu().opcodes[i]()
        }
        long stop = System.currentTimeMillis()
        println(stop - start)
        println((stop - start) / length)
    }
}
