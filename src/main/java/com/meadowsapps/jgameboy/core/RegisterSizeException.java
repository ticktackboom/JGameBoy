package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class RegisterSizeException extends RuntimeException {

    private int type;

    public static final int BIT = 0;
    public static final int UNDERFLOW = -1;
    public static final int OVERFLOW = 1;

    public RegisterSizeException(int type) {
    }

    public RegisterSizeException(int type, String message) {
        super(message);
    }

    public RegisterSizeException(int type, String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterSizeException(int type, Throwable cause) {
        super(cause);
    }

    private static String getTypeAsString(int type) {
        String rv = "";
        switch (type) {
            case UNDERFLOW:
                rv = "Underflow";
                break;
            case OVERFLOW:
                rv = "Overflow";
                break;
            case BIT:
                rv = "Bit";
                break;
        }
        return rv;
    }

    @Override
    public String toString() {
        return "RegisterSizeException{" +
                "type:" + getTypeAsString(type) +
                '}';
    }
}
