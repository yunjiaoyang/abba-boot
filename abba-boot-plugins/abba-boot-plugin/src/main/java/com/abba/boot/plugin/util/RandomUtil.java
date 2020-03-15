package com.abba.boot.plugin.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
@UtilityClass
public class RandomUtil {

    private static final int DEF_COUNT = 20;

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
     * Generates a reset key.
     *
     * @return the generated reset key
     */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }
}
