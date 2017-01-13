package com.meadowsapps.jgameboy.gbc;

import com.meadowsapps.jgameboy.Register;
import org.joou.UShort;

import static org.joou.Unsigned.ushort;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class Register16Bit implements Register {

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
}
