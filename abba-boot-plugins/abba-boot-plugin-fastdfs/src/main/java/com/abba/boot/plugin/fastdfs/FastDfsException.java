package com.abba.boot.plugin.fastdfs;

import com.abba.boot.plugin.lang.AbbaBootException;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/16
 */
public class FastDfsException extends AbbaBootException {
    public FastDfsException(Throwable cause) {
        super(cause);
    }

    public FastDfsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastDfsException(String message) {
        super(message);
    }
}
