package com.meadowsapps.jgameboy.core;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;

/**
 * Created by dmeadows on 2/8/2017.
 */
public interface Joypad extends CoreElement, EventHandler<KeyEvent> {

    int PRESSED = 0;

    int RELEASED = 1;

    int read();

    Button[] getButtons();

    public static int getType(KeyEvent event) {
        EventType type = event.getEventType();
        return (type.equals(KeyEvent.KEY_PRESSED)) ? PRESSED
                : (type.equals(KeyEvent.KEY_RELEASED)) ? RELEASED : -1;
    }
}
