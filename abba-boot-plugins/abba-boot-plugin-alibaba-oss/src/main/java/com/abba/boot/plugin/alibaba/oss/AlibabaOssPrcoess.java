package com.abba.boot.plugin.alibaba.oss;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
public interface AlibabaOssPrcoess {

    void progress(String objectName, double percent, long totalBytes, long currentWrittenBytes) throws AlibabaOssException;

    void success(String objectName) throws AlibabaOssException;


    void failed(String objectName) throws AlibabaOssException;
}
