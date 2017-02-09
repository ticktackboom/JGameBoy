package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Constants;

/**
 * Created by dmeadows on 2/9/2017.
 */
public interface GbcConstants extends Constants {

    int V_BLANK_IRQ = 0x01;
    int LCD_IRQ = 0x02;
    int TIMER_OVERFLOW_IRQ = 0x04;
    int JOYPAD_HILO_IRQ = 0x10;

    int V_BLANK_IR = 0x40;
    int LCD_IR = 0x48;
    int TIMER_OVERFLOW_IR = 0x50;
    int JOYPAD_HILO_IR = 0x60;



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

    /* END MEMORY MAP */


    int JOYPAD = 0xFF00;
    int INTERRUPT_ENABLED_FLAG = 0xFFFF;
    int INTERRUPT_FLAG = 0xFF0F;



    /* GPU */

    int TILE_MAP_0 = 0x9800;
    int TILE_MAP_1 = 0x9C00;
    int TILE_DATA_0 = 0x8800;
    int TILE_DATA_1 = 0x8000;

    int LCDC = 0xFF40;
    int STAT = 0xFF41;
    int SCROLL_Y = 0xFF42;
    int SCROLL_X = 0xFF43;
    int LY = 0xFF44;
    int LYC = 0xFF45;
    int BGP = 0xFF47;
    int OBJECT_PALETTE_0 = 0xFF48;
    int OBJECT_PALETTE_1 = 0xFFf9;
    int WX = 0xFF4B;
    int WY = 0xFF4A;

    int H_BLANK = 0x00;
    int V_BLANK = 0x01;
    int OAM_READ = 0x02;
    int VRAM_READ = 0x03;
    int SPRITE_8x8_MODE = 1;
    int SPRITE_8x16_MODE = 0;

    /* END GPU */
}
