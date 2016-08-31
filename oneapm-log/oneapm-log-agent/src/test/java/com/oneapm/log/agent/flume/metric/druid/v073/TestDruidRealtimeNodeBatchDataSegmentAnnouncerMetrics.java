package com.oneapm.log.agent.flume.metric.druid.v073;

import org.junit.Assert;
import org.junit.Test;


public class TestDruidRealtimeNodeBatchDataSegmentAnnouncerMetrics{
	
	@Test
	public void testTravEnum(){
		for(DruidRealtimeNodeBatchDataSegmentAnnouncerMetrics d :DruidRealtimeNodeBatchDataSegmentAnnouncerMetrics.values()){
			Assert.assertNotNull(d.toString());
		}
	}
	
	@Test
	public void testGetMetricContext(){
		DruidRealtimeNodeBatchDataSegmentAnnouncerMetrics e1 = DruidRealtimeNodeBatchDataSegmentAnnouncerMetrics.UNANNOUNCING_SEGMENT;
		String result = e1.getMetricContext();
		Assert.assertEquals("Unannouncing segment", result);
	}
	
}