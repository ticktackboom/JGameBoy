package com.meadowsapps.jgameboy.gbc

import com.meadowsapps.jgameboy.core.util.Register16Bit
import com.meadowsapps.jgameboy.core.util.UInt8
import groovy.lang.Closure as Opcode
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Created by dmeadows on 3/5/17.
 */
@InheritConstructors
class GbcCpu extends GbcCoreElement {

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
        BC.word = mmu().readWord(PC.word + 1)
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
        clock.t = 8
        PC.word++
    }

    /**
     * LD B,d8
     */
    private void Opcode0x06() {
        BC.hi = mmu().readByte(PC.word + 1)
        clock.m = 2
        clock.t = 8
        PC.word += 2
    }

    static void main(String[] args) {
        GbcCore core = new GbcCore()
        int length = 7
        long start = System.currentTimeMillis()
        for (int i = 0; i < length; i++) {
            core.cpu().table[i].call()
        }
        long stop = System.currentTimeMillis()
        println((stop - start) / length)
    }
}
