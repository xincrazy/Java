package com.oneapm.log.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	/**
	 * 将形如2016-05-09T00:00:00.007Z的格式转化成UNIX时间1462752000007
	 * @author shonminh
	 * @param string
	 * @return String类型的Unix时间,形如1462752000007
	 */
	public static String formatTZ(String string) {
		String s = string;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		Date d = null;
		try {
			d = formatter.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Long.toString(d.getTime());
	}
	public static String formatTZ(String string,String pattern) {
		String s = string;
		DateFormat formatter = new SimpleDateFormat(pattern);
		Date d = null;
		try {
			d = formatter.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Long.toString(d.getTime());
	}

	/**
	 * 获取当前的Unix时间戳
	 * @return result
	 */
	public static String UnixTimeStamp(){
		long unixTime = System.currentTimeMillis();
		String result = String.valueOf(unixTime);
		return result;
	}

	public static void main(String[] args) {
		String s = formatTZ("2016-05-09T00:00:00.007Z");
		System.out.println(s);
		System.out.println(UnixTimeStamp());
		System.out.println(formatTZ("2016-05-09T00:00:00.007Z", "yyyy-MM-dd'T'HH:mm:ss.SSS"));
		System.out.println(formatTZ("2016-05-09 19:00:00,008", "yyyy-MM-dd HH:mm:ss"));
	}
}