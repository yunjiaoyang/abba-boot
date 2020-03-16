package com.abba.boot.plugin.alibaba.sms;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.StringUtils.isBlank;

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

    public boolean validate() {
        if (isBlank(phoneNumbers)) {
            return false;
        }

        if (isBlank(templateCode)) {
            return false;
        }

        // 验证通过
        return true;
    }

}
