package com.abba.boot.plugin.alibaba.sms;


import com.abba.boot.plugin.lang.AbbaBootException;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
public class AlibabaSmsException extends AbbaBootException {
    public AlibabaSmsException(String message) {
        super(message);
    }

    public AlibabaSmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
