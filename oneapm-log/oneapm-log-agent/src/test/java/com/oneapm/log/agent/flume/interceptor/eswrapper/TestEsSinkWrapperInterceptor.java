package com.oneapm.log.agent.flume.interceptor.eswrapper;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;



public class TestEsSinkWrapperInterceptor {
	String indexName = "indexName"; 
	String indexType = "indexType";
	EsSinkWrapperInterceptor esSinkWrapperInterceptor = new EsSinkWrapperInterceptor(indexName,indexType);
	  private Map<String, String> map = new HashMap<String, String>();


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testEsSinkWrapperInterceptor() {
		map.put("indexName", indexName);
		map.put("indexType", indexType);
	}

	@Test(expected=None.class)
	public void testInitialize() {
		esSinkWrapperInterceptor.initialize();
	}

	@Test
	public void testInterceptEvent() {
		Event event = EventBuilder.withBody("helloworld", Charset.defaultCharset());
		Event result = esSinkWrapperInterceptor.intercept(event);
		Assert.assertNotNull(result);
		
	}

	@Test
	public void testInterceptListOfEvent() {
		Event event = EventBuilder.withBody("helloworld", Charset.defaultCharset());
        List<Event> events = new ArrayList<>();
        events.add(event);
        List<Event> result = esSinkWrapperInterceptor.intercept(events);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());  
	}

	@Test(expected=None.class)
	public void testClose() {
		esSinkWrapperInterceptor .close();
	}
	
	@Test
	public void testConfigure(){
		Context context = new Context();
		EsSinkWrapperInterceptor.EsSinkWrapperBuilder esSinkWrapperBuilder =  new EsSinkWrapperInterceptor .EsSinkWrapperBuilder();
		esSinkWrapperBuilder.configure(context);
		Interceptor inte = null;
		inte = esSinkWrapperBuilder.build();
		Assert.assertNotNull(inte);
	}

}
