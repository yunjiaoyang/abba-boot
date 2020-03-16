package com.abba.boot.plugin.alibaba.sms;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
public class AlibabaSmsService {
    private static final String SEND_SMS_ACTION = "SendSms";
    private static final String SEND_BATCH_SMS_ACTION = "SendBatchSms";

    private final String domain;
    private final String version;
    private final String signName;
    private final IAcsClient client;
    private final ObjectMapper mapper;

    public AlibabaSmsService(String domain,
                             String version,
                             String signName,
                             IAcsClient acsClient,
                             ObjectMapper mapper) {
        this.domain = domain;
        this.version = version;
        this.signName = signName;
        this.client = acsClient;
        this.mapper = mapper;
    }

    public AlibabaSmsResponse send(AlibabaSmsRequest alibabaSmsRequest) {
        if (alibabaSmsRequest == null || !alibabaSmsRequest.validate()) {
            return AlibabaSmsResponse.builder().success(false).message("Parameter verification failed ").build();
        }

        try {
            return doSend(alibabaSmsRequest);
        } catch (ClientException | JsonProcessingException e) {
            return AlibabaSmsResponse.builder().success(false).message(e.getMessage()).build();
        }
    }

    private AlibabaSmsResponse doSend(AlibabaSmsRequest alibabaSmsRequest) throws ClientException, JsonProcessingException {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(this.domain);
        request.setSysVersion(this.version);
        setRequestQueryParameter(request, "SignName", this.signName);

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
        if (isNotBlank(value)) {
            request.putQueryParameter(field, value);
        }
    }

}
