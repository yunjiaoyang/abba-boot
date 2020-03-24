package com.abba.boot.plugin.lang;

/**
 * 异常
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
public class ServiceRuntimeException extends RuntimeException {
    /**
     *
     */
    public ServiceRuntimeException() {
        super();
    }

    /**
     *
     * @param message
     */
    public ServiceRuntimeException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public ServiceRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     */
    public ServiceRuntimeException(Throwable cause) {
        super(cause);
    }

}
