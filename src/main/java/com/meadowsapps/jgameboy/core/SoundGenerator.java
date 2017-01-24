package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/24/2017.
 */
public interface SoundGenerator {

    int getSampleRate();

    void setSampleRate(int sampleRate);

    int getChannel();

    void setChannel(int channel);

    void play(byte[] b, int length, int offset);
}
