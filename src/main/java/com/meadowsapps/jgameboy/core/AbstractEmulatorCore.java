package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 2/9/2017.
 */
public abstract class AbstractEmulatorCore implements EmulatorCore {

    private boolean running;

    private Thread emulationThread;

    public AbstractEmulatorCore() {
        emulationThread = new Thread(this);
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
}
