package com.meadowsapps.jgameboy.gbc.core.gpu;

import com.meadowsapps.jgameboy.core.gpu.Gpu;
import com.meadowsapps.jgameboy.gbc.core.AbstractGbcCoreElement;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Dylan on 2/7/17.
 */
public class GbcGpu extends AbstractGbcCoreElement implements Gpu {

    private int[] oam;

    private int[] vram;

    private Image buffer;

    private int bgpValue;

    private GbcPalette bgp;

    private int[] obpValue;

    private GbcPalette[] obp;

    private GbcTile[] tiles;

    private int frameSkip;

    private int framesDrawn;

    private boolean frameDone;

    private boolean savedWindowDataSelect;

    private boolean spritesEnabledThisFrame;

    private boolean windowEnableThisLine;

    private int windowStopLine;

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

    private int tileDataSelect;

    private int bgTileMap;

    private int windowTileMap;

    private final Color[] gbColors = new Color[]{
            new Color(235, 235, 235),
            new Color(196, 196, 196),
            new Color(96, 96, 96),
            new Color(0, 0, 0)
    };

    public static final int OAM_SIZE = 0xA0;

    public static final int VRAM_SIZE = 0x2000;

    public GbcGpu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
        oam = new int[OAM_SIZE];
        vram = new int[VRAM_SIZE];

        obpValue = new int[2];
        bgp = new GbcPalette();
        obp = new GbcPalette[2];
        obp[0] = new GbcPalette();
        obp[1] = new GbcPalette();

        tiles = new GbcTile[0x300];
        for (int r = 0; r < 0x300; r++) {
            tiles[r] = new GbcTile();
        }

        // create buffer
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void reset() {
        oam = new int[OAM_SIZE];
        vram = new int[VRAM_SIZE];

        obpValue = new int[2];
        bgp = new GbcPalette();
        obp = new GbcPalette[2];
        obp[0] = new GbcPalette();
        obp[1] = new GbcPalette();

        tiles = new GbcTile[0x300];
        for (int r = 0; r < 0x300; r++) {
            tiles[r] = new GbcTile();
        }
        buffer.flush();
    }

    @Override
    public void draw(Graphics g) {
        Graphics back = buffer.getGraphics();
        if (isWindowDisplayEnabled()) {
            int tileAddress = 0;
            int windowStartAddress;
            if (isLcdDisplayEnabled()) {
                windowStartAddress = 0x1C00;
            } else {
                windowStartAddress = 0x1800;
            }
            int attributeData, attributes, tileDataAddress;

            back.setColor(new Color(bgp.getRgbEntry(0)));
            back.fillRect(wX, wY, WIDTH, HEIGHT);

            int tileNumber;
            for (int y = 0; y < 19 - (wY / GbcTile.HEIGHT); y++) {
                for (int x = 0; x < 21 - (wX / GbcTile.WIDTH); x++) {
                    tileAddress = windowStartAddress + (y * 32) + x;
                    if (!savedWindowDataSelect) {
                        tileNumber = vram[tileAddress] + 0x100;
                    } else {
                        tileNumber = vram[tileAddress];
                    }
                    tileDataAddress = tileNumber << 4;

                    if (isGbcFeaturesEnabled()) {
                        attributeData = vram[tileAddress + 0x2000];
                        attributes = (attributeData & 0x07) << 2;

                        if ((attributeData & 0x08) != 0) {
                            tileNumber += 0x180;
                            tileDataAddress += 0x2000;
                        }
                        if ((attributeData & 0x20) != 0) {
                            attributes |= TILE_FLIPX;
                        }
                        if ((attributeData & 0x40) != 0) {
                            attributes |= TILE_FLIPY;
                        }
                    } else {
                        attributes = TILE_BKG;
                    }

                    if (wY + (y * GbcTile.HEIGHT) < windowStopLine) {
                        if (!tiles[tileNumber].isValid(attributes)) {
                            tiles[tileNumber].validate(attributes, vram, tileDataAddress);
                        }
                        tiles[tileNumber].draw(back, attributes,
                                wX + (x * GbcTile.WIDTH),
                                wY + (y * GbcTile.HEIGHT));
                    }
                }
            }
        }

        drawSprites(0);
        if (isSpriteDisplayEnabled() && isGbcFeaturesEnabled()) {
            drawSprites(1);
        }

        g.drawImage(buffer, 0, 0, null);
        frameDone = true;
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
                        rv = bgpValue;
                        break;
                    case OBP0:
                        rv = obpValue[0];
                        break;
                    case OBP1:
                        rv = obpValue[1];
                        break;
                    case BGPI:
                        // rv = cgbBGPWriteSpecReg.value;
                        break;
                    case BGPD:
                        break;
                    case OBPI:
                        // rv = cgbOBJPWriteSpecReg.value;
                        break;
                    case OBPD:
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

                        windowTileMap = ((value & 0x40) == 0x40) ? TILE_MAP_1 : TILE_MAP_0;
                        tileDataSelect = ((value & 0x10) == 0x10) ? TILE_DATA_1 : TILE_DATA_0;
                        bgTileMap = ((value & 0x08) == 0x08) ? TILE_MAP_1 : TILE_MAP_0;
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
                        bgpValue = value;
                        bgp = GbcPalette.decode(value);
                        break;
                    case OBP0:
                        obpValue[0] = value;
                        obp[0] = GbcPalette.decode(value);
                        break;
                    case OBP1:
                        obpValue[0] = value;
                        obp[1] = GbcPalette.decode(value);
                        break;
                    case BGPI:
                        // cgbBGPWriteSpecReg.update(value);
                        break;
                    case BGPD:
                        // cgbBGPWriteDataRegister = value;
                        break;
                    case OBPI:
                        // cgbOBJPWriteSpecReg.update(value);
                        break;
                    case OBPD:
                        // cgbOBJPWriteDataRegister = value;
                        break;
                }
        }
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    public void invalidateAll() {
        for (int r = 0; r < 0x300; r++) {
            tiles[r].invalidate();
        }
    }

    public void invalidateAll(int attribs) {
        for (int r = 0; r < 0x300; r++) {
            tiles[r].invalidate(attribs);
        }
    }

    public void notifyScanline(int line) {
        if ((framesDrawn % frameSkip) == 0) {
            if (line == 0) {
                clearFrameBuffer();
                drawSprites(1);
                spritesEnabledThisFrame = isSpriteDisplayEnabled();
                windowEnableThisLine = isWindowDisplayEnabled();
                windowStopLine = HEIGHT;
            }

            if (isSpriteDisplayEnabled()) {
                spritesEnabledThisFrame = true;
            }

            if (windowEnableThisLine) {
                if (!isWindowDisplayEnabled()) {
                    windowEnableThisLine = false;
                    windowStopLine = line;
                }
            }

            if (line == (mmu().readByte(0xFF4A) + 1)) {
                savedWindowDataSelect = getBgWindowTileDataSelect() == 1;
            }

            if (isBackgroundEnabled() && isGbcFeaturesEnabled()) {
                int xPixelOffset = mmu().readByte(0xFF43) % 8;
                int yPixelOffset = mmu().readByte(0xFF42) % 8;
                if (((yPixelOffset + line) % 8 == 4) || (line == 0)) {
                    if ((HEIGHT <= line) && (line < (HEIGHT + GbcTile.HEIGHT))) {
                        notifyScanline(line + GbcTile.WIDTH);
                    }

                    Graphics back = buffer.getGraphics();
                    int xTileOffset = mmu().readByte(0xFF43) / GbcTile.WIDTH;
                    int yTileOffset = mmu().readByte(0xFF42) / GbcTile.HEIGHT;
                    int tileNumber = 0;

                    int y = ((line + yPixelOffset) / GbcTile.HEIGHT);
                    int bgStartAddress = (getBgTileMapDisplaySelect() == 1) ? 0x1C00 : 0x1800;
                    for (int x = 0; x < 21; x++) {
                        int tileNumberAddress;
                        int attributeData;
                        int vramAddress;

                        if (getBgTileMapDisplaySelect() == 1) {
                            tileNumberAddress = bgStartAddress +
                                    (((y + yTileOffset) % 32) * 32) + ((x + xTileOffset) % 32);
                            tileNumber = vram[tileNumberAddress];
                        } else {
                            tileNumberAddress = bgStartAddress +
                                    (((y + yTileOffset) % 32) * 32) + ((x + xTileOffset) % 32);
                            tileNumber = vram[tileNumberAddress] + 0x100;
                        }
                        attributeData = vram[tileNumberAddress + 0x2000];

                        int attributes = 0;
                        if (isGbcFeaturesEnabled()) {
                            if ((attributeData & 0x08) != 0) {
                                vramAddress = 0x2000 + (tileNumber << 4);
                                tileNumber += 0x180;
                            } else {
                                vramAddress = (tileNumber << 4);
                            }
                            if ((attributeData & 0x20) != 0) {
                                attributes |= TILE_FLIPX;
                            }
                            if ((attributeData & 0x40) != 0) {
                                attributes |= TILE_FLIPY;
                            }
                            attributes += ((attributeData & 0x07) * 4);
                        } else {
                            vramAddress = (tileNumber << 4);
                            attributes = TILE_BKG;
                        }

                        if (!tiles[tileNumber].isValid(attributes)) {
                            tiles[tileNumber].validate(attributes, vram, vramAddress);
                        }
                        tiles[tileNumber].draw(back, attributes,
                                (GbcTile.WIDTH * x) + xPixelOffset,
                                (GbcTile.HEIGHT * y) - yPixelOffset);
                    }
                }
            }
        }
    }

    private void clearFrameBuffer() {
        Graphics back = buffer.getGraphics();
        back.setColor(new Color(bgp.getRgbEntry(0)));
        back.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawSprites(int priority) {
        int vramAddress = 0;
        Graphics back = buffer.getGraphics();

        for (int index = 0; index < 40; index++) {
            int x = mmu().readByte(0xFE01 + (index * 4)) - 8;
            int y = mmu().readByte(0xFE00 + (index * 4)) - 16;
            int tileNumber = mmu().readByte(0xFE02 + (index * 4));
            int attributes = mmu().readByte(0xFE03 + (index * 4));

            if ((attributes & 0x80) >> 7 == priority) {
                int spriteAttributes = 0;
                if (getSpriteMode() == SPRITE_8x16_MODE) {
                    tileNumber &= 0xFE;
                }

                // is GBC features enabled
                if (isGbcFeaturesEnabled()) {
                    if ((attributes & 0x08) != 0) {
                        vramAddress = (tileNumber << 4) + 0x2000;
                        tileNumber += 0x180;
                    } else {
                        vramAddress = (tileNumber << 4);
                    }
                    spriteAttributes += ((attributes & 0x07) << 2) + 32;
                } else {
                    vramAddress = (tileNumber << 4);
                    if ((attributes & 0x10) == 0) {
                        spriteAttributes |= TILE_OBJ1;
                    } else {
                        spriteAttributes |= TILE_OBJ2;
                    }
                }

                if ((attributes & 0x20) != 0) {
                    spriteAttributes |= TILE_FLIPX;
                }
                if ((attributes & 0x40) != 0) {
                    spriteAttributes |= TILE_FLIPY;
                }

                if (!tiles[tileNumber].isValid(spriteAttributes)) {
                    tiles[tileNumber].validate(spriteAttributes, vram, vramAddress);
                }

                if ((spriteAttributes & TILE_FLIPY) != 0) {
                    if (getSpriteMode() == SPRITE_8x16_MODE) {
                        tiles[tileNumber].draw(back, x, y + GbcTile.HEIGHT, spriteAttributes);
                    } else {
                        tiles[tileNumber].draw(back, x, y, spriteAttributes);
                    }
                } else {
                    tiles[tileNumber].draw(back, x, y, spriteAttributes);
                }

                if (getSpriteMode() == SPRITE_8x16_MODE) {
                    if (!tiles[tileNumber + 1].isValid(spriteAttributes)) {
                        tiles[tileNumber + 1].validate(spriteAttributes, vram, vramAddress + 16);
                    }

                    if ((spriteAttributes & TILE_FLIPY) != 0) {
                        tiles[tileNumber + 1].draw(back, x, y, spriteAttributes);
                    } else {
                        tiles[tileNumber + 1].draw(back, x, y + GbcTile.HEIGHT, spriteAttributes);
                    }
                }
            }
        }
    }

    private boolean isGbcFeaturesEnabled() {
        return cartridge().getHeader().getCgbFlag() == 0x80;
    }

    private boolean isLcdDisplayEnabled() {
        return ((lcdc >> 7) & 0x01) == 1;
    }

    private int getWindowTileMapDisplaySelect() {
        return (lcdc >> 6) & 0x01;
    }

    private boolean isWindowDisplayEnabled() {
        return ((lcdc >> 5) & 0x01) == 1;
    }

    private int getBgWindowTileDataSelect() {
        return (lcdc >> 4) & 0x01;
    }

    private int getBgTileMapDisplaySelect() {
        return (lcdc >> 3) & 0x01;
    }

    private int getSpriteMode() {
        return (lcdc >> 2) & 0x01;
    }

    private boolean isSpriteDisplayEnabled() {
        return ((lcdc >> 1) & 0x01) == 1;
    }

    private boolean isBackgroundEnabled() {
        return ((lcdc >> 0) & 0x01) == 1;
    }

}
