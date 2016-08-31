package com.oneapm.log.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostNameUtil {
	/**
	 * 使用InetAddress来返回host文件里面的Hostname
	 * 
	 * @see See <a href=
	 *      "http://stackoverflow.com/questions/7348711/recommended-way-to-get-hostname-in-java">
	 *      stackoverflow</a>
	 * @return
	 */
	public static String getHostName() {
		String result = "";
		try {
			result = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(getHostName());
	}
}
