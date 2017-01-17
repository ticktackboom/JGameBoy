package com.meadowsapps.jgameboy.gbc;

/**
 * Created by Dylan on 1/13/17.
 */
public interface Constants {

    /**
     * Shift direction left
     */
    int LEFT = -1;
    /**
     * Shift direction right
     */
    int RIGHT = 1;

    public static int toInt(boolean b) {
        return (b) ? 1 : 0;
    }

    public static boolean toBoolean(int i) throws IllegalArgumentException {
        if (0 <= i && i <= 1) {
            return i == 1;
        } else {
            throw new IllegalArgumentException("i must be 0 or 1");
        }
    }
}
