package com.abba.boot.plugin.alibaba.oss;


import com.abba.boot.plugin.lang.BootRuntimeException;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
public class AlibabaOssException extends BootRuntimeException {
    public AlibabaOssException(String message) {
        super(message);
    }

    public AlibabaOssException(Throwable cause) {
        super(cause);
    }
}
