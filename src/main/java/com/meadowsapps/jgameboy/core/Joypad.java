package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 2/8/2017.
 */
public interface Joypad extends CoreElement {

    void press(Button button);

    void release(Button button);

    int read();

    Button[] getButtons();
}
