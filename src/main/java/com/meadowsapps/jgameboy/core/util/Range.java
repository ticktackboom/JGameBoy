package com.meadowsapps.jgameboy.core.util;

/**
 * Created by dmeadows on 2/17/2017.
 */
public class Range {

    private int start, stop;

    public Range(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }

    public boolean contains(int i) {
        return start <= i && i <= stop;
    }

    public int size() {
        return (stop - start) + 1;
    }

    public int start() {
        return start;
    }

    public int stop() {
        return stop;
    }

}
