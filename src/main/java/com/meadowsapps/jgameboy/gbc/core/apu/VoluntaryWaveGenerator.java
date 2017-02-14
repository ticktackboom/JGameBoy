package com.meadowsapps.jgameboy.gbc.core.apu;

/**
 * Created by dmeadows on 1/24/2017.
 */
public class VoluntaryWaveGenerator extends GbcSoundGenerator {
    public VoluntaryWaveGenerator(int sampleRate) {
        super(sampleRate);
    }

    @Override
    public void play(byte[] b, int length, int offset) {

    }
}
