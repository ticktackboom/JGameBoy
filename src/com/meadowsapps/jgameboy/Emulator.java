package com.meadowsapps.jgameboy;

/**
 * Created by dmeadows on 1/11/2017.
 */
public interface Emulator {

    Cpu getCpu();

    Ram getRam();
}
