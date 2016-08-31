package com.oneapm.log.agent.flume.metric.system.linux;

import org.junit.Assert;
import org.junit.Test;

public class TestSystemUsageMetrics{
	
	@Test
	public void testTravenum(){
		for(SystemUsageMetrics e:SystemUsageMetrics.values()){
			Assert.assertNotNull(e);
		}
	}
	
	@Test
	public void testGetSystemUsageContext(){
		SystemUsageMetrics s0 = SystemUsageMetrics .SYSTEM_CPU_USAGE;
		String r0 = s0.getSystemUsageContext();
		Assert.assertEquals("system/cpuUsage", r0);
		SystemUsageMetrics s1 = SystemUsageMetrics .SYSTEM_DISK_FILE_SYSTEM;
		String r1 = s1.getSystemUsageContext();
		Assert.assertEquals("system/diskFileSystem", r1);
		SystemUsageMetrics s2 = SystemUsageMetrics .SYSTEM_MEMORY;
		String r2 = s2.getSystemUsageContext();
		Assert.assertEquals("system/memory", r2);
	}
}