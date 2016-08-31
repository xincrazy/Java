package com.oneapm.log.agent.flume.metric.system.linux;

public enum SystemUsageMetrics {
	SYSTEM_DISK_FILE_SYSTEM("system/diskFileSystem"),SYSTEM_MEMORY("system/memory"),SYSTEM_CPU_USAGE("system/cpuUsage");
	private String context;
	public String getSystemUsageContext(){
		return this.context;
	}
	SystemUsageMetrics(String context){
	this.context = context;	
	}
	
}
