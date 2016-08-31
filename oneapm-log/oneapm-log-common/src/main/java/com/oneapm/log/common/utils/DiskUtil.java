package com.oneapm.log.common.utils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Util class provides some hard disk calculation functions.
 *
 */
public class DiskUtil {
    /**
     * Get the free disk space rate of the partition for the given path.
     * @return
     */
    public static String getFreeDiskSpaceRate(String path) {
        File diskPartition = new File(path);

        long totalDiskSpace = diskPartition.getTotalSpace();
        long freeDiskSpace = diskPartition.getFreeSpace();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(freeDiskSpace*1.0/totalDiskSpace);
    }
}
