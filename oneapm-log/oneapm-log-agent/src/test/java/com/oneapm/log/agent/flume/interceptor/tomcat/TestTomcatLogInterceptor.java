package com.oneapm.log.agent.flume.interceptor.tomcat;

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
import org.junit.Test;
import org.junit.Test.None;
import org.powermock.reflect.Whitebox;


public class TestTomcatLogInterceptor{
	
    TomcatLogInterceptor tomcatLogInterceptor = new TomcatLogInterceptor("%{TOMCATLOG:tomcatlog}");
    
    @Test(expected=None.class)
    public void testClose(){
    	tomcatLogInterceptor.close();
    	tomcatLogInterceptor.initialize();
    }

    @Test
    public void testIntercepter(){
    	Map<String, String> paramMap = new HashMap<>();
    	paramMap.put("logType", "%{TOMCATLOG:tomcatlog}");
    	Whitebox.setInternalState(tomcatLogInterceptor, "paramMap", paramMap);
    	String eventBody = "10.128.6.60 - - [04/Jul/2016:10:51:44 +0800] \"GET /E_Menu/userlogin/restaurant.action HTTP/1.1\" 200 6736 22";
    	Event event = EventBuilder.withBody(eventBody, Charset.defaultCharset());
    	Event result = tomcatLogInterceptor.intercept(event);
    	Assert.assertNotNull(result);	
    }
    
    @Test
    public void testInterceptOfList(){
    	String eventBody = "10.128.6.60 - - [04/Jul/2016:10:51:44 +0800] \"GET /E_Menu/userlogin/restaurant.action HTTP/1.1\" 200 6736 22";
    	Event event = EventBuilder.withBody(eventBody, Charset.defaultCharset());
    	String eventBody1 = "10.128.7.24 - - [04/Aug/2016:09:39:10 +0800] \"GET /tpm/account/1/applications/4/keyTransactions/1/xray/1.json?_=1470274753731 HTTP/1.0\" 200 388 22";
    	Event event1 = EventBuilder.withBody(eventBody1, Charset.defaultCharset());
    	List<Event> events = new ArrayList<>();
    	events.add(event);
    	events.add(event1);
    	List<Event> result = tomcatLogInterceptor.intercept(events);
    	Assert.assertNotNull(result);
    	Assert.assertEquals(6, result.size());    	
    }
    
	@Test
	public void testTomcatLogInterceptorBuilder(){
		
		TomcatLogInterceptor.TomcatLogInterceptorBuilder builder = new TomcatLogInterceptor.TomcatLogInterceptorBuilder();
		Map<String,String> map = new HashMap<>();
		Context context = new Context(map);
		builder.configure(context);
		Interceptor inte = builder.build();
		Assert.assertNotNull(inte);
		
	}
	
}