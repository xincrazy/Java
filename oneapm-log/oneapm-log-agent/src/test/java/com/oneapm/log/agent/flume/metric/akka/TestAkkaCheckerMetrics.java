package com.oneapm.log.agent.flume.metric.akka;

import org.junit.Assert;
import org.junit.Test;

public class TestAkkaCheckerMetrics{
	
	@Test
	public void testTravEnum(){
		for(AkkaCheckerMetrics e:AkkaCheckerMetrics.values()){
			Assert.assertNotNull(e.toString());
		}
	}
	
	@Test
	public void testGetAkkaCheckerContext(){
		AkkaCheckerMetrics e1 = AkkaCheckerMetrics .AKKA_COMMIT_BATCH_CHECKER;
		String result1 = e1.getAkkaCheckerContext();
		Assert.assertEquals("event/commitbatch/speed", result1);
		AkkaCheckerMetrics e2 = AkkaCheckerMetrics .AKKA_COMMIT_CHECKER;
		String result2 = e2.getAkkaCheckerContext();
		Assert.assertEquals("event/commit/speed", result2);
		AkkaCheckerMetrics e3 = AkkaCheckerMetrics .AKKA_FAILURE_BATCH_CHECKER;
		String result3 = e3.getAkkaCheckerContext();
		Assert.assertEquals("event/failurebatch/speed", result3);
		AkkaCheckerMetrics e4 = AkkaCheckerMetrics .AKKA_KAFKA_CHECKER;
		String result4 = e4.getAkkaCheckerContext();
		Assert.assertEquals("kafka/consume/speed", result4);
		AkkaCheckerMetrics e5 = AkkaCheckerMetrics .AKKA_SUCCESS_BATCH_CHECKER;
		String result5 = e5.getAkkaCheckerContext();
		Assert.assertEquals("event/successbatch/speed", result5);
	}
	
}