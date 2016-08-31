package com.oneapm.log.agent.flume.parser.aiconsumer.v402;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.oneapm.log.agent.flume.parser.ai.consumer.v402.AIConsumerParser;


public class TestAIConsumerParser {
	
	  private static AIConsumerParser parser;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetRawSystemDataArray() {
		
		String s = "{\"timestamp\":\"2016-05-11T09:14:36.640Z\",\"hostname\":\"lzp-desktop\",\"servicename\":\"AIConsumer\","
				+ "\"metricname\":\"dataworker\",\"metricdata\":value}";
		 String str = "{\"system\":\"cpuUsage\",\"timestamp\":\"1470136246868\",\"hostname\":\"will-desktop\",\"SYS_IDLE\":\"130\",\"Total\":\"401\"}";
		AIConsumerParser aIConsumerParser = new AIConsumerParser();
		String result = aIConsumerParser.parser(str);
		System.out.println(result);
	}

	@Test
	public void testGetParser() {
		parser = new AIConsumerParser();
		Assert.assertNotNull(parser);
	}
	
	@Test 
	public void testBuildResult() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String timestamp = "2016-05-11T09:14:36.640Z";
	    String dataworker ="dataworker";
	    String value = "value";
		AIConsumerParser aIConsumerParser = new AIConsumerParser();
		Method targetMethod = AIConsumerParser.class.getDeclaredMethod("buildResult", String.class,String.class,String.class);
		targetMethod.setAccessible(true);
		String result = (String) targetMethod.invoke(aIConsumerParser,timestamp,dataworker,value );
		Assert.assertNotNull(result);
	}
}
