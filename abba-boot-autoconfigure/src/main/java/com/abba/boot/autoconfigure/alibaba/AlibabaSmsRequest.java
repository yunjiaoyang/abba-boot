package com.abba.boot.autoconfigure.alibaba;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.util.StringUtils.hasText;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
@Data
@Builder
@Slf4j
public class AlibabaSmsRequest {
    /**
     * 电话号码，多个使用','相隔
     */
    private String phoneNumbers;
    private String templateCode;
    private String smsUpExtendCode;
    private String outId;
    private AlibabaSmsTemplateParam templateParam;

    public String validate() {
        if (!hasText(phoneNumbers)) {
            return "Property 'phone' must have value";
        }

        if (!hasText(templateCode)) {
            return "Property 'templateCode' must have value";
        }

        // 验证通过
        return null;
    }

}
