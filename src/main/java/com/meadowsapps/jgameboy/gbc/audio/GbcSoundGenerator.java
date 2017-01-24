package com.meadowsapps.jgameboy.gbc.audio;

import com.meadowsapps.jgameboy.core.AbstractSoundGenerator;

/**
 * Created by dmeadows on 1/24/2017.
 */
public abstract class GbcSoundGenerator extends AbstractSoundGenerator {

    private int length;

    public static final int CHANNEL_LEFT = 1;

    public static final int CHANNEL_RIGHT = 2;

    public static final int CHANNEL_MONO = 4;

    public GbcSoundGenerator(int sampleRate) {
        setSampleRate(sampleRate);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = (length != -1) ? (256 - length) / 4 : length;
    }
}
