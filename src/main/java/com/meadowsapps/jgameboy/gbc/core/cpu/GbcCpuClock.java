package com.meadowsapps.jgameboy.gbc.core.cpu;

/**
 * Created by dmeadows on 2/9/2017.
 */
public class GbcCpuClock {

    private int m, t;

    public int m() {
        return m;
    }

    public void m(int m) {
        this.m = m;
    }

    public int t() {
        return t;
    }

    public void t(int t) {
        this.t = t;
    }
}
