package com.meadowsapps.jgameboy.groovy.core.util

/**
 * Created by dmeadows on 3/2/2017.
 */
class Range {

    private int start

    private int stop

    Range(int start, int stop) {
        this.start = start
        this.stop = stop
    }

    boolean contains(Number n) {
        return start <= n && n <= stop
    }

    int size() {
        return (stop - start) + 1
    }

    int getStart() {
        return start
    }

    int getStop() {
        return stop
    }

}
