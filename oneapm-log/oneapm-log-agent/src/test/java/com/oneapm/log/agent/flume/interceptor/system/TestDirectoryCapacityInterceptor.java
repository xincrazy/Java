package com.oneapm.log.agent.flume.interceptor.system;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;



public class TestDirectoryCapacityInterceptor {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {  
	}

	@Test(expected=None.class)
	public void testInitialize() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		 Class<?> c = Class.forName("com.oneapm.log.agent.flume.interceptor.system.DirectoryCapacityInterceptor");   
         Constructor<?> c0=c.getDeclaredConstructor();   
         c0.setAccessible(true);   
         DirectoryCapacityInterceptor directoryCapacityInterceptor =(DirectoryCapacityInterceptor)c0.newInstance();
         directoryCapacityInterceptor.initialize();
         directoryCapacityInterceptor.close();
	}

	@Test
	public void testInterceptEvent() throws ClassNotFoundException, NoSuchMethodException, SecurityException, 
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		 Class<?> c = Class.forName("com.oneapm.log.agent.flume.interceptor.system.DirectoryCapacityInterceptor");   
         Constructor<?> c0=c.getDeclaredConstructor();   
         c0.setAccessible(true);   
         DirectoryCapacityInterceptor directoryCapacityInterceptor =(DirectoryCapacityInterceptor)c0.newInstance();
         String s = "{\"system\":\"cpuUsage\",\"timestamp\":\"1470136246868\",\"hostname\":\"will-desktop\",\"SYS_IDLE\":\"130\",\"Total\":\"401\"}";
         Event event = EventBuilder.withBody(s, Charset.defaultCharset());
         Event result = directoryCapacityInterceptor.intercept(event);
         Assert.assertNotNull(result);
         List<Event> events = new ArrayList<>();
         events.add(event);
         List<Event> resultList = directoryCapacityInterceptor.intercept(events);
         Assert.assertNotNull(resultList);
         Assert.assertEquals(1,resultList.size());
         
	}

	@Test
	public void testConfigure() {
		Context context = new Context();
		DirectoryCapacityInterceptor.DirectoryCapacityInterceptorBuilder builder = new DirectoryCapacityInterceptor.DirectoryCapacityInterceptorBuilder();
		builder.configure(context);
		Interceptor inte = null;
		inte = builder.build();
		Assert.assertNotNull(inte);
		
	}

}
