package com.abba.boot.plugin.lang;

/**
 * 异常
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
public class AbbaBootException extends RuntimeException {
    /**
     *
     */
    public AbbaBootException() {
        super();
    }

    /**
     *
     * @param message
     */
    public AbbaBootException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public AbbaBootException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     */
    public AbbaBootException(Throwable cause) {
        super(cause);
    }

}
