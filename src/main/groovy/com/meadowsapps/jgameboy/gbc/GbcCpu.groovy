package com.meadowsapps.jgameboy.gbc

import com.meadowsapps.jgameboy.core.util.Register16Bit
import com.meadowsapps.jgameboy.core.util.UInt16
import com.meadowsapps.jgameboy.core.util.UInt8
import groovy.lang.Closure as Opcode
import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/5/17.
 */
@InheritConstructors
class GbcCpu extends GbcCoreElement {

    private boolean debug = true

    private Register16Bit AF

    private Register16Bit BC

    private Register16Bit DE

    private Register16Bit HL

    private Register16Bit SP

    private Register16Bit PC

    private Opcode[] table

    private Clock clock

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
        clock = new Clock()

        table = new Opcode[0xFF]
        table[0x00] = this.&Opcode0x00
        table[0x01] = this.&Opcode0x01
        table[0x02] = this.&Opcode0x02
        table[0x03] = this.&Opcode0x03
        table[0x04] = this.&Opcode0x04
        table[0x05] = this.&Opcode0x05
        table[0x06] = this.&Opcode0x06
        table[0x07] = this.&Opcode0x07
        table[0x08] = this.&Opcode0x08
        table[0x09] = this.&Opcode0x09
        table[0x0A] = this.&Opcode0x0A
        table[0x0B] = this.&Opcode0x0B
        table[0x0C] = this.&Opcode0x0C
        table[0x0D] = this.&Opcode0x0D
        table[0x0E] = this.&Opcode0x0E
        table[0x0F] = this.&Opcode0x0F
        table[0x10] = this.&Opcode0x10
        table[0x11] = this.&Opcode0x11
        table[0x12] = this.&Opcode0x12
        table[0x13] = this.&Opcode0x13
        table[0x14] = this.&Opcode0x14
        table[0x15] = this.&Opcode0x15
        table[0x16] = this.&Opcode0x16
        table[0x17] = this.&Opcode0x17
        table[0x18] = this.&Opcode0x18
        table[0x19] = this.&Opcode0x19
        table[0x1A] = this.&Opcode0x1A
        table[0x1B] = this.&Opcode0x1B
        table[0x1C] = this.&Opcode0x1C
        table[0x1D] = this.&Opcode0x1D
        table[0x1E] = this.&Opcode0x1E
        table[0x1F] = this.&Opcode0x1F
        table[0x20] = this.&Opcode0x20
        table[0x21] = this.&Opcode0x21
        table[0x22] = this.&Opcode0x22
        table[0x23] = this.&Opcode0x23
        table[0x24] = this.&Opcode0x24
        table[0x25] = this.&Opcode0x25
        table[0x26] = this.&Opcode0x26
        table[0x27] = this.&Opcode0x27
        table[0x28] = this.&Opcode0x28
        table[0x29] = this.&Opcode0x29
        table[0x2A] = this.&Opcode0x2A
        table[0x2B] = this.&Opcode0x2B
        table[0x2C] = this.&Opcode0x2C
        table[0x2D] = this.&Opcode0x2D
        table[0x2E] = this.&Opcode0x2E
        table[0x2F] = this.&Opcode0x2F

    }

    void execute() {
        UInt8 opcode = mmu().readByte(PC.word)
        table[opcode.intValue()].call()
    }

    /**
     * NOP
     */
    private void Opcode0x00() {
        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD BC,d16
     */
    private void Opcode0x01() {
        UInt16 d16 = mmu().readWord(PC.word + 1)
        BC.word = d16

        clock.m = 3
        clock.t = 12

        PC.word += 3
    }

    /**
     * LD (BC),A
     */
    private void Opcode0x02() {
        mmu().writeByte(BC.word, AF.hi)

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * INC BC
     */
    private void Opcode0x03() {
        BC.word++

        clock.m = 1
        clock.t = 8

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

        clock.m = 1
        clock.t = 4

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD B,d8
     */
    private void Opcode0x06() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        BC.hi = d8

        clock.m = 2
        clock.t = 8

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD (a16),SP
     */
    private void Opcode0x08() {
        UInt16 a16 = mmu().readWord(PC.word + 1)
        mmu().writeWord(a16, SP.word)

        // todo: is this machine cycle correct?
        clock.m = 4
        clock.t = 20

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

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * LD A,(BC)
     */
    private void Opcode0x0A() {
        AF.hi = mmu().readByte(BC.word)

        clock.m = 3
        clock.t = 8

        PC.word++
    }

    /**
     * DEC BC
     */
    private void Opcode0x0B() {
        BC.word--

        clock.m = 1
        clock.t = 4

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

        clock.m = 1
        clock.t = 4

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD C,d8
     */
    private void Opcode0x0E() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        BC.lo = d8

        clock.m = 2
        clock.t = 8

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * STOP 0
     */
    private void Opcode0x10() {
        // todo: ensure this is correct
        if (!debug)
            while (AF.hi.equals(0x00) || mmu().readByte(0xFF00).equals(0x00)); ;

        clock.m = 1
        clock.t = 4

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

        clock.m = 3
        clock.t = 12

        PC.word += 3
    }

    /**
     * LD (DE),A
     */
    private void Opcode0x12() {
        mmu().writeByte(DE.word, AF.hi)

        clock.m = 2
        clock.t = 8

        PC.word++
    }

    /**
     * INC DE
     */
    private void Opcode0x13() {
        DE.word++

        clock.m = 1
        clock.t = 8

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

        clock.m = 1
        clock.t = 4

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD D,d8
     */
    private void Opcode0x16() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        BC.hi = d8

        clock.m = 2
        clock.t = 4

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

        PC.word++
    }

    /**
     * JR r8
     */
    private void Opcode0x18() {
        byte r8 = mmu().readByte(PC.word + 1).byteValue()
        PC.word += r8

        clock.m = 3
        clock.t = 4

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

        PC.word++
    }

    /**
     * LD A,(DE)
     */
    private void Opcode0x1A() {
        AF.hi = mmu().readByte(DE.word)

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * DEC DE
     */
    private void Opcode0x1B() {
        DE.word--

        clock.m = 2
        clock.t = 8

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

        clock.m = 1
        clock.t = 4

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD E,d8
     */
    private void Opcode0x1E() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        DE.lo = d8

        clock.m = 2
        clock.t = 8

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * JR NZ,r8
     */
    private void Opcode0x20() {
        if (AF.lo[Z] != 1) {
            byte r8 = mmu().readByte(PC.word + 1).byteValue()
            PC.word += r8

            clock.t = 12
        } else {
            clock.t = 8
        }
        clock.m = 2

        PC.word += 2
    }

    /**
     * LD HL,d16
     */
    private void Opcode0x21() {
        UInt16 d16 = mmu().readWord(PC.word + 1)
        HL.word = d16

        clock.m = 3
        clock.t = 12

        PC.word += 3
    }

    /**
     * LD (HL+),A
     */
    private void Opcode0x22() {
        mmu().writeByte(HL.word, AF.hi)
        HL.word++

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * INC HL
     */
    private void Opcode0x23() {
        HL.word++

        clock.m = 1
        clock.t = 4

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

        clock.m = 1
        clock.t = 4

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD H,d8
     */
    private void Opcode0x26() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        HL.hi = d8

        clock.m = 2
        clock.t = 8

        PC.word += 2
    }

    /**
     * DAA
     */
    private void Opcode0x27() {
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
    }

    /**
     * JR Z,r8
     */
    private void Opcode0x28() {
        if (AF.lo[Z] == 1) {
            byte r8 = mmu().readByte(PC.word + 1).byteValue()
            PC.word += r8

            clock.t = 12
        } else {
            clock.t = 8
        }
        clock.m = 2

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

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * LD A,(HL+)
     */
    private void Opcode0x2A() {
        AF.hi = mmu().readByte(HL.word)
        HL.word++

        clock.m = 2
        clock.t = 8

        PC.word++
    }

    /**
     * DEC HL
     */
    private void Opcode0x2B() {
        HL.word--

        clock.m = 1
        clock.t = 8

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

        clock.m = 1
        clock.t = 4

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD L,d8
     */
    private void Opcode0x2E() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        HL.lo = d8

        clock.m = 2
        clock.t = 8

        PC.word += 2
    }

    /**
     * CPL
     */
    private void Opcode0x2F() {
        AF.hi = ~AF.hi

        AF.lo[N] = 1
        AF.lo[H] = 1

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * JR NC,r8
     */
    private void Opcode0x30() {
        if (AF.lo[C] != 1) {
            byte r8 = mmu().readByte(PC.word + 1).byteValue()
            PC.word += r8

            clock.t = 12
        } else {
            clock.t = 8
        }

        clock.m = 3

        PC.word += 2
    }

    /**
     * LD SP,d16
     */
    private void Opcode0x31() {
        UInt16 d16 = mmu().readWord(PC.word + 1)
        SP.word = d16

        clock.m = 3
        clock.t = 12

        PC.word += 3
    }

    /**
     * LD (HL-),A
     */
    private void Opcode0x32() {
        mmu().writeByte(HL.word, AF.hi)
        HL.word--

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    static void main(String[] args) {
        GbcCore core = new GbcCore()
        int length = 0x2F
        long start = System.currentTimeMillis()
        for (int i = 0; i < length; i++) {
            core.cpu().table[i]()
        }
        long stop = System.currentTimeMillis()
        println(stop - start)
        println((stop - start) / length)
    }
}
