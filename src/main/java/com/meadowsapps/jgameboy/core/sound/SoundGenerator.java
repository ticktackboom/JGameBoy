package com.meadowsapps.jgameboy.core.sound;

/**
 * Created by dmeadows on 1/24/2017.
 */
public interface SoundGenerator {

    void play(byte[] b, int length, int offset);
}
