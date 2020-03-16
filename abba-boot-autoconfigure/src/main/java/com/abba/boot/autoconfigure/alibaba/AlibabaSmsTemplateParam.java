package com.abba.boot.autoconfigure.alibaba;

import lombok.Getter;

import java.util.HashMap;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
@Getter
public class AlibabaSmsTemplateParam {
    private static final HashMap<String, Object> params = new HashMap();

    public AlibabaSmsTemplateParam put(String name, Object value) {
        params.put(name, value);
        return this;
    }
}
