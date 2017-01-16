package com.meadowsapps.jgameboy;

/**
 * Created by dmeadows on 1/13/2017.
 */
public abstract class AbstractRegister implements Register {

    @Override
    public final void inc() throws RegisterSizeException {
        add(1);
    }

    @Override
    public final void dec() throws RegisterSizeException {
        subtract(1);
    }

    @Override
    public final void shift(int dir, int by) {
        int max = (int) (Math.log(size()) / Math.log(2));
        if (0 < by && by < max - 1) {
            int value = (dir == LEFT) ? read() << by : read() >> by;
            write(value);
        } else {
//            throw new IllegalArgumentException("Register Size: ")
        }
    }

    @Override
    public final void invert() {
        int value = read();
        write(~value);
    }

    @Override
    public final void add(int value) throws RegisterSizeException {
        if (!(read() + value > size())) {
            write(read() + value);
        } else {
            String message = String.format("Register Size: %d + %d > %d",
                    read(), value, size());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public final void subtract(int value) throws RegisterSizeException {
        if (!(read() - value < 0)) {
            write(read() - value);
        } else {
            String message = String.format("Register Size: %d - %d < 0",
                    read(), value);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public final int get(int bit) {
        return ((read() >> bit) & 1);
    }

    @Override
    public final void set(int bit, int set) {

    }

    @Override
    public final void set(int bit, boolean set) {
        set(bit, (set) ? 1 : 0);
    }

    @Override
    public final boolean isSet(int bit) {
        return get(bit) == 1;
    }

    @Override
    public String toString() {
        return "" + read();
    }
}
