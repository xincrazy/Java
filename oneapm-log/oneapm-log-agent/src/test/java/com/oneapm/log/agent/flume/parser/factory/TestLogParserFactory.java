package com.oneapm.log.agent.flume.parser.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.oneapm.log.agent.flume.interceptor.ai.consumer.v402.AiConsumerInterceptor;
import com.oneapm.log.agent.flume.interceptor.cassandra.v37.CassandraLogInterceptor;
import com.oneapm.log.agent.flume.interceptor.druid.v073.DruidLogInterceptor;
import com.oneapm.log.agent.flume.interceptor.eswrapper.EsSinkWrapperInterceptor;
import com.oneapm.log.agent.flume.interceptor.jvm.hotspot.JvmInterceptor;
import com.oneapm.log.agent.flume.interceptor.kafka.v08.topic.KafkaTopicMonitorInterceptor;
import com.oneapm.log.agent.flume.interceptor.system.ProcessAliveCheckerInterceptor;
import com.oneapm.log.agent.flume.interceptor.system.SystemUsageInterceptor;
import com.oneapm.log.agent.flume.interceptor.tomcat.TomcatLogInterceptor;
import com.oneapm.log.agent.flume.interceptor.transform.TransformInterceptor;
import com.oneapm.log.agent.flume.parser.Parser;

public class TestLogParserFactory{
	
	LogParserFactory logParserFactory = new LogParserFactory();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test 
	public void testGetInstance(){
		LogParserFactory log  = logParserFactory.getInstance();
		Assert.assertNotNull(log);
	}
	
	@Test
	public void testGetParser(){
		String metric = "event/commit/speed";
		Parser parser = logParserFactory.getParser(metric);
		Assert.assertNotNull(parser);
		String metric1 = "Unannouncing segment";
		Parser parser1 = logParserFactory.getParser(metric1);
		Assert.assertNotNull(parser1);
		String metric2 = "system/diskFileSystem";
		Parser parser2 = logParserFactory.getParser(metric2);
		Assert.assertNotNull(parser2);
		String metric3 = "redis/info";
		Parser parser3= logParserFactory.getParser(metric3);
		Assert.assertNotNull(parser3);
		String metric4 = "hello";
		Parser parser4= logParserFactory.getParser(metric4);
		Assert.assertNull(parser4);
	}
	
	@Test
	public void testGetParser1(){
		Parser parser = logParserFactory.getParser();
		Assert.assertNotNull(parser);
	}
	
	@Test
	public void testGetParserByClassName(){
		Class<TransformInterceptor> clazz = null;
		try {
			clazz = (Class<TransformInterceptor>) Class.forName("com.oneapm.log.agent.flume.interceptor.transform.TransformInterceptor");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			Parser parser = logParserFactory.getParserByClassName(clazz);
			Assert.assertNotNull(parser);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testGetParserByClassName1(){
		Class<LogParserFactory> clazz = null;
		try {
			clazz = (Class<LogParserFactory>) Class.forName("com.oneapm.log.agent.flume.parser.factory.LogParserFactory");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			logParserFactory.getParserByClassName(clazz);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testGetParserByClass(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			Class<TransformInterceptor> clazz = (Class<TransformInterceptor>) Class.forName("com.oneapm.log.agent.flume.interceptor.transform.TransformInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNotNull(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testGetParserByClass1(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("indexName", "indexName");
		map.put("indexType", "indexType");
		try {
			Class<EsSinkWrapperInterceptor> clazz = (Class<EsSinkWrapperInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.eswrapper.EsSinkWrapperInterceptor");
			Parser parser = logParserFactory.getParserByClass(clazz,map);
			Assert.assertNotNull(parser);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetParserByClass2(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("processKeyword", "processKeyword");
		map.put("processName", "processName");
		try {
			Class<ProcessAliveCheckerInterceptor> clazz = (Class<ProcessAliveCheckerInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.system.ProcessAliveCheckerInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNotNull(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@Test 
	public void testGetParserByClass3(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("appName", "appName");
		try {
			Class<JvmInterceptor> clazz = (Class<JvmInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.jvm.hotspot.JvmInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNotNull(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@Test 
	public void testGetParserByClass4(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("metricType", "metricType");
		map.put("version", "21");
		map.put("cassandraPath", "cassandraPath");
		try {
			Class<CassandraLogInterceptor> clazz = (Class<CassandraLogInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.cassandra.v21.CassandraLogInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNotNull(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testGetParserByClass5(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("metricType", "metricType");
		map.put("version", "37");
		map.put("cassandraPath", "cassandraPath");
		try {
			Class<CassandraLogInterceptor> clazz = (Class<CassandraLogInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.cassandra.v37.CassandraLogInterceptor");
			try {				
				Parser parser = logParserFactory.getParserByClass(clazz,map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetParserClass5Exception() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", "version");
		Class<CassandraLogInterceptor> clazz = (Class<CassandraLogInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.cassandra.v37.CassandraLogInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNull(parser);
			} catch (Exception e) {
			}	
	}
	
	@Test
	public void testGetParserByClass6(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "logType");
		try {
			Class<TomcatLogInterceptor> clazz = (Class<TomcatLogInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.tomcat.TomcatLogInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNotNull(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testGetParserByClass7(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("AiConsumerInterceptor", "AiConsumerInterceptor");
		try {
			Class<AiConsumerInterceptor> clazz = (Class<AiConsumerInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.ai.consumer.v402.AiConsumerInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNotNull(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testGetParserByClass8(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("topic", "topic");
		map.put("zookeeperConnect", "zookeeperConnect");
		map.put("consumerGroup", "consumerGroup");
		map.put("clusterName", "clusterName");
		try {
			Class<KafkaTopicMonitorInterceptor> clazz = (Class<KafkaTopicMonitorInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.kafka.v08.topic.KafkaTopicMonitorInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNotNull(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testGetParserByClass9(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			Class<SystemUsageInterceptor> clazz = (Class<SystemUsageInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.system.SystemUsageInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNotNull(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testGetParserByClass10(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("druidNode", "druidNode");
		map.put("tolerableRequestTime", "tolerableRequestTime");
		try {
			Class<DruidLogInterceptor> clazz = (Class<DruidLogInterceptor>)
					Class.forName("com.oneapm.log.agent.flume.interceptor.druid.v073.DruidLogInterceptor");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
				Assert.assertNotNull(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testException(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			Class<LogParserFactory> clazz = (Class<LogParserFactory>)
					Class.forName("com.oneapm.log.agent.flume.parser.factory.LogParserFactory");
			try {
				Parser parser = logParserFactory.getParserByClass(clazz,map);
			} catch (Exception e) {
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}