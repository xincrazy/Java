package com.oneapm.log.agent.flume.source.elasticsearch.v211.jmx;

import org.junit.Assert;
import org.junit.Test;


public class TestElasticsearchSourceCounter{
	
	String name = "elasticsearchSourceCounter";
	ElasticsearchSourceCounter elasticsearchSourceCounter = new ElasticsearchSourceCounter(name);
	
	@Test
	public void testSetQueueSize(){
		long queueSize = 3;
		elasticsearchSourceCounter.setQueueSize(queueSize);
		long result = elasticsearchSourceCounter.getQueueSize();
		Assert.assertEquals(3, result);
	}
	
}