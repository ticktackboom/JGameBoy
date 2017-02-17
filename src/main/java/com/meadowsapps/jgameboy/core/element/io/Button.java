package com.meadowsapps.jgameboy.core.element.io;

import javafx.scene.input.KeyCode;

/**
 * Created by dmeadows on 2/8/2017.
 */
public interface Button {

    void map(KeyCode keyCode);

    boolean isPressed();

    KeyCode getMapping();
}
