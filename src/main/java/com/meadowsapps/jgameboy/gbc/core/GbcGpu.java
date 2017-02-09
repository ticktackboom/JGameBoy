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

    public static final int OAM_SIZE = 0xA0;
    public static final int VRAM_SIZE = 0x2000;

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
    public void step() {

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
