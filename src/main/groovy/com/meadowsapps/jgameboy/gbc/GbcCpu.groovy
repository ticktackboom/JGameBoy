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

    private Opcode[] opcodes

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

        long start = System.currentTimeMillis()
        opcodes = new Opcode[0x100]
        opcodes[0x00] = this.&Opcode0x00
        opcodes[0x01] = this.&Opcode0x01
        opcodes[0x02] = this.&Opcode0x02
        opcodes[0x03] = this.&Opcode0x03
        opcodes[0x04] = this.&Opcode0x04
        opcodes[0x05] = this.&Opcode0x05
        opcodes[0x06] = this.&Opcode0x06
        opcodes[0x07] = this.&Opcode0x07
        opcodes[0x08] = this.&Opcode0x08
        opcodes[0x09] = this.&Opcode0x09
        opcodes[0x0A] = this.&Opcode0x0A
        opcodes[0x0B] = this.&Opcode0x0B
        opcodes[0x0C] = this.&Opcode0x0C
        opcodes[0x0D] = this.&Opcode0x0D
        opcodes[0x0E] = this.&Opcode0x0E
        opcodes[0x0F] = this.&Opcode0x0F
        opcodes[0x10] = this.&Opcode0x10
        opcodes[0x11] = this.&Opcode0x11
        opcodes[0x12] = this.&Opcode0x12
        opcodes[0x13] = this.&Opcode0x13
        opcodes[0x14] = this.&Opcode0x14
        opcodes[0x15] = this.&Opcode0x15
        opcodes[0x16] = this.&Opcode0x16
        opcodes[0x17] = this.&Opcode0x17
        opcodes[0x18] = this.&Opcode0x18
        opcodes[0x19] = this.&Opcode0x19
        opcodes[0x1A] = this.&Opcode0x1A
        opcodes[0x1B] = this.&Opcode0x1B
        opcodes[0x1C] = this.&Opcode0x1C
        opcodes[0x1D] = this.&Opcode0x1D
        opcodes[0x1E] = this.&Opcode0x1E
        opcodes[0x1F] = this.&Opcode0x1F
        opcodes[0x20] = this.&Opcode0x20
        opcodes[0x21] = this.&Opcode0x21
        opcodes[0x22] = this.&Opcode0x22
        opcodes[0x23] = this.&Opcode0x23
        opcodes[0x24] = this.&Opcode0x24
        opcodes[0x25] = this.&Opcode0x25
        opcodes[0x26] = this.&Opcode0x26
        opcodes[0x27] = this.&Opcode0x27
        opcodes[0x28] = this.&Opcode0x28
        opcodes[0x29] = this.&Opcode0x29
        opcodes[0x2A] = this.&Opcode0x2A
        opcodes[0x2B] = this.&Opcode0x2B
        opcodes[0x2C] = this.&Opcode0x2C
        opcodes[0x2D] = this.&Opcode0x2D
        opcodes[0x2E] = this.&Opcode0x2E
        opcodes[0x2F] = this.&Opcode0x2F
        opcodes[0x30] = this.&Opcode0x30
        opcodes[0x31] = this.&Opcode0x31
        opcodes[0x32] = this.&Opcode0x32
        opcodes[0x33] = this.&Opcode0x33
        opcodes[0x34] = this.&Opcode0x34
        opcodes[0x35] = this.&Opcode0x35
        opcodes[0x36] = this.&Opcode0x36
        opcodes[0x37] = this.&Opcode0x37
        opcodes[0x38] = this.&Opcode0x38
        opcodes[0x39] = this.&Opcode0x39
        opcodes[0x3A] = this.&Opcode0x3A
        opcodes[0x3B] = this.&Opcode0x3B
        opcodes[0x3C] = this.&Opcode0x3C
        opcodes[0x3D] = this.&Opcode0x3D
        opcodes[0x3E] = this.&Opcode0x3E
        opcodes[0x3F] = this.&Opcode0x3F
        opcodes[0x40] = this.&Opcode0x40
        opcodes[0x41] = this.&Opcode0x41
        opcodes[0x42] = this.&Opcode0x42
        opcodes[0x43] = this.&Opcode0x43
        opcodes[0x44] = this.&Opcode0x44
        opcodes[0x45] = this.&Opcode0x45
        opcodes[0x46] = this.&Opcode0x46
        opcodes[0x47] = this.&Opcode0x47
        opcodes[0x48] = this.&Opcode0x48
        opcodes[0x49] = this.&Opcode0x49
        opcodes[0x4A] = this.&Opcode0x4A
        opcodes[0x4B] = this.&Opcode0x4B
        opcodes[0x4C] = this.&Opcode0x4C
        opcodes[0x4D] = this.&Opcode0x4D
        opcodes[0x4E] = this.&Opcode0x4E
        opcodes[0x4F] = this.&Opcode0x4F
        opcodes[0x50] = this.&Opcode0x50
        opcodes[0x51] = this.&Opcode0x51
        opcodes[0x52] = this.&Opcode0x52
        opcodes[0x53] = this.&Opcode0x53
        opcodes[0x54] = this.&Opcode0x54
        opcodes[0x55] = this.&Opcode0x55
        opcodes[0x56] = this.&Opcode0x56
        opcodes[0x57] = this.&Opcode0x57
        opcodes[0x58] = this.&Opcode0x58
        opcodes[0x59] = this.&Opcode0x59
        opcodes[0x5A] = this.&Opcode0x5A
        opcodes[0x5B] = this.&Opcode0x5B
        opcodes[0x5C] = this.&Opcode0x5C
        opcodes[0x5D] = this.&Opcode0x5D
        opcodes[0x5E] = this.&Opcode0x5E
        opcodes[0x5F] = this.&Opcode0x5F
        opcodes[0x60] = this.&Opcode0x60
        opcodes[0x61] = this.&Opcode0x61
        opcodes[0x62] = this.&Opcode0x62
        opcodes[0x63] = this.&Opcode0x63
        opcodes[0x64] = this.&Opcode0x64
        opcodes[0x65] = this.&Opcode0x65
        opcodes[0x66] = this.&Opcode0x66
        opcodes[0x67] = this.&Opcode0x67
        opcodes[0x68] = this.&Opcode0x68
        opcodes[0x69] = this.&Opcode0x69
        opcodes[0x6A] = this.&Opcode0x6A
        opcodes[0x6B] = this.&Opcode0x6B
        opcodes[0x6C] = this.&Opcode0x6C
        opcodes[0x6D] = this.&Opcode0x6D
        opcodes[0x6E] = this.&Opcode0x6E
        opcodes[0x6F] = this.&Opcode0x6F
        opcodes[0x70] = this.&Opcode0x70
        opcodes[0x71] = this.&Opcode0x71
        opcodes[0x72] = this.&Opcode0x72
        opcodes[0x73] = this.&Opcode0x73
        opcodes[0x74] = this.&Opcode0x74
        opcodes[0x75] = this.&Opcode0x75
        opcodes[0x76] = this.&Opcode0x76
        opcodes[0x77] = this.&Opcode0x77
        opcodes[0x78] = this.&Opcode0x78
        opcodes[0x79] = this.&Opcode0x79
        opcodes[0x7A] = this.&Opcode0x7A
        opcodes[0x7B] = this.&Opcode0x7B
        opcodes[0x7C] = this.&Opcode0x7C
        opcodes[0x7D] = this.&Opcode0x7D
        opcodes[0x7E] = this.&Opcode0x7E
        opcodes[0x7F] = this.&Opcode0x7F
        opcodes[0x80] = this.&Opcode0x80
        opcodes[0x81] = this.&Opcode0x81
        opcodes[0x82] = this.&Opcode0x82
        opcodes[0x83] = this.&Opcode0x83
        opcodes[0x84] = this.&Opcode0x84
        opcodes[0x85] = this.&Opcode0x85
        opcodes[0x86] = this.&Opcode0x86
        opcodes[0x87] = this.&Opcode0x87
        opcodes[0x88] = this.&Opcode0x88
        opcodes[0x89] = this.&Opcode0x89
        opcodes[0x8A] = this.&Opcode0x8A
        opcodes[0x8B] = this.&Opcode0x8B
        opcodes[0x8C] = this.&Opcode0x8C
        opcodes[0x8D] = this.&Opcode0x8D
        opcodes[0x8E] = this.&Opcode0x8E
        opcodes[0x8F] = this.&Opcode0x8F
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

    /**
     * INC SP
     */
    private void Opcode0x33() {
        SP.word++

        clock.m = 2
        clock.t = 8

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

        clock.m = 3
        clock.t = 12

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

        clock.m = 3
        clock.t = 12

        PC.word++
    }

    /**
     * LD (HL),d8
     */
    private void Opcode0x36() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        mmu().writeByte(HL.word, d8)

        clock.m = 2
        clock.t = 12

        PC.word += 2
    }

    /**
     * SCF
     */
    private void Opcode0x37() {
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = 1

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * JR C,r8
     */
    private void Opcode0x38() {
        if (AF.lo[C] == 1) {
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
     * ADD HL,SP
     */
    private void Opcode0x39() {
        int hl = HL.word.intValue()
        int sp = SP.word.intValue()
        HL.word += SP.word

        AF.lo[N] = 0
        AF.lo[H] = (hl & 0x0FFF) + (sp & 0x0FFF) > 0x0FFF
        AF.lo[C] = HL.word.intValue() > 0xFFFF

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * LD A,(HL-)
     */
    private void Opcode0x3A() {
        AF.hi = mmu().readByte(HL.word)
        HL.word--

        clock.m = 1
        clock.t = 8
    }

    /**
     * DEC SP
     */
    private void Opcode0x3B() {
        SP.word--

        clock.m = 2
        clock.t = 8

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

        clock.m = 1
        clock.t = 4

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

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD A,d8
     */
    private void Opcode0x3E() {
        UInt8 d8 = mmu().readByte(PC.word + 1)
        AF.hi = d8

        clock.m = 2
        clock.t = 8

        PC.word += 2
    }

    /**
     * CCF
     */
    private void Opcode0x3F() {
        AF.lo[N] = 0
        AF.lo[H] = 0
        AF.lo[C] = ~AF.lo[C]

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD B,B
     */
    private void Opcode0x40() {
        BC.hi = BC.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD B,C
     */
    private void Opcode0x41() {
        BC.hi = BC.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD B,D
     */
    private void Opcode0x42() {
        BC.hi = DE.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD B,E
     */
    private void Opcode0x43() {
        BC.hi = DE.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD B,H
     */
    private void Opcode0x44() {
        BC.hi = HL.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD B,L
     */
    private void Opcode0x45() {
        BC.hi = HL.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD B,(HL)
     */
    private void Opcode0x46() {
        BC.hi = mmu().readByte(HL.word)

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD B,A
     */
    private void Opcode0x47() {
        BC.hi = AF.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD C,B
     */
    private void Opcode0x48() {
        BC.lo = BC.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD C,C
     */
    private void Opcode0x49() {
        BC.lo = BC.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD C,D
     */
    private void Opcode0x4A() {
        BC.lo = DE.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD C,E
     */
    private void Opcode0x4B() {
        BC.lo = DE.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD C,H
     */
    private void Opcode0x4C() {
        BC.lo = HL.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD C,L
     */
    private void Opcode0x4D() {
        BC.lo = HL.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD C,(HL)
     */
    private void Opcode0x4E() {
        BC.lo = mmu().readByte(HL.word)

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD C,A
     */
    private void Opcode0x4F() {
        BC.lo = AF.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD D,B
     */
    private void Opcode0x50() {
        DE.hi = BC.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD D,C
     */
    private void Opcode0x51() {
        DE.hi = BC.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD D,D
     */
    private void Opcode0x52() {
        DE.hi = DE.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD D,E
     */
    private void Opcode0x53() {
        DE.hi = DE.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD D,H
     */
    private void Opcode0x54() {
        DE.hi = HL.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD D,L
     */
    private void Opcode0x55() {
        DE.hi = HL.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD D,(HL)
     */
    private void Opcode0x56() {
        DE.hi = mmu().readByte(HL.word)

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD D,A
     */
    private void Opcode0x57() {
        DE.hi = AF.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD E,B
     */
    private void Opcode0x58() {
        DE.lo = BC.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD E,C
     */
    private void Opcode0x59() {
        DE.lo = BC.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD E,D
     */
    private void Opcode0x5A() {
        DE.lo = DE.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD E,E
     */
    private void Opcode0x5B() {
        DE.lo = DE.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD E,H
     */
    private void Opcode0x5C() {
        DE.lo = HL.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD E,L
     */
    private void Opcode0x5D() {
        DE.lo = HL.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD E,(HL)
     */
    private void Opcode0x5E() {
        DE.lo = mmu().readByte(HL.word)

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD E,A
     */
    private void Opcode0x5F() {
        DE.lo = AF.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD H,B
     */
    private void Opcode0x60() {
        HL.hi = BC.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD H,C
     */
    private void Opcode0x61() {
        HL.hi = BC.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD H,D
     */
    private void Opcode0x62() {
        HL.hi = DE.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD H,E
     */
    private void Opcode0x63() {
        HL.hi = DE.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD H,H
     */
    private void Opcode0x64() {
        HL.hi = HL.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD H,L
     */
    private void Opcode0x65() {
        HL.hi = HL.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD H,(HL)
     */
    private void Opcode0x66() {
        HL.hi = mmu().readByte(HL.word)

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD H,A
     */
    private void Opcode0x67() {
        HL.hi = AF.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD L,B
     */
    private void Opcode0x68() {
        HL.lo = BC.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD L,C
     */
    private void Opcode0x69() {
        HL.lo = BC.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD L,D
     */
    private void Opcode0x6A() {
        HL.lo = DE.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD L,E
     */
    private void Opcode0x6B() {
        HL.lo = DE.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD L,H
     */
    private void Opcode0x6C() {
        HL.lo = HL.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD L,L
     */
    private void Opcode0x6D() {
        HL.lo = HL.lo

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD L,(HL)
     */
    private void Opcode0x6E() {
        HL.lo = mmu().readByte(HL.word)

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD L,A
     */
    private void Opcode0x6F() {
        HL.lo = AF.hi

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    /**
     * LD (HL),B
     */
    private void Opcode0x70() {
        mmu().writeByte(HL.word, BC.hi)

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * LD (HL),C
     */
    private void Opcode0x71() {
        mmu().writeByte(HL.word, BC.lo)

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * LD (HL),D
     */
    private void Opcode0x72() {
        mmu().writeByte(HL.word, DE.hi)

        clock.m = 1
        clock.t = 8

        PC.word++
    }

    /**
     * LD (HL),E
     */
    private void Opcode0x73() {
        mmu().writeByte(HL.word, DE.lo)

        clock.m = 1
        clock.t = 8

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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
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

        AF.lo[Z] = sum == 0
        AF.lo[N] = 0
        AF.lo[H] = (value1 & 0x0F) + (value2 & 0x0F) > 0x0F
        AF.lo[C] = sum > 0xFF

        clock.m = 1
        clock.t = 4

        PC.word++
    }

    static void main(String[] args) {
        GbcCore core = new GbcCore()
        int length = 0x90
        long start = System.currentTimeMillis()
        for (int i = 0; i < length; i++) {
            core.cpu().opcodes[i]()
        }
        long stop = System.currentTimeMillis()
        println(stop - start)
        println((stop - start) / length)
    }
}
