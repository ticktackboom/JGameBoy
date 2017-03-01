package com.meadowsapps.jgameboy.core;


import javafx.animation.AnimationTimer;

/**
 * Created by dmeadows on 2/9/2017.
 */
public abstract class AbstractEmulatorCore extends AnimationTimer implements EmulatorCore {

    private double scale;

    private boolean running;

    public AbstractEmulatorCore() {
        scale = 1.0;
    }

    @Override
    public void start() {
        running = true;
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        running = false;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
