package com.abba.boot.autoconfigure.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
@Slf4j
public class AlibabaOssService {
    private final AlibabaOssProperties properties;
    private final AlibabaOssPrcoess process;

    private OSS oss;

    public AlibabaOssService(AlibabaOssProperties properties, AlibabaOssPrcoess process) {
        this.properties = properties;
        this.process = process;
    }

    @PostConstruct
    public void _init() {
        log.info("Alibaba OSS initializing...");
        if (this.properties == null && !this.properties.validate()) {
            return;
        }

        try {
            this.oss = new OSSClientBuilder().build(this.properties.getEndpoint(),
                    this.properties.getAccessKeyId(),
                    this.properties.getAccessKeySecret());
            log.info("Alibaba OSS initialized successfully");
        } catch (Exception e) {
            log.error("Alibaba OSS initialization exception, cause: {}", e.getMessage());
        }

    }

    public AlibabaOssResponse uploadByte(String objectName, byte[] bytes) throws AlibabaOssException {
        return this.upload(objectName, new ByteArrayInputStream(bytes));
    }

    public AlibabaOssResponse uploadUrl(String objectName, String url) throws AlibabaOssException {
        try {
            return this.upload(objectName, new URL(url).openStream());
        } catch (IOException e) {
            throw new AlibabaOssException(e);
        }
    }

    public AlibabaOssResponse uploadLocalFile(String objectName, String localFile) throws AlibabaOssException {
        try {
            return this.upload(objectName, new FileInputStream(localFile));
        } catch (FileNotFoundException e) {
            throw new AlibabaOssException(e);
        }
    }

    public AlibabaOssResponse upload(String objectName, InputStream inputStream) throws AlibabaOssException {
        OSS client = null;
        try {
            client = this.getOSS();
            PutObjectRequest request = new PutObjectRequest(this.properties.getBucketName(), objectName, inputStream)
                    .withProgressListener(new AlibabaOssProgressListener(objectName, process));

            client.putObject(request);
        } catch (Exception e) {
            throw new AlibabaOssException(e);
        } finally {
            clossOSS(client);
        }
        return AlibabaOssResponse.builder().objectName(objectName).objectUrl(getObjectUrl(objectName)).build();

    }

    public void download(String objectName, String localFile) throws AlibabaOssException {
        OSS client = null;
        try {
            client = this.getOSS();
            GetObjectRequest request = new GetObjectRequest(this.properties.getBucketName(), objectName)
                    .withProgressListener(new AlibabaOssProgressListener(objectName, process));
            client.getObject(request, new File(localFile));
        } catch (Exception e) {
            throw new AlibabaOssException(e);
        }finally {
            clossOSS(client);
        }
    }

    public void delete(String objectName) throws AlibabaOssException {
        OSS client = null;
        try {
            client = this.getOSS();
            client.deleteObject(this.properties.getBucketName(), objectName);
        } catch (Exception e) {
            throw new AlibabaOssException(e);
        }finally {
            clossOSS(client);
        }
    }

    public AlibabaOssResponse multipartUpload(String objectName, File uploadFile, long partSize) throws AlibabaOssException {
        OSS client = null;
        try {
            client = this.getOSS();

            // init multi part upload request
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(this.properties.getBucketName(), objectName);

            // get upload id
            InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);
            String uploadId = result.getUploadId();
            List<PartETag> partETags = new ArrayList();
            // local file length
            long fileLength = uploadFile.length();
            // part count
            int partCount = (int) (fileLength / partSize);

            if (fileLength % partSize != 0) {
                partCount++;
            }

            for (int i = 0; i < partCount; i++) {
                // start position
                long startPos = i * partSize;
                // current part size
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;

                InputStream is = new FileInputStream(uploadFile);
                is.skip(startPos);

                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(this.properties.getBucketName());
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(is);
                // set part size
                uploadPartRequest.setPartSize(curPartSize);
                // set part number
                uploadPartRequest.setPartNumber(i + 1);

                // execute upload part
                UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
                partETags.add(uploadPartResult.getPartETag());
            }

            // sort by part number
            Collections.sort(partETags, Comparator.comparingInt(PartETag::getPartNumber));

            // merge upload part file
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(this.properties.getBucketName(), objectName, uploadId, partETags);
            client.completeMultipartUpload(completeMultipartUploadRequest);
        } catch (Exception e) {
            throw new AlibabaOssException(e);
        } finally {
            clossOSS(client);
        }
        return AlibabaOssResponse.builder().objectName(objectName).objectUrl(getObjectUrl(objectName)).build();
    }

    public AlibabaOssResponse multipartUpload(String objectName, String localFile, long partSize) throws AlibabaOssException {
        // load local file
        File uploadFile = new File(localFile);
        // execute multi part upload file
        return multipartUpload(objectName, uploadFile, partSize);
    }

    private OSS getOSS() {
        if (this.oss == null) {
            throw new AlibabaOssException("Service not started");
        }

        return this.oss;
    }

    private void clossOSS(OSS oss) {
        if (oss != null) {
            oss.shutdown();
        }
    }

    protected String getDefaultObjectUrl(String objectName) {
        return String.format("https://%s.%s/%s", this.properties.getBucketName(),
                this.properties.getEndpoint().replace("http://", ""),
                objectName);
    }

    protected String getObjectUrl(String objectName) {
        final String domain = this.properties.getDomain();
        if (domain != null && domain.length() > 0) {
            return String.format(domain + "/%s", objectName);
        }
        return getDefaultObjectUrl(objectName);
    }
}
