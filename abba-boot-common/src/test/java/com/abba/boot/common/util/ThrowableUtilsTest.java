package com.abba.boot.common.util;

import com.abba.boot.common.lang.AbbaBootException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 异常工具测试
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
public class ThrowableUtilsTest {
    @Test
    public void getStackTraceTest() {
        try {
            throw new AbbaBootException("abc");
        } catch(AbbaBootException e) {
            String trace = ThrowableUtils.getStackTrace(e);
            assertNotNull(trace);
            assertTrue(trace.contains("com.abba.boot.common.lang.AbbaBootException: abc"));
        }
    }

}
