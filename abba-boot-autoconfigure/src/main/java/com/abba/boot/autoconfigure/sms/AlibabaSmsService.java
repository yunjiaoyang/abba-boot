package com.abba.boot.autoconfigure.sms;



import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import static org.springframework.util.StringUtils.hasText;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
@Slf4j
public class AlibabaSmsService {
    private static final String SEND_SMS_ACTION = "SendSms";
    private static final String SEND_BATCH_SMS_ACTION = "SendBatchSms";

    private IAcsClient acsClient;
    private final AlibabaSmsProperties properties;
    private final ObjectMapper mapper;

    public AlibabaSmsService(AlibabaSmsProperties properties, ObjectMapper mapper) {
        this.properties = properties;
        this.mapper = mapper;
    }

    @PostConstruct
    public void _init() {
        log.info("Alibaba SMS initializing...");
        if (this.properties == null && !this.properties.validate()) {
            return;
        }

        DefaultProfile profile = DefaultProfile.getProfile(this.properties.getRegionId(),
                this.properties.getAccessKeyId(),
                this.properties.getSecret());
        this.acsClient = new DefaultAcsClient(profile);

        log.info("Alibaba SMS initialized successfully");
    }

    public AlibabaSmsResponse send(AlibabaSmsRequest alibabaSmsRequest) throws AlibabaSmsException {
        if (alibabaSmsRequest == null) {
            return AlibabaSmsResponse.builder().success(false).message("Parameter 'alibabaSmsRequest' must not be null").build();
        }

        String message = alibabaSmsRequest.validate();
        if (hasText(message)) {
            return AlibabaSmsResponse.builder().success(false).message(message).build();
        }

        try {
            return doSend(alibabaSmsRequest);
        } catch (ClientException | JsonProcessingException e) {
            return AlibabaSmsResponse.builder().success(false).message(e.getMessage()).build();
        }
    }

    private IAcsClient getClient() {
        if (this.acsClient == null) {
            throw new AlibabaSmsException("Service not started");
        }

        return this.acsClient;
    }

    private AlibabaSmsResponse doSend(AlibabaSmsRequest alibabaSmsRequest) throws ClientException, JsonProcessingException {
        final IAcsClient client = this.getClient();

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(this.properties.getDomain());
        request.setSysVersion(this.properties.getVersion());
        setRequestQueryParameter(request, "SignName", this.properties.getSignName());
        setRequestQueryParameter(request, "RegionId", this.properties.getRegionId());

        final Object templateParam = alibabaSmsRequest.getTemplateParam();
        if (templateParam != null) {
            request.putQueryParameter("TemplateParam", this.mapper.writeValueAsString(templateParam));
        }

        setRequestQueryParameter(request, "SmsUpExtendCode", alibabaSmsRequest.getSmsUpExtendCode());
        setRequestQueryParameter(request, "OutId", alibabaSmsRequest.getOutId());
        setRequestQueryParameter(request, "TemplateCode", alibabaSmsRequest.getTemplateCode());

        final String phoneNumbers = alibabaSmsRequest.getPhoneNumbers();
        setRequestQueryParameter(request, "PhoneNumbers", phoneNumbers);
        if (phoneNumbers.contains(",")) {
            request.setSysAction(SEND_BATCH_SMS_ACTION);
        } else {
            request.setSysAction(SEND_SMS_ACTION);
        }

        HttpResponse response = client.getCommonResponse(request).getHttpResponse();
        return AlibabaSmsResponse.builder().success(response.getStatus() == 200).message(response.getReasonPhrase()).build();
    }

    private void setRequestQueryParameter(final CommonRequest request, final String field, final String value) {
        if (hasText(value)) {
            request.putQueryParameter(field, value);
        }
    }

}
