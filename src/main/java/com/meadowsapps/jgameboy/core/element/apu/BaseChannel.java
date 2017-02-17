package com.meadowsapps.jgameboy.core.element.apu;

/**
 * Created by dmeadows on 1/24/2017.
 */
public class BaseChannel implements Channel {

    private boolean on;

    private boolean count;

    private int index;

    private int length;

    private float frequency;

    private int[] wave;

    @Override
    public boolean isOn() {
        return on;
    }

    @Override
    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public boolean isCount() {
        return count;
    }

    @Override
    public void setCount(boolean count) {
        this.count = count;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public float getFrequency() {
        return frequency;
    }

    @Override
    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    @Override
    public int[] getWave() {
        return wave;
    }

    @Override
    public void setWave(int[] wave) {
        this.wave = wave;
    }
}
