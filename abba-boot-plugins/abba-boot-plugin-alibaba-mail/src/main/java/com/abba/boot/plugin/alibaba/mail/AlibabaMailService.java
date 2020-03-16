package com.abba.boot.plugin.alibaba.mail;


import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;


/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
public class AlibabaMailService {
    static final String JOIN_SPLIT = ",";

    private final String version;
    private final String accountName;
    private final Integer addressType;
    private final Boolean replyToAddress;
    private final String fromAlias;
    private final String tagName;
    private final IAcsClient client;

    public AlibabaMailService(String version, String accountName, Integer addressType,
                              Boolean replyToAddress, String fromAlias,
                              String tagName, IAcsClient client) {
        this.version = version;
        this.accountName = accountName;
        this.addressType = addressType;
        this.replyToAddress = replyToAddress;
        this.fromAlias = fromAlias;
        this.tagName = tagName;
        this.client = client;
    }

    public AlibabaMailResponse sendMail(AlibabaMailRequest alibabaMailRequest) {
        if (alibabaMailRequest == null || !alibabaMailRequest.validate()) {
            return AlibabaMailResponse.builder().success(false).message("Parameter verification failed ").build();
        }

        try {
            return doSend(alibabaMailRequest);
        } catch (ClientException e) {
            return AlibabaMailResponse.builder().success(false).message(e.getMessage()).build();
        }
    }

    private AlibabaMailResponse doSend(AlibabaMailRequest alibabaMailRequest) throws ClientException {
        SingleSendMailRequest request = new SingleSendMailRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysVersion(this.version);
        request.setAccountName(this.accountName);
        request.setAddressType(this.addressType);
        request.setReplyToAddress(this.replyToAddress);

        final String fromAlias = getDefaultValue(alibabaMailRequest.getFromAlias(), this.fromAlias);
        final String tagName = getDefaultValue(alibabaMailRequest.getTagName(), this.tagName);
        if (isNotEmpty(tagName)) {
            request.setTagName(tagName);
        }

        request.setClickTrace(alibabaMailRequest.getClickTrace());
        request.setToAddress(String.join(JOIN_SPLIT, alibabaMailRequest.getToAddress()));
        request.setSubject(alibabaMailRequest.getSubject());
        switchBody(request, alibabaMailRequest.getContent(), alibabaMailRequest.getContentType());
        SingleSendMailResponse response = client.getAcsResponse(request);

        return AlibabaMailResponse.builder().success(isEmpty(response.getRequestId())).build();
    }

    private String getDefaultValue(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }

    private void switchBody(SingleSendMailRequest request, String content, AlibabaMailRequest.ContentType contentType) {
        AlibabaMailRequest.ContentType type = contentType == null ? AlibabaMailRequest.ContentType.TEXT : contentType;
        switch (type) {
            case HTML:
                request.setHtmlBody(content);
                break;
            case TEXT:
                request.setTextBody(content);
                break;
        }
    }
}
