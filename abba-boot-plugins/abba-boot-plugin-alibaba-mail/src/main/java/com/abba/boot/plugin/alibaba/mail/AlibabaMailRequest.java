package com.abba.boot.plugin.alibaba.mail;

import com.abba.boot.plugin.enums.MailContentType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;


/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
@Data
@Builder
public class AlibabaMailRequest {
    private MailContentType contentType;
    private String content;
    private List<String> toAddress;
    private String fromAlias;
    private String subject;
    private String tagName;
    private String clickTrace = "0";

    public boolean validate() {
        if (isBlank(content)) {
            return false;
        }

        if (toAddress == null || toAddress.isEmpty()) {
            return false;
        }

        if (isBlank(subject)) {
            return false;
        }

        // 验证通过
        return true;
    }
}
