package com.meadowsapps.jgameboy.core.element;

/**
 * Created by dmeadows on 2/17/2017.
 */
public class InitializationException extends Exception {

    public InitializationException() {
    }

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationException(Throwable cause) {
        super(cause);
    }

}
