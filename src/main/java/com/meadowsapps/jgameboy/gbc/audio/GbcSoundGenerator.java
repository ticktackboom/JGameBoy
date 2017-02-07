package com.meadowsapps.jgameboy.gbc.audio;

import com.meadowsapps.jgameboy.core.sound.AbstractSoundGenerator;
import com.meadowsapps.jgameboy.core.sound.Envelope;

/**
 * Created by dmeadows on 1/24/2017.
 */
public abstract class GbcSoundGenerator extends AbstractSoundGenerator {

    protected int length;

    protected int cyclePos;

    protected int cycleLength;

    protected int frequency;

    protected int amplitude;

    protected Envelope envelope;

    public static final int CHANNEL_LEFT = 1;

    public static final int CHANNEL_RIGHT = 2;

    public static final int CHANNEL_MONO = 4;

    public GbcSoundGenerator(int sampleRate) {
        setSampleRate(sampleRate);
        envelope = new Envelope();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = (length != -1) ? (64 - length) / 4 : length;
    }

    public int getCyclePos() {
        return cyclePos;
    }

    public void setCyclePos(int cyclePos) {
        this.cyclePos = cyclePos;
    }

    public int getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
        int initialValue = envelope.getInitialValue();
        amplitude = initialValue * 2;
    }
}
