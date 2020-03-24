package com.abba.boot.plugin.lang;

/**
 * 异常
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
public class BootRuntimeException extends RuntimeException {
    /**
     *
     */
    public BootRuntimeException() {
        super();
    }

    /**
     *
     * @param message
     */
    public BootRuntimeException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public BootRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     */
    public BootRuntimeException(Throwable cause) {
        super(cause);
    }

}
