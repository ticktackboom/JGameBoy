package com.meadowsapps.jgameboy;

/**
 * Created by Dylan on 1/6/17.
 */
public interface Constants {
    /**
     * The total number of registers
     */
    int REGISTER_COUNT = 9;
    /**
     * Register A - 8-Bit
     */
    int A = 0;
    /**
     * Register B - 8-Bit
     */
    int B = 1;
    /**
     * Register C - 8-Bit
     */
    int C = 2;
    /**
     * Register D - 8-Bit
     */
    int D = 3;
    /**
     * Register E - 8-Bit
     */
    int E = 4;
    /**
     * Register F - 8-Bit
     */
    int F = 5;
    /**
     * Register SP - 16-Bit
     */
    int SP = 6;
    /**
     * Register PC - 16-Bit
     */
    int PC = 7;
    /**
     * Register HL - 16-Bit
     */
    int HL = 8;

    /**
     * Zero flag
     */
    short F_ZERO = 0x80;
    /**
     * Subtract/negative flag
     */
    short F_SUBTRACT = 0x40;
    /**
     * Half carry flag
     */
    short F_HALFCARRY = 0x20;
    /**
     * Carry flag
     */
    short F_CARRY = 0x10;

    /**
     * Vertical blank interrupt
     */
    short INT_VBLANK = 0x01;
    /**
     * LCD Coincidence interrupt
     */
    short INT_LCDC = 0x02;
    /**
     * TIMA (programmable timer) interrupt
     */
    short INT_TIMA = 0x04;
    /**
     * Serial interrupt
     */
    short INT_SER = 0x08;
    /**
     * P10 - P13 (Joypad) interrupt
     */
    short INT_P10 = 0x10;
}
