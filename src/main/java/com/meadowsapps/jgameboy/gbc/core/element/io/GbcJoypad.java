package com.meadowsapps.jgameboy.gbc.core.element.io;

import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.core.element.io.Joypad;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;
import com.meadowsapps.jgameboy.gbc.core.element.AbstractGbcCoreElement;
import javafx.scene.input.KeyEvent;

import static com.meadowsapps.jgameboy.gbc.core.element.GbcMemoryMap.JOYP;

/**
 * Created by dmeadows on 2/8/2017.
 */
public class GbcJoypad extends AbstractGbcCoreElement implements Joypad {

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
    public void initialize() throws InitializationException {
    }

    @Override
    public void reset() {
    }

    @Override
    public void handle(KeyEvent event) {
        int register = mmu().readByte(JOYP);
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
        mmu().writeByte(register, JOYP);
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
