package com.abba.boot.autoconfigure.oss;

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
public class AlibabaOssResponse {
    private String objectName;
    private String objectUrl;
}
