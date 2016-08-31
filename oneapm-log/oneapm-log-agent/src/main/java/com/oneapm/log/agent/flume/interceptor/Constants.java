package com.oneapm.log.agent.flume.interceptor;

public interface Constants {
	
	public static final String INTERCEPTOR_DEFAULT_METRICS_SEPERATOR = ",";
	
	//系统使用情况到时候用","分隔
	public static final String INTERCEPTOR_DEFAULT_SYSTEM_USAGE_SEPERATOR = ",";
	
	public static final String INTERCEPTOR_DEFAULT_KAFKA_SEPERATOR=",";

	public static final String INTERCEPTOR_DEFAULT_REDIS_SEPERATOR=",";

	public static final String INTERCEPTOR_DEFAULT_PROCESS_ALIVE_SEPERATOR = ",";
	
	 public static final String INTERCEPTOR_DIMENSION_PREFIX ="D_";

	  public static final String INTERCEPTOR_VALUE_PREFIX="V_";
}
