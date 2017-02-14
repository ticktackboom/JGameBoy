package com.meadowsapps.jgameboy.core.cpu;

/**
 * Created by dmeadows on 1/12/2017.
 */
public interface Register {

    int read();

    void write(int value);

    void inc();

    void dec();

    void shift(int dir, int by);

    void invert();

    void add(int value);

    void subtract(int value);

    int get(int bit);

    void set(int bit, int set);

    void set(int bit, boolean set);

    void flip(int bit);

    boolean isSet(int bit);
}
