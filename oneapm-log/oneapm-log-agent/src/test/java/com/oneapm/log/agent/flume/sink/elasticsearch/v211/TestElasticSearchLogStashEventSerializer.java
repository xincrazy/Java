package com.oneapm.log.agent.flume.sink.elasticsearch.v211;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.event.EventBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Test.None;



public class TestElasticSearchLogStashEventSerializer{
	
	ElasticSearchLogStashEventSerializer elasticSearchLogStashEventSerializer 
	= new ElasticSearchLogStashEventSerializer();
	
	@Test
	public void testGetContentBuilder() throws IOException{
		Map<String, String> map =new HashMap<String, String>();
		map.put("keys", "keys");
		Event event = EventBuilder.withBody("hello",Charset.defaultCharset(),map);
		XContentBuilder builder = elasticSearchLogStashEventSerializer.getContentBuilder(event);
		Assert.assertNotNull(builder);
	}
	
	@Test(expected=None.class)
	public void testConfigure(){
		ComponentConfiguration conf = null;
		elasticSearchLogStashEventSerializer.configure(conf);
	}
	
	@Test(expected=None.class)
	public void testconfigure1(){
		Context context = new Context();
		elasticSearchLogStashEventSerializer.configure(context);
	}
	
	@Test
	public void testAppendHeaders() throws IOException,ClassNotFoundException, NoSuchMethodException,
	SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
	InstantiationException{
		Map<String, String> map =new HashMap<String, String>();
		Event e = EventBuilder.withBody("hello",Charset.defaultCharset(),map);
		map.put("timestamp","1466992580000");
		map.put("source", "src_path");
		map.put("type", "type");
		map.put("host", "10.128.6.169");
		map.put("src_path", "src_path");
		XContentBuilder builder = elasticSearchLogStashEventSerializer.getContentBuilder(e);
		Class clazz = elasticSearchLogStashEventSerializer.getClass();
		Method method = clazz.getDeclaredMethod("appendHeaders",XContentBuilder.class,Event.class );
		method.setAccessible(true);
		method.invoke(elasticSearchLogStashEventSerializer,builder,e);
		Assert.assertNotNull(method);
	}
	
	@Test
	public void testAppendHeaders1() throws IOException,ClassNotFoundException, NoSuchMethodException,
	SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
	InstantiationException{
		Map<String, String> map =new HashMap<String, String>();
		map.put("@timestamp","");
		map.put("@source","");
		map.put("@type", "");
		map.put("@host", "");
		map.put("@src_path","");
		map.put("timestamp","1466992580000");
		map.put("source", "source");
		map.put("type", "type");
		map.put("host", "10.128.6.169");
		map.put("src_path", "src_path");
		Event e = EventBuilder.withBody("hello",Charset.defaultCharset(),map);
		XContentBuilder builder = elasticSearchLogStashEventSerializer.getContentBuilder(e);
		Class clazz = elasticSearchLogStashEventSerializer.getClass();
		Method method = clazz.getDeclaredMethod("appendHeaders",XContentBuilder.class,Event.class );
		method.setAccessible(true);
		method.invoke(elasticSearchLogStashEventSerializer,builder,e);
		Assert.assertNotNull(method);
	}
	
}