package com.abba.boot.autoconfigure.oss;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
public class AlibabaOssProgressListener implements ProgressListener {
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private int percentScale = 2;
    private final String objectName;
    private final AlibabaOssPrcoess prcoess;

    public AlibabaOssProgressListener(String objectName, AlibabaOssPrcoess prcoess) {
        this.objectName = objectName;
        this.prcoess = prcoess;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        if (prcoess == null) {
            return;
        }
            // current progress bytes
            long bytes = progressEvent.getBytes();
            // progress event type
            ProgressEventType eventType = progressEvent.getEventType();

            switch (eventType) {
                // sent file total bytes
                case REQUEST_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    break;
                // request byte transfer
                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    if (this.totalBytes != -1) {
                        // Calculation percent
                        double percent = (this.bytesWritten * 100.00 / this.totalBytes);
                        BigDecimal decimal = BigDecimal.valueOf(percent).setScale(percentScale, BigDecimal.ROUND_DOWN);
                        prcoess.progress(objectName, decimal.doubleValue(), this.totalBytes, this.bytesWritten);
                    }
                    break;
                // complete
                case TRANSFER_COMPLETED_EVENT:
                    prcoess.success(objectName);
                    break;
                // failed
                case TRANSFER_FAILED_EVENT:
                    prcoess.failed(objectName);
                    break;
                default:
                    break;
            }
    }
}
