package com.oneapm.log.agent.flume.parser.akka;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAkkaLoggingParser{
	
	AkkaLoggingParser akkaLoggingParser = new AkkaLoggingParser();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test
	public void testGetparser(){
		AkkaLoggingParser parser = akkaLoggingParser.getParser(); 
		Assert.assertNotNull(parser);
	}
	
	@Test 
	public void testGetDataArray(){
		String content= "2016-06-27 09:56:20.950 [Thread-4] INFO  c.o.research.alert.data.cassandra.EventServiceImpl - |[{\"feed\":\"metrics\","
				+ "\"timestamp\":\"2016-06-27 09:56:20:950\",\"service\":\"alert\",\"host\":\"10.128.7.245\",\"metric\":\"event/commit/speed\",\"value\":4565}]\n";
		String result = akkaLoggingParser.getDataArray(content);
		String str = "{\"timestamp\":\"1466992580000\",\"hostname\":\"10.128.7.245\",\"servicename\":\"alert\","
				+ "\"servicetype\":\"akka\",\"metricname\":\"event/commit/speed\",\"metricdata\":4565.0}";
		Assert.assertNotNull(result);
		Assert.assertEquals(str, result);
	}
	
	@Test
	public void testParser(){
		String content= "2016-06-27 09:56:20.950 [Thread-4] INFO  c.o.research.alert.data.cassandra.EventServiceImpl - |[{\"feed\":\"metrics\","
				+ "\"timestamp\":\"2016-06-27 09:56:20:950\",\"service\":\"alert\",\"host\":\"10.128.7.245\",\"metric\":\"event/commit/speed\",\"value\":4565}]\n";
		String result = akkaLoggingParser.parser(content);
		String str = "{\"timestamp\":\"1466992580000\",\"hostname\":\"10.128.7.245\",\"servicename\":\"alert\","
				+ "\"servicetype\":\"akka\",\"metricname\":\"event/commit/speed\",\"metricdata\":4565.0}";
		Assert.assertNotNull(result);
		Assert.assertEquals(str, result);
	}
}