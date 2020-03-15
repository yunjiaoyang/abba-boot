package com.abba.boot.autoconfigure.mail;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
@Data
@Builder
public class AlibabaMailRequest {
    private ContentType contentType;
    private String content;
    private List<String> toAddress;
    private String fromAlias;
    private String subject;
    private String tagName;
    private String clickTrace = "0";

    public String validate() {
        if (!hasText(content)) {
            return "Property 'content' must have value";
        }

        if (CollectionUtils.isEmpty(toAddress)) {
            return "Property 'toAddress' must not be empty";
        }

        if (!hasText(subject)) {
            return "Property 'subject' must have value";
        }

        // 验证通过
        return null;
    }
}
