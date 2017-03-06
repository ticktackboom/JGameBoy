package com.meadowsapps.jgameboy.gbc

import com.meadowsapps.jgameboy.core.util.Register16Bit
import com.meadowsapps.jgameboy.core.util.UInt16
import com.meadowsapps.jgameboy.core.util.UInt8

/**
 * Created by dmeadows on 3/5/17.
 */
class GbcCpu {

    private Register16Bit AF

    private Register16Bit BC

    private Register16Bit DE

    private Register16Bit HL

    private Register16Bit SP

    private Register16Bit PC

    private Clock clock

    void execute() {
        UInt8 opcode = new UInt8() // mmu().readByte(PC.word)
        UInt8 operand1 = new UInt8() // mmu().readByte(PC.word + 1)
        UInt8 operand2 = new UInt8() // mmu().readByte(PC.word + 2)
        UInt16 d16 = UInt16.combine(operand2, operand1)

        switch (opcode.intValue()) {
        // NOP
            case 0x00:
                break
        // LD BC,d16
            case 0x01:
                BC.word = d16
                break
        // LD (BC),A
            case 0x02:

                break
        }

    }

}
