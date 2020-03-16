package com.abba.boot.plugin.fastdfs;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.util.Assert;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/16
 */
public class FastDfsService {
    private final TrackerServer trackerS;
    private final TrackerClient trackerC;
    private final StorageServer storageS;
    private final StorageClient1 storageC;

    public FastDfsService(TrackerServer trackerS, TrackerClient trackerC, StorageServer storageS, StorageClient1 storageC) {
        this.trackerS = trackerS;
        this.trackerC = trackerC;
        this.storageS = storageS;
        this.storageC = storageC;
    }


    public String upload(FastDFSFile file) {
        Assert.notNull(file, "Parameter 'file' must not be null");

        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("author", file.getAuthor());
        metaList[1] = new NameValuePair("name", file.getName());
        metaList[2] = new NameValuePair("md5", file.getMd5());

        try {
            return storageC.upload_file1(file.getContent(), file.getExt(), metaList);
        } catch (IOException | MyException e) {
            throw new FastDfsException("Upload file error", e);
        }
    }

    public FileInfo getFileInfo(String fileId) {
        Assert.notNull(fileId, "Parameter 'fileId' must not be null");
        try {
            return storageC.get_file_info1(fileId);
        } catch (IOException | MyException e) {
            throw new FastDfsException("Get file Info error", e);
        }
    }

    public boolean setMetaData(String fileId, Map<String, String> metaDataMap) {
        Assert.notNull(fileId, "Parameter 'fileId' must not be null");
        Assert.notNull(metaDataMap, "Parameter 'metaDataMap' must not be null");

        // 转换成需要的参数类型
        NameValuePair[] metaList = new NameValuePair[metaDataMap.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : metaDataMap.entrySet()) {
            metaList[index++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        try {
            int success = storageC.set_metadata1(fileId, metaList, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE);
            return success == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException("Set meta data error", e);
        }
    }

    public Map<String, String> setMetaData(String fileId) {
        Assert.notNull(fileId, "Parameter 'fileId' must not be null");
        try {
            NameValuePair[] metaList = storageC.get_metadata1(fileId);

            Map<String, String> rslt = new HashMap<>();
            for (NameValuePair nameValuePair : metaList) {
                rslt.put(nameValuePair.getName(), nameValuePair.getValue());
            }
            return rslt;
        } catch (IOException | MyException e) {
            throw new FastDfsException("Set meta data error", e);
        }
    }

    public byte[] download(String fileId) {
        Assert.notNull(fileId, "Parameter 'fileId' must not be null");
        try {
            return storageC.download_file1(fileId);
        } catch (IOException | MyException e) {
            throw new FastDfsException("download error", e);
        }
    }

    public boolean delete(String fileId) {
        Assert.notNull(fileId, "Parameter 'fileId' must not be null");
        try {
            int success =  storageC.delete_file1(fileId);
            return success == 0;
        } catch (IOException | MyException e) {
            throw new FastDfsException("download error", e);
        }
    }

    public String getFileUrl(String fileId) {
        Assert.notNull(fileId, "Parameter 'fileId' must not be null");
        try {
            StringBuilder url = new StringBuilder("http://")
                    .append(trackerS.getInetSocketAddress().getHostString())
                    .append(":")
                    .append(ClientGlobal.getG_tracker_http_port())
                    .append("/")
                    .append(fileId);
            if (ClientGlobal.getG_anti_steal_token()) {
                int ts = (int) (System.currentTimeMillis() / 1000);
                String token = ProtoCommon.getToken(fileId, ts, ClientGlobal.getG_secret_key());
                url.append("?token=").append(token)
                        .append("&ts=").append(ts);
            }

            return url.toString();
        } catch (IOException | NoSuchAlgorithmException | MyException e) {
            throw new FastDfsException("Get file url error", e);
        }
    }
}
