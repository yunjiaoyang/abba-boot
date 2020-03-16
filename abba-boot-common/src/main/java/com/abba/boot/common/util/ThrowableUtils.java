package com.abba.boot.common.util;

import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
@UtilityClass
public final class ThrowableUtils {
    /**
     *
     * @param throwable
     * @return
     */
    public String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
