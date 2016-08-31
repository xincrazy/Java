package com.oneapm.log.agent.flume.parser.cassandra.v37;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.oneapm.log.agent.flume.parser.cassandra.v37.CassandraLogParser;


public class TestCassandraLogParser{
	
	String metricType = "metricType";
    String cassandraPath = "cassandraPath";
	Map<String, String> paramMap = new HashMap<>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {	
	}
	
	@Test 
	public void testCassandraLogParser(){
		paramMap.put("metricType", metricType);
		paramMap.put("cassandraPath", cassandraPath);
		Assert.assertNotNull(paramMap);
	}
	
	@Test
	public void testParser() {
		String content = "ClientReadRequestLatency,Client1,Client2,Client3,Client4,"
				+ "Client5,Client6,Client7,Client8,Client9,Client10,Client11,Client12,Client13";		
		paramMap.put("metricType", "ClientReadRequestLatency");
		CassandraLogParser cassandraLogParser = new CassandraLogParser(paramMap);
		String parser = cassandraLogParser .parser(content);
		Assert.assertNotNull(parser);
	}
	
	@Test
	public void testParser1() {
		String content = "ClientWriteRequestTimeouts,Client1,Client2,Client3,Client4,Client5";		
		paramMap.put("metricType", "ClientWriteRequestTimeouts");
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
	public void testBuildResult() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		paramMap.put("metricType", "ClientReadRequestLatency");
		CassandraLogParser cassandraLogParser = new CassandraLogParser(paramMap);
		String timestamp = "2016-05-11T09:14:36.640Z";
	    String key ="keyone";
	    String value = "value";
		Class te = cassandraLogParser.getClass();
		Method targetMethod = CassandraLogParser.class.getDeclaredMethod("buildResult", String.class,String.class,String.class);
		targetMethod.setAccessible(true);
		String result = (String) targetMethod.invoke(cassandraLogParser,timestamp,key,value );
		Assert.assertNotNull(result);
	}
}