package com.meadowsapps.jgameboy.core;


import javax.swing.*;

/**
 * Created by dmeadows on 2/9/2017.
 */
public abstract class AbstractEmulatorCore implements EmulatorCore {

    private double scale;

    private boolean running;

    private Timer timer;

    public AbstractEmulatorCore() {
        timer = new Timer(17, event -> {
            run();
        });
        timer.setRepeats(true);
        scale = 1.0;
    }

    @Override
    public void start() {
        timer.start();
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public double getScale() {
        return scale;
    }
}
