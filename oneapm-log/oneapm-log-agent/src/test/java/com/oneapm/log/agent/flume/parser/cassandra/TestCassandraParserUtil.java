package com.oneapm.log.agent.flume.parser.cassandra;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestCassandraParserUtil{
	
	CassandraParserUtil cassandraParserUtil = new CassandraParserUtil();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {	
	}
	@Test
	public void testGetResultKeyByMetricType(){
		String metricType = "ClientReadRequestLatency";
		String result = cassandraParserUtil.getResultKeyByMetricType(metricType);
		Assert.assertNotNull(result);
		String metricType1 = "ClientWriteRequestLatency";
		String result1 = cassandraParserUtil.getResultKeyByMetricType(metricType1);
		Assert.assertNotNull(result1);
		String metricType2 = "ClientReadRequestTimeouts";
		String result2 = cassandraParserUtil.getResultKeyByMetricType(metricType2);
		Assert.assertNotNull(result2);
		String metricType3 = "ClientWriteRequestTimeouts";
		String result3 = cassandraParserUtil.getResultKeyByMetricType(metricType3);
		Assert.assertNotNull(result3);
		String metricType4 = "ClientReadRequestUnavailables";
		String result4 = cassandraParserUtil.getResultKeyByMetricType(metricType4);
		Assert.assertNotNull(result4);
		String metricType5 = "ClientWriteRequestUnavailables";
		String result5 = cassandraParserUtil.getResultKeyByMetricType(metricType5);
		Assert.assertNotNull(result5);
		String metricType6 = "TotalCompactionsCompleted";
		String result6 = cassandraParserUtil.getResultKeyByMetricType(metricType6);
		Assert.assertNotNull(result6);
		String metricType7 = "StorageLoad";
		String result7 = cassandraParserUtil.getResultKeyByMetricType(metricType7);
		Assert.assertNotNull(result7);
		String metricType8 = "CompactionPendingTask";
		String result8 = cassandraParserUtil.getResultKeyByMetricType(metricType8);
		Assert.assertNotNull(result3);
		String metricType9 = "hello";
		String result9 = cassandraParserUtil.getResultKeyByMetricType(metricType9);
		Assert.assertNotNull(result9);	
	}
}