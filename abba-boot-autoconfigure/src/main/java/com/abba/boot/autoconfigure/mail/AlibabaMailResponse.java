package com.abba.boot.autoconfigure.mail;

import lombok.Builder;
import lombok.Data;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
@Data
@Builder
public class AlibabaMailResponse {
    private boolean success;
    private String message;
}
