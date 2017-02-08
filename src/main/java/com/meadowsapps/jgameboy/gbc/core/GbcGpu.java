package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Gpu;

/**
 * Created by Dylan on 2/7/17.
 */
public class GbcGpu extends AbstractGbcCoreElement implements Gpu {

    private int[] oam = new int[0xA0];

    private int[] vram = new int[0x4000];

    public GbcGpu(GbcCore core) {
        super(core);
    }

    @Override
    public int read(int addr) {
        switch (addr) {

        }
        return 0;
    }

    @Override
    public void write(int value, int addr) {

    }

}
