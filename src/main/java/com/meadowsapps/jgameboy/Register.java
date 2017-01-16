package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.gbc.Constants;

/**
 * Created by dmeadows on 1/12/2017.
 */
public interface Register extends Constants {

    int read();

    void write(int value) throws RegisterSizeException;

    int size();

    void inc() throws RegisterSizeException;

    void dec() throws RegisterSizeException;

    void shift(int dir, int by) throws RegisterSizeException;

    void invert();

    void add(int value) throws RegisterSizeException;

    void subtract(int value) throws RegisterSizeException;

    int get(int bit);

    void set(int bit, int set);

    void set(int bit, boolean set);

    boolean isSet(int bit);
}
