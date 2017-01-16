package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.Ram;

/**
 * Created by Dylan on 1/14/17.
 */
public class GbcVram implements Ram {

    private int[] m;

    public GbcVram() {
        m = new int[size()];
    }

    @Override
    public int read(int addr) {
        return m[addr];
    }

    @Override
    public void write(int value, int addr) {
        m[addr] = value;
    }

    @Override
    public int size() {
        return 8000;
    }
}
