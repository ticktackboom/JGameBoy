package com.meadowsapps.jgameboy;

import com.meadowsapps.jgameboy.AbstractRegister;
import org.joou.UShort;

import static org.joou.Unsigned.ushort;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register16Bit extends AbstractRegister {

    private UShort value;

    public Register16Bit() {
        value = ushort(0);
    }

    @Override
    public int read() {
        return value.intValue();
    }

    @Override
    public int write(int value) {
        this.value = ushort(value);
        return this.value.intValue();
    }

    @Override
    public int size() {
        return UShort.MAX_VALUE;
    }
}
