package com.oneapm.log.agent.flume.metric.druid.v073;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDruidNodeLoggingEmitterMetrics{
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test 
	public void testTravEnum(){//遍历enum
		for (DruidNodeLoggingEmitterMetrics e :DruidNodeLoggingEmitterMetrics.values()) {
			  Assert.assertNotNull(e.toString());
		}
	}
	
	@Test 
	public void testGetMetricContext(){
		DruidNodeLoggingEmitterMetrics e = DruidNodeLoggingEmitterMetrics.CACHE_DELTA_HITRATE ;
		String result = e.getMetricContext();
		Assert.assertEquals("cache/delta/hitRate", result);
		DruidNodeLoggingEmitterMetrics e1 = DruidNodeLoggingEmitterMetrics.CACHE_TOTAL_HITRATE;
		String result1 = e1.getMetricContext();
		Assert.assertEquals("cache/total/hitRate", result1);
		DruidNodeLoggingEmitterMetrics e2 = DruidNodeLoggingEmitterMetrics.COORDINATOR_SEGMENT_COUNT;
		String result2 = e2.getMetricContext();
		Assert.assertEquals("coordinator/segment/count", result2);
		DruidNodeLoggingEmitterMetrics e3 = DruidNodeLoggingEmitterMetrics.COORDINATOR_SEGMENT_SIZE;
		String result3 = e3.getMetricContext();
		Assert.assertEquals("coordinator/segment/size", result3);
		DruidNodeLoggingEmitterMetrics e4 = DruidNodeLoggingEmitterMetrics.EVENT_PROCESSED;
		String result4 = e4.getMetricContext();
		Assert.assertEquals("events/processed", result4);
		DruidNodeLoggingEmitterMetrics e5 = DruidNodeLoggingEmitterMetrics .EVENT_THROWNAWAY;
		String result5 = e5.getMetricContext();
		Assert.assertEquals("events/thrownAway", result5);
		DruidNodeLoggingEmitterMetrics e6 = DruidNodeLoggingEmitterMetrics.EVENT_UNPARSEABLE;
		String result6 = e6.getMetricContext();
		Assert.assertEquals("events/unparseable", result6);
		DruidNodeLoggingEmitterMetrics e7 = DruidNodeLoggingEmitterMetrics.REQUEST_TIME;
		String result7 = e7.getMetricContext();
		Assert.assertEquals("request/time", result7);
		
		
		
		
	}
}