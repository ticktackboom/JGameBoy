package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Gpu;
import com.meadowsapps.jgameboy.core.Sprite;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;

/**
 * Created by Dylan on 2/7/17.
 */
public class GbcGpu extends AbstractGbcCoreElement implements Gpu {

    private int[] oam;

    private int[] vram;

    private Color[] spriteMap;

    private boolean[] showSprite;

    private Color[] background;

    private boolean[] showBackground;

    public static final Color WHITE = new Color(255, 255, 255, 255);

    public static final Color LIGHT_GRAY = new Color(192, 192, 192, 255);

    public static final Color DARK_GRAY = new Color(96, 96, 96, 255);

    public static final Color BLACK = new Color(0, 0, 0, 255);

    public static final Color TRANSPARENT = new Color(1, 1, 1, 0);

    public GbcGpu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
        oam = new int[0xA0];
        vram = new int[0x4000];

        spriteMap = new Color[0x25600];
        showSprite = new boolean[0x25600];

        background = new Color[0x25600];
        showBackground = new boolean[0x25600];

        for (int i = 0; i < 0x25600; i++) {
            spriteMap[i] = WHITE;
            background[i] = WHITE;
        }
    }

    @Override
    public void reset() {
        for (int i = 0; i < 0x25600; i++) {
            spriteMap[i] = WHITE;
            background[i] = WHITE;
        }

        showSprite = new boolean[0x25600];
        showBackground = new boolean[0x25600];
    }

    @Override
    public void draw(GraphicsContext context) {
        BufferedImage frame = new BufferedImage(display().WIDTH, display().HEIGHT, BufferedImage.TYPE_INT_ARGB);

        int lcdControl = mmu().readByte(0xFF40);
        if ((lcdControl & 0x80) == 1) {
            for (int x = 0; x < display().WIDTH; x++) {
                for (int y = 0; y < display().HEIGHT; y++) {
                    if (showSprite[y * 256 + x] && (lcdControl & 0x02) == 1) {
                        frame.setRGB(x, y, toRGB(spriteMap[y * 256 + x]));
                    }

                    if (showBackground[y * 256 + x] && (lcdControl & 0x01) == 1) {
                        frame.setRGB(x, y, toRGB(background[y * 256 + x]));
                    }
                }
            }
        }

        showSprite = new boolean[0x25600];
        showBackground = new boolean[0x25600];

        Image renderer = SwingFXUtils.toFXImage(frame, null);
        context.drawImage(renderer, 0, 0, display().WIDTH, display().HEIGHT);
    }

    @Override
    public int read(int addr) {
        switch (addr) {

        }
        return 0;
    }

    @Override
    public void write(int value, int addr) {

    }

    private void drawSprites(int lcdControl, int lineNumber, Sprite[] sprites) {
        int drawCount = 0;
        int spritePatternTable = 0x8000;

        for (int index = sprites.length - 1; index >= 0; index--) {
            if (drawCount >= 10) break;

            Sprite sprite = sprites[index];
            if (sprite.getY() + sprite.getHeight() > lineNumber && sprite.getY() <= lineNumber) {
                int tileAddress = spritePatternTable + (sprite.getTileNumber() * 16);
                int tileLine = lineNumber - sprite.getY();
                if (sprite.isYFlip()) {
                    tileLine = sprite.getHeight() - 1 - tileLine;
                }

            }
        }
    }

    private int toRGB(Color color) {
        int r = (int) color.getRed();
        int g = (int) color.getGreen();
        int b = (int) color.getBlue();
        return (r << 16) + (g << 8) + b;
    }
}
