package com.oneapm.log.common.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/***
	 * 对于一个targetStrs字符串数组,只要其中任一个字符串存在于字符串origStr中,则返回该字符串.
	 * @param origStr
	 * @param targetStrs
	 * @return
	 */
	public static String containStr(String origStr, String[] targetStrs) {
		for (String currentTargetStr : targetStrs) {
			if (origStr.contains(currentTargetStr)) {
				return currentTargetStr;
			}
		}
		return null;
	}

	/**
	 * 对于一个字符串origStr和一个字符串数组targetStrs, 判断targetStrs数组里面的字符串是否都在origStr中出现过
	 * 
	 * @param origStr
	 * @param targetStrs
	 * @return boolean
	 */
	public static boolean containStrAll(String origStr, String[] targetStrs) {
		for (String string : targetStrs) {
			if (!origStr.contains(string))
				return false;
		}

		return true;
	}

	/**
	 * 对于一个字符串origStr和一个字符串数组targetStrs,
	 * 判断targetStrs数组里面的字符串在origStr里面的个数是否大于等于num
	 * 
	 * @author shonminh
	 * @param origStr
	 * @param targetStrs
	 * @param num
	 * @return boolean
	 */
	public static boolean containStrPart(String origStr, String[] targetStrs, int num) {
		int sum = 0;
		for (String string : targetStrs) {
			if (origStr.contains(string))
				sum++;
		}
		if (sum >= num)
			return true;
		else
			return false;
	}

	/**
	 * 将一个字符串用指定的分隔符分隔后形成的数组
	 * 
	 * @param origStr
	 * @param seperator
	 * @return String类型的数组
	 */
	public static String[] getArray(String origStr, String seperator) {

		String[] array = origStr.split(seperator);

		String[] finalArray = new String[array.length];

		for (int i = 0; i < array.length; i++) {
			String currentStr = array[i];
			finalArray[i] = removePrePostBlankChar(currentStr);
		}

		return finalArray;
	}

	public static String removePrePostBlankChar(String origStr) {
		if (origStr != null) {
			// 去除半角空格
			origStr = origStr.trim();

			// 判断是不是全角空格
			if (origStr.startsWith("　")) {
				origStr = origStr.substring(1, origStr.length()).trim();
			}
			if (origStr.endsWith("　")) {
				origStr = origStr.substring(0, origStr.length() - 1).trim();
			}
		}

		return origStr;
	}

	/**
	 * 把两个数字字符串转化成long类型并相加,返回值String
	 */
	public static String addToString(String a, String b) {
		long la = Long.parseLong(a);
		long lb = Long.parseLong(b);
		String result = String.valueOf(la + lb);
		return result;
	}

	/**
	 * 去掉result中数字value的引号,并且处理数字字符串格式， 精确到小数点后三位。
	 * 例如： 处理前{"timestamp":"1466380865644","hostname":"guowei-desktop",
	 *              "dataSource":"metricData_druid_oneHour_longterm","metric":"events/processed","value":"412348"}
	 *       处理后{"timestamp":1466380865644,"hostname":"guowei-desktop",
	 *              "dataSource":"metricData_druid_oneHour_longterm","metric":"events/processed","value":412348}
	 * @param result
	 *          处理前value带有引号的String.
	 * @return
	 *      处理后value不带引号的String.
	 */
	public static String removeQuotationForValues(String result) {
		String[] temps =
				result.substring(1, result.length() - 1).split(":");
		DecimalFormat decimalFormat = new DecimalFormat("0.000");
		for (String firstLevel : temps) {
			String[] firstTemps = firstLevel.split(",");
			for (String secondLevel : firstTemps) {
				Matcher m =
						Pattern.compile("\"[0-9]\\d*\\.?\\d*\\s?/?\\d*\"").matcher(secondLevel);
				if (m.matches()) {
					String replaceString =
							secondLevel.substring(1, secondLevel.length() - 1);
					if (replaceString.contains(" ")) {
						replaceString = replaceString.split(" ")[0];
						replaceString = decimalFormat.format(Double.valueOf(replaceString));
					}
					else if (replaceString.contains("/")) {
						replaceString = replaceString.split("/")[0];
						replaceString = decimalFormat.format(Double.valueOf(replaceString));
					}
					result = result.replaceAll(secondLevel, replaceString);
				}

			}
		}

		return result;
	}

	public static void main(String args[]) {

		String all = "event/processed,hello world, today is good,   yes or no      ";
		String[] finalArray = StringUtil.getArray(all, ",");
		for (String str : finalArray) {
			System.out.println("#" + str + "#");
		}

	}

}