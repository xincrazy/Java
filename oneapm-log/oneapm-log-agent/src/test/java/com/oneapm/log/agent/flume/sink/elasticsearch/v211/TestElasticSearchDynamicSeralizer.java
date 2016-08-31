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


public class TestElasticSearchDynamicSeralizer{
	
	ElasticSearchDynamicSerializer elasticSearchDynamicSerializer = new ElasticSearchDynamicSerializer();
	
	@Test(expected=None.class)
	public void testConfigure1(){
		Context context = new Context();
		elasticSearchDynamicSerializer.configure(context);
	}
	
	@Test(expected=None.class)
	public void testConfigure2(){
		ComponentConfiguration conf = null;
		elasticSearchDynamicSerializer.configure(conf);
	}
	
	@Test
	public void testGetContentBuilder() throws IOException{
		Event event = EventBuilder.withBody("hello", Charset.defaultCharset());
		XContentBuilder builder = elasticSearchDynamicSerializer.getContentBuilder(event);
		Assert.assertNotNull(builder);
	}
	
	@Test
	public void testAppendHeaders() throws IOException,ClassNotFoundException, NoSuchMethodException,
	SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
	InstantiationException{
		Map<String, String> map =new HashMap<String, String>();
		map.put("keys", "keys");
		Event e = EventBuilder.withBody("hello",Charset.defaultCharset(),map);
		XContentBuilder builder = elasticSearchDynamicSerializer.getContentBuilder(e);
		Class clazz = elasticSearchDynamicSerializer.getClass();
		Method method = clazz.getDeclaredMethod("appendHeaders",XContentBuilder.class,Event.class );
		method.setAccessible(true);
		method.invoke(elasticSearchDynamicSerializer,builder,e);
		Assert.assertNotNull(method);
	}
}