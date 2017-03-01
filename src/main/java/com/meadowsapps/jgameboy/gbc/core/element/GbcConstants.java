package com.meadowsapps.jgameboy.gbc.core.element;

import com.meadowsapps.jgameboy.core.util.Constants;

/**
 * Created by dmeadows on 2/9/2017.
 */
public interface GbcConstants extends Constants {

    int WIDTH = 160;

    int HEIGHT = 144;

    int V_BLANK_INTERRUPT = 0x01;

    int LCDC_STATUS_INTERRUPT = 0x02;

    int TIMER_INTERRUPT = 0x04;

    int SERIAL_INTERRUPT = 0x08;

    int JOYPAD_INTERRUPT = 0x10;

    /* V-Blank Interrupt */
    int INT40 = 0x40;

    /* LCDC Status Interrupt */
    int INT48 = 0x48;

    /* Timer Interrupt */
    int INT50 = 0x50;

    /* Serial Interrupt */
    int INT58 = 0x58;

    /* Joypad Interrupt */
    int INT60 = 0x60;

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

}
