package com.abba.boot.autoconfigure.alibaba;


import com.abba.boot.plugin.lang.AbbaBootException;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
public class AlibabaOssException extends AbbaBootException {
    public AlibabaOssException(String message) {
        super(message);
    }

    public AlibabaOssException(Throwable cause) {
        super(cause);
    }
}
