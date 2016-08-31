package com.oneapm.log.agent.flume.parser.kafka.v08.cluster;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestKafkaClusterMonitorParser{
	
	KafkaClusterMonitorParser kafkaClusterMonitorParser = new KafkaClusterMonitorParser ();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test
	public void testParser(){
		String content = "hello";
		String parser = kafkaClusterMonitorParser .parser(content);
		Assert.assertNull(parser);
	}
}