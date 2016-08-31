package com.oneapm.log.common.utils;

import java.io.File;

public class FileUtil {

	public static String getFolderPath(String fullFilePath) {
		if (fullFilePath != null)
			return fullFilePath.substring(0,
					fullFilePath.lastIndexOf(File.separator));

		return null;
	}

	public static String getFileName(String fullFilePath) {
		if (fullFilePath != null)
			return fullFilePath.substring(
					fullFilePath.lastIndexOf(File.separator) + 1,
					fullFilePath.length());

		return null;
	}

	public static void main(String args[]) {

		String fullFilePath = "/home/hadoop/temp/flume/input.log";
		System.out.println(FileUtil.getFolderPath(fullFilePath));
		System.out.println(FileUtil.getFileName(fullFilePath));

	}

}
