package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.core.Button;
import com.meadowsapps.jgameboy.core.EmulatorCore;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.scene.input.KeyEvent.KEY_RELEASED;

/**
 * Created by dmeadows on 2/8/2017.
 */
public class JGameBoyKeyListener implements EventHandler<KeyEvent> {

    private EmulatorCore core;

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType().equals(KEY_PRESSED)) {
            onKeyPress(event);
        } else if (event.getEventType().equals(KEY_RELEASED)) {
            onKeyRelease(event);
        }
    }

    public void onKeyPress(KeyEvent event) {
        Button[] buttons = core.joypad().getButtons();
        for (Button button : buttons) {
            if (button.getMapping() == event.getCode()) {
                core.joypad().press(button);
                break;
            }
        }
    }

    public void onKeyRelease(KeyEvent event) {
        Button[] buttons = core.joypad().getButtons();
        for (Button button : buttons) {
            if (button.getMapping() == event.getCode()) {
                core.joypad().release(button);
                break;
            }
        }
    }

    public EmulatorCore getCore() {
        return core;
    }

    public void setCore(EmulatorCore core) {
        this.core = core;
    }
}
