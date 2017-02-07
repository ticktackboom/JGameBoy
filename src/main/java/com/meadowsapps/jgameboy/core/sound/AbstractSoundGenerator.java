package com.meadowsapps.jgameboy.core.sound;

/**
 * Created by dmeadows on 1/24/2017.
 */
public abstract class AbstractSoundGenerator implements SoundGenerator {

    protected int channel;

    protected int sampleRate;

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
