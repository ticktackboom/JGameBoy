package com.meadowsapps.jgameboy.gbc.core.element;

import com.meadowsapps.jgameboy.core.util.Range;

/**
 * Created by dmeadows on 3/1/2017.
 */
public interface GbcMemoryMap {

    Range BIOS = new Range(0x0000, 0x0100);

    Range ROM = new Range(0x0000, 0x7FFF);

    Range ROM_0 = new Range(0x0000, 0x3FFF);

    Range ROM_N = new Range(0x4000, 0x7FFF);

    Range VRAM = new Range(0x8000, 0x9FFF);

    Range TILE_DATA_1 = new Range(0x8000, 0x8FFF);

    Range TILE_DATA_0 = new Range(0x8800, 0x97FF);

    Range TILE_MAP_0 = new Range(0x9800, 0x9BFF);

    Range TILE_MAP_1 = new Range(0x9C00, 0x9FFF);

    Range ERAM = new Range(0xA000, 0xBFFF);

    Range RAM = new Range(0xC000, 0xDFFF);

    Range RAM_0 = new Range(0xC000, 0xCFFF);

    Range RAM_1 = new Range(0xD000, 0xDFFF);

    Range ECHO = new Range(0xE000, 0xFE9F);

    Range OAM = new Range(0xFE00, 0xFEFF);

    Range UNUSED = new Range(0xFEA0, 0xFEFF);

    Range IO = new Range(0xFF00, 0xFF7F);

    int JOYP = 0xFF00;

    int SB = 0xFF01;

    int SC = 0xFF02;

    int DIV = 0xFF04;

    int TIMA = 0xFF05;

    int TMA = 0xFF06;

    int TAC = 0xFF07;

    int IF = 0xFF0F;

    int NR10 = 0xFF10;

    int NR11 = 0xFF11;

    int NR12 = 0xFF12;

    int NR13 = 0xFF13;

    int NR14 = 0xFF14;

    int NR21 = 0xFF16;

    int NR22 = 0xFF17;

    int NR23 = 0xFF18;

    int NR24 = 0xFF19;

    int NR30 = 0xFF1A;

    int NR31 = 0xFF1B;

    int NR32 = 0xFF1C;

    int NR33 = 0xFF1D;

    int NR34 = 0xFF1E;

    int NR41 = 0xFF20;

    int NR42 = 0xFF21;

    int NR43 = 0xFF22;

    int NR44 = 0xFF23;

    int NR50 = 0xFF24;

    int NR51 = 0xFF25;

    int NR52 = 0xFF26;

    Range WAVE_PATTERN_RAM = new Range(0xFF30, 0xFF3F);

    int LCDC = 0xFF40;

    int STAT = 0xFF41;

    int SCY = 0xFF42;

    int SCX = 0xFF43;

    int LY = 0xFF44;

    int LYC = 0xFF45;

    int DMA = 0xFF46;

    int BGP = 0xFF47;

    int OBP0 = 0xFF48;

    int OBP1 = 0xFF49;

    int WY = 0xFF4A;

    int KEY1 = 0xFF4D;

    int WX = 0xFF4B;

    int VBK = 0xFF4F;

    int BOOT = 0xFF50;

    int HDMA1 = 0xFF51;

    int HDMA2 = 0xFF52;

    int HDMA3 = 0xFF53;

    int HDMA4 = 0xFF54;

    int HDMA5 = 0xFF55;

    int RP = 0xFF56;

    int BGPI = 0xFF68;

    int BGPD = 0xFF69;

    int OBPI = 0xFF6A;

    int OBPD = 0xFF6B;

    int SVBK = 0xFF70;

    Range HRAM = new Range(0xFF80, 0xFFFE);

    int IE = 0xFFFF;

}
