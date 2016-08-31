package com.oneapm.log.agent.flume.parser.cassandra.v21;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestCassandraLogParser {
	
	String metricType = "metricType";
	String cassandraPath = "cassandraPath";
	Map<String, String> paramMap = new HashMap<>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testCassandraLogParser() {
		paramMap.put("metricType", metricType);
		paramMap.put("cassandraPath", cassandraPath);
		metricType = paramMap.get("metricType");
        cassandraPath = paramMap.get("cassandraPath");
        Assert.assertEquals("metricType", metricType);
        Assert.assertEquals("cassandraPath", cassandraPath);
	}

	@Test
	public void testParser() {
		String content = "ClientReadRequestLatency,Client1,Client2,Client3,Client4,Client5";		
		paramMap.put("metricType", "ClientReadRequestLatency");
		CassandraLogParser cassandraLogParser = new CassandraLogParser(paramMap);
		String parser = cassandraLogParser .parser(content);
		Assert.assertNotNull(parser);
	}
	
	@Test
	public void testParser1() {
		String content = "ClientReadRequestTimeouts,Client1,Client2,Client3,Client4,Client5";		
		paramMap.put("metricType", "ClientReadRequestTimeouts");
		CassandraLogParser cassandraLogParser = new CassandraLogParser(paramMap);
		String parser = cassandraLogParser .parser(content);
		Assert.assertNotNull(parser);
	}
	
	@Test
	public void testParser2() {
		String content = "CompactionPendingTask,Client1,Client2,Client3,Client4,Client5";		
		paramMap.put("metricType", "CompactionPendingTask");
		CassandraLogParser cassandraLogParser = new CassandraLogParser(paramMap);
		String parser = cassandraLogParser .parser(content);
		Assert.assertNotNull(parser);
	}
	
	@Test
	public void testParser3() {
		String content = "";		
		paramMap.put("metricType", "hello");
		CassandraLogParser cassandraLogParser = new CassandraLogParser(paramMap);
		String parser = cassandraLogParser .parser(content);
		Assert.assertNull(parser);
	}

	@Test
	public void testGetParser() {
		paramMap.put("metricType", "CompactionPendingTask");
		CassandraLogParser ca = CassandraLogParser.getParser(paramMap);
		Assert.assertNotNull(ca);	
	}

	@Test
	public void testGetCurrentTimeStamp() {
		CassandraLogParser cassandraLogParser = new CassandraLogParser(paramMap);
		String result =cassandraLogParser.getCurrentTimeStamp();
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testBuildResult() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		paramMap.put("metricType", "ClientReadRequestLatency");
		CassandraLogParser cassandraLogParser = new CassandraLogParser(paramMap);
		String timestamp = "2016-05-11T09:14:36.640Z";
	    String key ="keyone";
	    String value = "value";
		Method targetMethod = CassandraLogParser.class.getDeclaredMethod("buildResult", String.class,String.class,String.class);
		targetMethod.setAccessible(true);
		String result = (String) targetMethod.invoke(cassandraLogParser,timestamp,key,value );
		Assert.assertNotNull(result);
	}

}
