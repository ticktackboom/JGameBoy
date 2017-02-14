package com.meadowsapps.jgameboy.core.apu;

/**
 * Created by dmeadows on 1/24/2017.
 */
public class Envelope {

    private int initialValue;

    private int numberOfSteps;

    private boolean increase;

    public int getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    public void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
    }

    public boolean isIncrease() {
        return increase;
    }

    public void setIncrease(boolean increase) {
        this.increase = increase;
    }
}
