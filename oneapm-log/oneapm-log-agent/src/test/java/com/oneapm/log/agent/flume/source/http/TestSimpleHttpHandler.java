package com.oneapm.log.agent.flume.source.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;
import org.mockito.Mockito;

public class TestSimpleHttpHandler {
	
	public static String DEFAULT_CHARSET="UTF-8";
	SimpleHttpHandler simpleHttpHandler = new SimpleHttpHandler();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testSimpleHttpHandler() {
		SimpleHttpHandler sim= new SimpleHttpHandler();
		Assert.assertNotNull(sim);
	}

	@Test
	public void testGetEvents() throws IOException,Exception{
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		BufferedReader reader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(reader);
		List<Event> result = simpleHttpHandler.getEvents(request);
		Assert.assertNotNull(result);
	}

	@Test(expected=None.class)
	public void testConfigure() {
		Context context = new Context();
		simpleHttpHandler.configure(context);
	}
	
	@Test
	public void testgetSimpleEvents() throws NoSuchMethodException, 
	SecurityException, InstantiationException, IllegalAccessException, 
	IllegalArgumentException, InvocationTargetException {	
		Event event1 = EventBuilder.withBody("helloworld",Charset.forName("UTF-8"));
		List<Event> events = new ArrayList<Event>();
		events.add(event1);
		 Class<SimpleHttpHandler> class1 = SimpleHttpHandler.class; 
		 Object instance = class1.newInstance(); 
		 Method method = class1.getDeclaredMethod("getSimpleEvents", new Class[]{List.class}); 
		 method.setAccessible(true); 
		 List<Event> result = (List<Event>) method.invoke(instance, events);  
		 Assert.assertNotNull(result);
	}
}
