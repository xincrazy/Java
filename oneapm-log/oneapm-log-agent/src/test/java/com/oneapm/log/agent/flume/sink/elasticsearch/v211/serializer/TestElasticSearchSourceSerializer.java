package com.oneapm.log.agent.flume.sink.elasticsearch.v211.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.event.EventBuilder;
import org.elasticsearch.common.io.BytesStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Test.None;


public class TestElasticSearchSourceSerializer{
	ElasticSearchSourceSerializer elasticSearchSourceSerializer = new ElasticSearchSourceSerializer();
	
	@Test(expected=None.class)
	public void testConfigure(){
		ComponentConfiguration conf = null;
		elasticSearchSourceSerializer.configure(conf);
	}
	
	@Test(expected=None.class)
	public void testConfigure1(){
		Context context = new Context();
		elasticSearchSourceSerializer.configure(context);
	}
	
	@Test
	public void testGetContentBuilder() throws IOException{
		byte [] data = new byte[]{'-','-','-'};
		Map<String,String> headers = new HashMap<String,String>();
		Event event = EventBuilder.withBody(data, headers);
		BytesStream builder = elasticSearchSourceSerializer.getContentBuilder(event);
		Assert.assertNotNull(builder);
	}
}