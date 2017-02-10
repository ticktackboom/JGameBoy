package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Gpu;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Dylan on 2/7/17.
 */
public class GbcGpu extends AbstractGbcCoreElement implements Gpu {

    private int[] oam;

    private int[] vram;

    private int[][] pixels;

    private boolean lcdInterrupt;

    private boolean vBlankInterrupt;

    private int mode;

    private int clock;

    private int ly;

    private int lcdc;

    private int lyc;

    private int stat;

    private int scrollX, scrollY;

    private int wX, wY;

    private int bgp, obp0, obp1;

    private boolean spritesOn;

    private boolean backgroundOn = true;

    private boolean windowOn = true;

    private boolean displayOn = true;

    private int tileDataSelect;

    private int spriteSizeMode;

    private int bgTileMap;

    private int windowTileMap;

    private RawTile[][] rawTileData;

    private Tile[][] tileData;

    private Palette backgroundPalette;

    private Palette[] objectPalettes;

    private int cgbBGPWriteDataRegister;

    private CGBPaletteSpecRegister cgbBGPWriteSpecReg;

    private int cgbOBJPWriteDataRegister;

    private CGBPaletteSpecRegister cgbOBJPWriteSpecReg;

    private BufferedImage frame;

    private static final Color WHITE = new Color(235, 235, 235);

    private static final Color LIGHT_GRAY = new Color(196, 196, 196);

    private static final Color DARK_GRAY = new Color(96, 96, 96);

    private static final Color BLACK = new Color(0, 0, 0);

    public static final int OAM_SIZE = 0xA0;
    public static final int VRAM_SIZE = 0x2000;

    public GbcGpu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
        oam = new int[OAM_SIZE];
        vram = new int[VRAM_SIZE];
        pixels = new int[HEIGHT][WIDTH];

        tileData = new Tile[2][0x200];
        rawTileData = new RawTile[2][0x200];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 0x200; j++) {
                tileData[i][j] = new Tile();
                rawTileData[i][j] = new RawTile();
            }
        }

        backgroundPalette = new Palette();
        objectPalettes = new Palette[2];
        for (int i = 0; i < 2; i++) {
            objectPalettes[i] = new Palette();
        }

        cgbBGPWriteSpecReg = new CGBPaletteSpecRegister();
        cgbOBJPWriteSpecReg = new CGBPaletteSpecRegister();

        frame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void reset() {
        oam = new int[OAM_SIZE];
        vram = new int[VRAM_SIZE];
    }

    @Override
    public void step() {
        int t = cpu().getClock().m();
        if (!displayOn) {
            ly = 0;
            clock = 0x1C8;
            mode = H_BLANK;
            return;
        } else {
            if (ly >= HEIGHT) {
                mode = V_BLANK;
                lcdInterrupt = false;
            } else if (clock >= (0x1C8 - 0x50)) {
                mode = OAM_READ;
                lcdInterrupt = false;
            } else if (clock >= (0x1C8 - 0x50 - 0xAC)) {
                mode = VRAM_READ;
                lcdInterrupt = false;
            } else {
                mode = H_BLANK;
                if (!(hBlankLcdInterruptEnabled() && lcdInterrupt)) {
                    handleInterrupt(LCD_IRQ);
                    lcdInterrupt = true;
                }
            }
        }

        clock -= t;
        if (clock <= 0) {
            clock += 0x1C8;
            ly++;

            if (ly == HEIGHT) {
                // for each sprite8x8
                // reset scanline draw queue

                // for each sprite8x16
                // reset scanline draw queue

                if (!vBlankInterrupt) {
                    handleInterrupt(V_BLANK_IRQ);

                    if (vBlankLcdInterruptEnabled()) {
                        handleInterrupt(LCD_IRQ);
                    }
                    vBlankInterrupt = true;
                }

                // dump output to screen controller over a channel
            } else if (ly > 0x99) {
                vBlankInterrupt = false;
                ly = 0;
            }

            if (coincidenceLcdInterruptEnabled() && (ly & 0xFF) == lyc) {
                stat |= 0x04;
                handleInterrupt(LCD_IRQ);
            }

            if (ly < HEIGHT) {
                if (displayOn) {
                    if (backgroundOn) {
                        renderBackgroundScanline();
                    }

                    if (windowOn) {
                        renderWindowScanline();
                    }

                    if (spritesOn) {
                        renderSpritesOnScanline();
                    }
                }
            }
        }
    }

    @Override
    public void draw(GraphicsContext context) {
        context.clearRect(0, 0, WIDTH, HEIGHT);
        BufferedImage frame = new BufferedImage(display().WIDTH, display().HEIGHT, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                frame.setRGB(x, y, this.frame.getRGB(x, y));
            }
        }
        Image renderer = SwingFXUtils.toFXImage(frame, null);
        context.drawImage(renderer, 0, 0, display().WIDTH, display().HEIGHT);

    }

    @Override
    public int read(int addr) {
        int rv = -1;

        addr &= 0xFFFF;
        switch (addr & 0xF000) {
            case 0x8000:
            case 0x9000:
                addr -= 0x8000;
                rv = vram[addr];
                break;
            case 0xF000:
                if ((addr & 0xFF00) == OAM && addr < 0xFEA0) {
                    addr -= 0xFE00;
                    rv = oam[addr];

                    break;
                }

                switch (addr) {
                    case LCDC:
                        rv = lcdc;
                        break;
                    case STAT:
                        rv = (mode | (stat & 0xF8));
                        break;
                    case SCROLL_Y:
                        rv = scrollY;
                        break;
                    case SCROLL_X:
                        rv = scrollX;
                        break;
                    case WX:
                        rv = wX;
                        break;
                    case WY:
                        rv = wY;
                        break;
                    case LY:
                        rv = ly;
                        break;
                    case LYC:
                        rv = lyc;
                        break;
                    case BGP:
                        rv = bgp;
                        break;
                    case OBJECT_PALETTE_0:
                        rv = obp0;
                        break;
                    case OBJECT_PALETTE_1:
                        rv = obp1;
                        break;
                    case CGB_BGP_WRITESPEC_REGISTER:
                        rv = cgbBGPWriteSpecReg.value;
                        break;
                    case CGB_BGP_WRITEDATA_REGISTER:
                        break;
                    case CGB_OBJP_WRITESPEC_REGISTER:
                        rv = cgbOBJPWriteSpecReg.value;
                        break;
                    case CGB_OBJP_WRITEDATA_REGISTER:
                        break;
                }
        }

        rv &= 0xFF;
        return rv;
    }

    @Override
    public void write(int value, int addr) {
        switch (addr & 0xF000) {
            case 0x8000:
            case 0x9000:
                addr -= 0x8000;
                vram[addr] = value;
                break;
            case 0xF000:
                if ((addr & 0xFF00) == OAM && addr < 0xFEA0) {
                    addr -= 0xFE00;
                    oam[addr] = value;
                }
                switch (addr) {
                    case LCDC:
                        lcdc = value;

                        windowOn = (value & 0x20) == 0x20;
                        displayOn = (value & 0x80) == 0x80;
                        spritesOn = (value & 0x02) == 0x02;
                        backgroundOn = (value & 0x01) == 0x01;

                        windowTileMap = ((value & 0x40) == 0x40) ? TILE_MAP_1 : TILE_MAP_0;
                        tileDataSelect = ((value & 0x10) == 0x10) ? TILE_DATA_1 : TILE_DATA_0;
                        bgTileMap = ((value & 0x08) == 0x08) ? TILE_MAP_1 : TILE_MAP_0;
                        spriteSizeMode = ((value & 0x04) == 0x04) ? SPRITE_8x16_MODE : SPRITE_8x8_MODE;
                        break;
                    case STAT:
                        stat = (stat & 0x0F) | value;
                        break;
                    case SCROLL_Y:
                        scrollY = value;
                        break;
                    case SCROLL_X:
                        scrollX = value;
                        break;
                    case WX:
                        wX = value;
                        break;
                    case WY:
                        wY = value;
                        break;
                    case LY:
                        ly = 0;
                        break;
                    case LYC:
                        lyc = value;
                        break;
                    case BGP:
                        bgp = value;
                        backgroundPalette = new Palette(value);
                        break;
                    case OBJECT_PALETTE_0:
                        obp0 = value;
                        objectPalettes[0] = new Palette(value);
                        break;
                    case OBJECT_PALETTE_1:
                        obp1 = value;
                        objectPalettes[1] = new Palette(value);
                        break;
                    case CGB_BGP_WRITESPEC_REGISTER:
                        cgbBGPWriteSpecReg.update(value);
                        break;
                    case CGB_BGP_WRITEDATA_REGISTER:
                        cgbBGPWriteDataRegister = value;
                        break;
                    case CGB_OBJP_WRITESPEC_REGISTER:
                        cgbOBJPWriteSpecReg.update(value);
                        break;
                    case CGB_OBJP_WRITEDATA_REGISTER:
                        cgbOBJPWriteDataRegister = value;
                        break;
                }
        }
    }

    private void handleInterrupt(int interrupt) {
        int old = mmu().readByte(INTERRUPT_FLAG);
        int value = old | interrupt;
        mmu().writeByte(value, INTERRUPT_FLAG);
    }

    private void drawScanline(int tileMapOffset, int lineOffset, int screenX, int tileX, int tileY) {
        drawNonColorScanline(tileMapOffset, lineOffset, screenX, tileX, tileY);
    }

    private void drawColorScanline(int tileMapOffset, int lineOffset, int screenX, int tileX, int tileY) {
        while (screenX < WIDTH) {

            screenX++;
        }
    }

    private void drawNonColorScanline(int tileMapOffset, int lineOffset, int screenX, int tileX, int tileY) {
        int tileId = calculateTileNumber(tileMapOffset, lineOffset);
        while (screenX < WIDTH) {
            int color = tileData[0][tileId].data[tileY][tileX];
            frame.setRGB(screenX, ly, color);

            tileX++;
            if (tileX == 8) {
                tileX = 0;
                lineOffset = (lineOffset + 1) % 32;
                tileId = calculateTileNumber(tileMapOffset, lineOffset);
            }

            screenX++;
        }
    }

    private int calculateTileNumber(int tileMapOffset, int lineOffset) {
        int tileId = read(tileMapOffset + lineOffset);
        if (tileDataSelect == TILE_DATA_0) {
            if (tileId < 0x80) {
                tileId += 0x100;
            }
        }
        return tileId;
    }

    private void renderBackgroundScanline() {
        int screenYAdjusted = ly + scrollY;
        int initialTileMapOffset = bgTileMap + (((screenYAdjusted % 256) / 8) * 32);
        int initialLineOffset = (scrollX / 8) % 32;

        int initialTileX = scrollX % 8;
        int initialTileY = screenYAdjusted % 8;
        drawScanline(initialTileMapOffset, initialLineOffset, 0, initialTileX, initialTileY);
    }

    private void renderWindowScanline() {
        int screenYAdjusted = ly - wY;
        if ((0 <= wX && wX < 167) && (0 <= wY && wY < HEIGHT) && screenYAdjusted >= 0) {
            int initialTileMapOffset = windowTileMap + ((screenYAdjusted / 8) * 32);
            int initialLineOffset = 0;
            int screenXAdjusted = (wX - 7) % 0xFF;

            int initialTileX = screenXAdjusted % 8;
            int initialTileY = screenYAdjusted % 8;
            drawScanline(initialTileMapOffset, initialLineOffset, screenXAdjusted, initialTileX, initialTileY);
        }
    }

    private void renderSpritesOnScanline() {

    }

    private boolean hBlankLcdInterruptEnabled() {
        return (read(STAT) & 0x08) == 0x08;
    }

    private boolean vBlankLcdInterruptEnabled() {
        return (read(STAT) & 0x10) == 0x10;
    }

    private boolean coincidenceLcdInterruptEnabled() {
        return (read(STAT) & 0x40) == 0x40;
    }

    class Palette {

        private Color[] colors = new Color[4];

        public Palette() {
            for (int i = 0; i < 4; i++) {
                colors[i] = new Color(0);
            }
        }

        public Palette(int value) {
            colors[0] = new Color(value & 0x03);
            colors[1] = new Color((value >> 2) & 0x03);
            colors[2] = new Color((value >> 4) & 0x03);
            colors[3] = new Color((value >> 6) & 0x03);
        }

        public void updateHi(int index, int hi) {
            Color color = colors[index];
            int lo = color.getRGB() & 0xFF;
            int rgb = (hi << 8) + lo;
            colors[index] = new Color(rgb);
        }

        public void updateLo(int index, int lo) {
            Color color = colors[index];
            int hi = color.getRGB() >> 8;
            int rgb = (hi << 8) + lo;
            colors[index] = new Color(rgb);
        }
    }

    class CGBPaletteSpecRegister {

        int value;

        int paletteNumber;

        int paletteDataNumber;

        boolean high;

        boolean incrementOnNext;

        public void update(int value) {
            this.value = value;
            paletteNumber = (value & 0x38) >> 3;
            paletteDataNumber = (value & 0x06) >> 1;
            high = (value & 0x01) == 0x01;
            incrementOnNext = (value & 0x80) == 0x80;
        }

        public void incrememt() {
            update(value + 1);
        }
    }

    class RawTile {

        int[] data = new int[16];

    }

    class Tile {

        int[][] data = new int[8][8];

    }
}
