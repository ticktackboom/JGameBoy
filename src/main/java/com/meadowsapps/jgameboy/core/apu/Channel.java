package com.meadowsapps.jgameboy.core.apu;

/**
 * Created by dmeadows on 1/24/2017.
 */
public interface Channel {

    boolean isOn();

    void setOn(boolean on);

    boolean isCount();

    void setCount(boolean count);

    int getIndex();

    void setIndex(int index);

    int getLength();

    void setLength(int length);

    float getFrequency();

    void setFrequency(float frequency);

    int[] getWave();

    void setWave(int[] wave);

}
