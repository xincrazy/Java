package com.oneapm.log.common.constants;

import java.nio.charset.Charset;

public interface OverallConstants {

	public static final String DEFAULT_CHARSET_NAME = "UTF-8";
	public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
	// Default timezone is Beijing time. GMT+8:00   Asia/Shanghai
	// Usage: TimeZone.getTimeZone(DEFAULT_TIMEZONE)
	public static final String DEFAULT_TIMEZONE = "GMT+8";

}
