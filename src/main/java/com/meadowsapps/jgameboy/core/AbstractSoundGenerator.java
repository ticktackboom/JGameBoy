package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/24/2017.
 */
public abstract class AbstractSoundGenerator implements SoundGenerator {

    private int channel;

    private int sampleRate;

    @Override
    public int getSampleRate() {
        return sampleRate;
    }

    @Override
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    @Override
    public int getChannel() {
        return channel;
    }

    @Override
    public void setChannel(int channel) {
        this.channel = channel;
    }
}
