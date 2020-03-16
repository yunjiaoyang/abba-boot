package com.abba.boot.plugin.alibaba.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import lombok.Data;

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
@Data
public class AlibabaOssService {
    private final String bucketName;
    private final OSS client;
    private final AlibabaOssPrcoess process;

    public AlibabaOssService(String bucketName, OSS client, AlibabaOssPrcoess process) {
        this.bucketName = bucketName;
        this.client = client;
        this.process = process;
    }


    public PutObjectResult uploadByte(String objectName, byte[] bytes) throws AlibabaOssException {
        return this.upload(objectName, new ByteArrayInputStream(bytes));
    }

    public PutObjectResult uploadUrl(String objectName, String url) throws AlibabaOssException {
        try {
            return this.upload(objectName, new URL(url).openStream());
        } catch (IOException e) {
            throw new AlibabaOssException(e);
        }
    }

    public PutObjectResult uploadLocalFile(String objectName, String localFile) throws AlibabaOssException {
        try {
            return this.upload(objectName, new FileInputStream(localFile));
        } catch (FileNotFoundException e) {
            throw new AlibabaOssException(e);
        }
    }

    public PutObjectResult upload(String objectName, InputStream inputStream) throws AlibabaOssException {
        try {
            PutObjectRequest request = new PutObjectRequest(this.bucketName, objectName, inputStream)
                    .withProgressListener(this.createListener(objectName));

            return client.putObject(request);
        } catch (Exception e) {
            throw new AlibabaOssException(e);
        } finally {
            clossOSS(client);
        }
    }

    public void download(String objectName, String localFile) throws AlibabaOssException {
        try {
            GetObjectRequest request = new GetObjectRequest(this.bucketName, objectName)
                    .withProgressListener(this.createListener(objectName));
            client.getObject(request, new File(localFile));
        } catch (Exception e) {
            throw new AlibabaOssException(e);
        } finally {
            clossOSS(client);
        }
    }

    public void delete(String objectName) throws AlibabaOssException {
        try {
            client.deleteObject(this.bucketName, objectName);
        } catch (Exception e) {
            throw new AlibabaOssException(e);
        } finally {
            clossOSS(client);
        }
    }

    public CompleteMultipartUploadResult multipartUpload(String objectName, File uploadFile, long partSize) throws AlibabaOssException {
        try {
            // init multi part upload request
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(this.bucketName, objectName);

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
                uploadPartRequest.setBucketName(this.bucketName);
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
                    new CompleteMultipartUploadRequest(this.bucketName, objectName, uploadId, partETags);
            return client.completeMultipartUpload(completeMultipartUploadRequest);
        } catch (Exception e) {
            throw new AlibabaOssException(e);
        } finally {
            clossOSS(client);
        }
    }

    public CompleteMultipartUploadResult multipartUpload(String objectName, String localFile, long partSize) throws AlibabaOssException {
        // load local file
        File uploadFile = new File(localFile);
        // execute multi part upload file
        return multipartUpload(objectName, uploadFile, partSize);
    }

    private void clossOSS(OSS oss) {
        if (oss != null) {
            oss.shutdown();
        }
    }

    private AlibabaOssProgressListener createListener(String objectName) {
        if (this.process == null) {
            return null;
        }

        return new AlibabaOssProgressListener(objectName, process);
    }
}
