package org.ogcs.log.exception;

/**
 * @author TinyZ
 * @date 2016-07-01.
 */
public class IllegalVersionException extends Exception {

    public IllegalVersionException(String message) {
        super(message);
    }

    public IllegalVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalVersionException(Throwable cause) {
        super(cause);
    }

    protected IllegalVersionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
