package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Constants;

/**
 * Created by dmeadows on 2/9/2017.
 */
public interface GbcConstants extends Constants {

    int VBLANK_IRQ = 0x01;
    int LCD_IRQ = 0x02;
    int TIMER_OVERFLOW_IRQ = 0x04;
    int SERIAL_IRQ = 0x08;
    int P10_IRQ = 0x10;

    int VBLANK_IR = 0x40;
    int LCD_IR = 0x48;
    int TIMER_OVERFLOW_IR = 0x50;
    int SERIAL_IR = 0x58;
    int P10_IR = 0x60;

    int WIDTH = 160;
    int HEIGHT = 144;



    /* MEMORY MAP */

    int CARTRIDGE = 0x0000;
    int VIDEO_RAM = 0x8000;
    int EXTERNAL_RAM = 0xA000;
    int WORKING_RAM = 0xC000;
    int WORKING_ECHO = 0xE000;
    int OAM = 0xFE00;
    int UNUSED = 0xFEA0;
    int HARDWARE_IO = 0xFF00;
    int HIGH_RAM = 0xFF80;
    int INTERRUPT = 0xFFFF;

    int JOYP = 0xFF00;
    int SB = 0xFF01;
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
    int WX = 0xFF4B;
    int VBK = 0xFF4F;
    int BOOT = 0xFF50;
    int HDMA1 = 0xFF51;
    int HDMA2 = 0xFF52;
    int HDMA3 = 0xFF53;
    int HDMA4 = 0xFF54;
    int HDMA5 = 0xFF55;
    int BGPI = 0xFF68;
    int BGPD = 0xFF69;
    int OBPI = 0xFF6A;
    int OBPD = 0xFF6B;
    int SVBK = 0xFF70;

    /* END MEMORY MAP */

    int IE_FLAG = 0xFFFF;

    int TILE_MAP_0 = 0x9800;
    int TILE_MAP_1 = 0x9C00;
    int TILE_DATA_0 = 0x8800;
    int TILE_DATA_1 = 0x8000;

    int WINDOW_ON = 0x20;
    int DISPLAY_ON = 0x80;
    int BACKGROUND_ON = 0x01;
    int SPRITES_ON = 0x02;

    int TILE_BKG = 0x00;
    int TILE_OBJ1 = 0x04;
    int TILE_OBJ2 = 0x08;
    int TILE_FLIPX = 0x01;
    int TILE_FLIPY = 0x02;

    int HBLANK = 0x00;
    int VBLANK = 0x01;
    int OAM_READ = 0x02;
    int VRAM_READ = 0x03;
    int SPRITE_8x8_MODE = 0x01;
    int SPRITE_8x16_MODE = 0x00;

    /* END GPU */
}
