package com.oneapm.log.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUnixTimeStampUtil {
/**
 * 将形如yyyy-MM-dd hh:ss:mm的字符串转换成标准的Unix时间
 * */
public static String getUnixTimeStamp(String string){
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm");
	Date time = null;
	try {
		time = format.parse(string);
	} catch (ParseException e) {
		e.printStackTrace();
	}
	Long result= time.getTime();
	String resultString =Long.toString(result);
	return resultString;
}
/**
 * 将形如yyyyMMdd hh:MM:ss.SSS的字符串转换成标准的Unix时间
 * @param string
 * @return
 */
public static String getAIUnixTimeStamp(String string) {
  DateFormat format = new SimpleDateFormat("yyyyMMdd hh:MM:ss.SSS");
  Date time = null;
  try {
    time = format.parse(string);
  } catch (ParseException e) {
    e.printStackTrace();
  }
  Long result = time.getTime();
  String resultString = Long.toString(result);
  return resultString;
}

public static void main(String[] args) {
	String time2 =getUnixTimeStamp("2016-05-11 09:14:39");
	System.out.println(time2);
}
}