package com.abba.boot.autoconfigure.alibaba;

import lombok.Builder;
import lombok.Data;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
@Data
@Builder
public class AlibabaSmsResponse {
    private boolean success;
    private String message;
}