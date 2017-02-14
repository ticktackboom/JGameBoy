package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 2/9/2017.
 */
public abstract class AbstractEmulatorCore implements EmulatorCore {

    private double scale;

    private boolean running;

    private Thread emulationThread;

    public AbstractEmulatorCore() {
        emulationThread = new Thread(this);
        scale = 1.0;
    }

    @Override
    public void start() {
        if (!emulationThread.isAlive()) {
            emulationThread.start();
            running = true;
        }
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
