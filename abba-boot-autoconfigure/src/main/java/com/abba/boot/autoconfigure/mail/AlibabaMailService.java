package com.abba.boot.autoconfigure.mail;

import com.abba.boot.autoconfigure.sms.AlibabaSmsException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

import static org.springframework.util.StringUtils.hasText;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
@Slf4j
public class AlibabaMailService {
    static final String JOIN_SPLIT = ",";

    private final AlibabaMailProperties properties;
    private IAcsClient acsClient;

    public AlibabaMailService(AlibabaMailProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void _init() {
        log.info("Alibaba Mail initializing...");
        if (this.properties == null && !this.properties.validate()) {
            return;
        }

        DefaultProfile profile = DefaultProfile.getProfile(this.properties.getRegionId(),
                this.properties.getAccessKeyId(),
                this.properties.getSecret());
        this.acsClient = new DefaultAcsClient(profile);

        log.info("Alibaba Mail initialized successfully");
    }

    public AlibabaMailResponse sendMail(AlibabaMailRequest alibabaMailRequest) throws AlibabaMailException {
        if (alibabaMailRequest == null) {
            return AlibabaMailResponse.builder().success(false).message("Parameter 'alibabaMailRequest' must not be null").build();
        }

        String message = alibabaMailRequest.validate();
        if (hasText(message)) {
            return AlibabaMailResponse.builder().success(false).message(message).build();
        }

        try {
            return doSend(alibabaMailRequest);
        } catch (ClientException e) {
            return AlibabaMailResponse.builder().success(false).message(e.getMessage()).build();
        }
    }

    private AlibabaMailResponse doSend(AlibabaMailRequest alibabaMailRequest) throws ClientException {
        final IAcsClient client = this.getClient();

        SingleSendMailRequest request = new SingleSendMailRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysVersion(this.properties.getVersion());
        request.setAccountName(this.properties.getAccountName());
        request.setAddressType(this.properties.getAddressType());
        request.setReplyToAddress(this.properties.isReplyToAddress());

        final String fromAlias = alibabaMailRequest.getFromAlias() == null ?
                this.properties.getFromAlias() : alibabaMailRequest.getFromAlias();
        request.setFromAlias(fromAlias);

        final String tagName = alibabaMailRequest.getTagName() == null ?
                this.properties.getTagName() : alibabaMailRequest.getTagName();
        if (hasText(tagName)) {
            request.setTagName(tagName);
        }

        request.setClickTrace(alibabaMailRequest.getClickTrace());
        request.setToAddress(String.join(JOIN_SPLIT, alibabaMailRequest.getToAddress()));
        request.setSubject(alibabaMailRequest.getSubject());
        switchBody(request, alibabaMailRequest.getContent(), alibabaMailRequest.getContentType());
        SingleSendMailResponse response = client.getAcsResponse(request);

        return AlibabaMailResponse.builder().success(!StringUtils.isEmpty(response.getRequestId())).build();
    }

    private IAcsClient getClient() {
        if (this.acsClient == null) {
            throw new AlibabaSmsException("Service not started");
        }

        return this.acsClient;
    }

    private void switchBody(SingleSendMailRequest request, String content, ContentType contentType) {
        if (ObjectUtils.isEmpty(contentType)) {
            request.setTextBody(content);
        } else {
            switch (contentType) {
                case HTML:
                    request.setHtmlBody(content);
                    break;
                case TEXT:
                    request.setTextBody(content);
                    break;
            }
        }
    }
}
