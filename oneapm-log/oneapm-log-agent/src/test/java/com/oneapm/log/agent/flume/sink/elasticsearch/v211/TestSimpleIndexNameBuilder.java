package com.oneapm.log.agent.flume.sink.elasticsearch.v211;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.formatter.output.BucketPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;


public class TestSimpleIndexNameBuilder{

	SimpleIndexNameBuilder simpleIndexNameBuilder = new SimpleIndexNameBuilder();

	@Test
	public void testGetIndexName(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("value", "value");
		Event event = EventBuilder.withBody("body", Charset.defaultCharset(),map);
		String indexName = "indexName";
		Whitebox.setInternalState( simpleIndexNameBuilder, "indexName", indexName );
		assertEquals(simpleIndexNameBuilder.getIndexName(event), "indexName");
	}
	
	@Test
	public void testGetIndexPrefix() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		map.put("value", "value");
		Event event = EventBuilder.withBody("body", Charset.defaultCharset(),map);
		String indexName = "indexName";
		Whitebox.setInternalState( simpleIndexNameBuilder, "indexName", indexName );
		assertEquals(simpleIndexNameBuilder.getIndexPrefix(event), "indexName");
	}
	
	@Test(expected=None.class)
	public void testConfigure(){
		ComponentConfiguration conf = null;
		simpleIndexNameBuilder.configure(conf);
	}
	
	@Test
	public void testConfigure1(){
		Context context = new Context();
		simpleIndexNameBuilder.configure(context);
		Assert.assertNotNull(context);
	}
	@Test
	public void testGetIndexType(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("value", "value");
		Event event = EventBuilder.withBody("body", Charset.defaultCharset(),map);
		String result = simpleIndexNameBuilder.getIndexType(event); 
		Assert.assertNull(result);
	}
	
	@Test
	public void testGetDocument(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("value", "value");
		Event event = EventBuilder.withBody("body", Charset.defaultCharset(),map);
		Event result = simpleIndexNameBuilder.getDocument(event);
		Assert.assertNull(result);
	}
}