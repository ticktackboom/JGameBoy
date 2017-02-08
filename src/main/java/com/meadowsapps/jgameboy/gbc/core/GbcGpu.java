package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Gpu;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

/**
 * Created by Dylan on 2/7/17.
 */
public class GbcGpu extends AbstractGbcCoreElement implements Gpu {

    private int[] oam;

    private int[] vram;

    public static final int CHARACTER_DATA = 0x8000;

    public static final int TILE_MAP_0 = 0x9800;
    public static final int TILE_MAP_1 = 0x9C00;
    public static final int TILE_DATA_0 = 0x8800;
    public static final int TILE_DATA_1 = 0x8000;

    public static final int LCDC = 0xFF40;
    public static final int STAT = 0xFF41;
    public static final int SCROLL_Y = 0xFF42;
    public static final int SCROLL_X = 0xFF43;
    public static final int LY = 0xFF44;
    public static final int LYC = 0xFF45;
    public static final int BGP = 0xFF47;
    public static final int OBJECT_PALETTE_0 = 0xFF48;
    public static final int OBJECT_PALETTE_1 = 0xFFf9;
    public static final int WX = 0xFF4B;
    public static final int WY = 0xFF4A;

    public static final int VRAM_SIZE = 0xA000 - 0x8000;

    public static final int OAM = 0xFE00;

    public static final int OAM_SIZE = 0xFEA0 - 0xFE00;

    public GbcGpu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
        oam = new int[OAM_SIZE];
        vram = new int[VRAM_SIZE];
    }

    @Override
    public void reset() {
        oam = new int[OAM_SIZE];
        vram = new int[VRAM_SIZE];
    }

    @Override
    public void draw(GraphicsContext context) {
        BufferedImage frame = new BufferedImage(display().WIDTH, display().HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Image renderer = SwingFXUtils.toFXImage(frame, null);
        context.drawImage(renderer, 0, 0, display().WIDTH, display().HEIGHT);
    }

    @Override
    public int read(int addr) {
        int rv = -1;

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
                }
        }

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
            default:
                switch (addr) {
                    case LCDC:
                        break;
                    case STAT:
                        break;
                    case SCROLL_Y:
                        break;
                    case SCROLL_X:
                        break;
                    case WX:
                        break;
                    case WY:
                        break;
                    case LY:
                        break;
                    case LYC:
                        break;
                    case BGP:
                        break;
                    case OBJECT_PALETTE_0:
                        break;
                    case OBJECT_PALETTE_1:
                        break;
                }

        }
    }

}
