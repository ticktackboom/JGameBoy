package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Button;
import com.meadowsapps.jgameboy.core.Joypad;

/**
 * Created by dmeadows on 2/8/2017.
 */
public class GbcJoypad extends AbstractGbcCoreElement implements Joypad {

    private int register;

    public GbcJoypad(GbcCore core) {
        super(core);
    }

    @Override
    public void press(Button button) {
        if (button instanceof GbcButton) {
            GbcButton gbcButton = (GbcButton) button;
            gbcButton.pressed = true;
        }
    }

    @Override
    public void release(Button button) {
        if (button instanceof GbcButton) {
            GbcButton gbcButton = (GbcButton) button;
            gbcButton.pressed = false;
        }
    }

    @Override
    public int read() {
        return register;
    }

    @Override
    public GbcButton[] getButtons() {
        return GbcButton.values();
    }
}
