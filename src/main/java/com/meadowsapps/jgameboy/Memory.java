package com.meadowsapps.jgameboy;

/**
 * Created by Dylan on 1/14/17.
 */
public interface Memory {

    int read(int addr);

    void write(int value, int addr);

    int size();
}
