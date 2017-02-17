package com.meadowsapps.jgameboy.gbc.core.element.io;

import com.meadowsapps.jgameboy.core.element.io.Button;
import javafx.scene.input.KeyCode;

/**
 * Created by dmeadows on 2/8/2017.
 */
public enum GbcButton implements Button {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    A,
    B,
    START,
    SELECT;

    boolean pressed;

    private KeyCode mapping;

    @Override
    public void map(KeyCode keyCode) {
        this.mapping = keyCode;
    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    @Override
    public KeyCode getMapping() {
        return mapping;
    }

    public static GbcButton getButton(KeyCode keyCode) {
        GbcButton rv = null;
        for (GbcButton button : values()) {
            if (button.getMapping() == keyCode) {
                rv = button;
                break;
            }
        }
        return rv;
    }
}
