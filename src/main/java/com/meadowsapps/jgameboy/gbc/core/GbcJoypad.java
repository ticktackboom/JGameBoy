package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Joypad;
import javafx.scene.input.KeyEvent;

/**
 * Created by dmeadows on 2/8/2017.
 */
public class GbcJoypad extends AbstractGbcCoreElement implements Joypad {

    private int register;

    public static final int SELECT_BUTTONS = 5;
    public static final int SELECT_DIRECTION = 4;
    public static final int INPUT_UP = 2;
    public static final int INPUT_DOWN = 3;
    public static final int INPUT_LEFT = 1;
    public static final int INPUT_RIGHT = 0;
    public static final int INPUT_A = 0;
    public static final int INPUT_B = 1;
    public static final int INPUT_START = 3;
    public static final int INPUT_SELECT = 2;

    public GbcJoypad(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void reset() {
    }

    @Override
    public void handle(KeyEvent event) {
        int type = Joypad.getType(event);
        GbcButton button = GbcButton.getButton(event.getCode());
        if (button != null) {
            switch (type) {
                case PRESSED:
                    if (isDirectionKey(button)) {
                        register |= (1 << SELECT_DIRECTION);
                        register |= ~(1 << SELECT_BUTTONS);
                        register |= (1 << getBit(button));
                    } else {
                        register |= (1 << SELECT_BUTTONS);
                        register |= ~(1 << SELECT_DIRECTION);
                        register |= (1 << getBit(button));
                    }
                    button.pressed = true;
                    break;
                case RELEASED:
                    register |= ~(1 << getBit(button));
                    button.pressed = false;
                    break;
            }
        }
    }

    @Override
    public int read() {
        return register & 0xFF;
    }

    @Override
    public GbcButton[] getButtons() {
        return GbcButton.values();
    }

    private boolean isDirectionKey(GbcButton button) {
        return button == GbcButton.UP || button == GbcButton.DOWN
                || button == GbcButton.LEFT || button == GbcButton.RIGHT;
    }

    private int getBit(GbcButton button) {
        int bit = 7;
        switch (button) {
            case UP:
                bit = INPUT_UP;
                break;
            case DOWN:
                bit = INPUT_DOWN;
                break;
            case LEFT:
                bit = INPUT_LEFT;
                break;
            case RIGHT:
                bit = INPUT_RIGHT;
                break;
            case A:
                bit = INPUT_A;
                break;
            case B:
                bit = INPUT_B;
                break;
            case START:
                bit = INPUT_START;
                break;
            case SELECT:
                bit = INPUT_SELECT;
                break;
        }
        return bit;
    }

}
