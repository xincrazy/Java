package com.oneapm.log.agent.flume.source.jmx;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.event.EventBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Test.None;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;


public class JMXSourceTest{
	
	 @Test(expected=None.class)
	 public void testConfigure(){
		 Context context = new Context();
	     Map<String, String> params = new HashMap<String, String>();
	     params.put("host","10.128.7.97");
	     params.put("rmi.host","10.128.7.97");
	     params.put("rmi.port","7199");
	     params.put("localhost","192.2.2.2");
	     params.put("interval","60000");
	     params.put("interval.min","100");
	     params.put("batch","10");
	     params.put("rmi.port","7199");
	     params.put("serializer.type", "com.oneapm.log.agent.flume.source.jmx.CassandraJMXSerializer");
	     context.putAll(params);
	     JMXSource source = new JMXSource();
	     JMXServiceURL url = Mockito.mock(JMXServiceURL.class);
	     Whitebox.setInternalState(source, "url", url);
	     MBeanServerConnection conn = Mockito.mock(MBeanServerConnection.class);
	     Whitebox.setInternalState(source, "conn", conn);
	     JMXConnector connector =  Mockito.mock(JMXConnector.class);
	     Whitebox.setInternalState(source, "connector", connector);
	     source.configure(context);
	 }
	 
	 @Test(expected=None.class)
	 public void testConfigure1(){
		 Context context = new Context();
	     Map<String, String> params = new HashMap<String, String>();
	     params.put("host","10.128.7.97");
	     params.put("rmi.host","10.128.7.97");
	     params.put("rmi.port","7199");
	     params.put("localhost","192.2.2.2");
	     params.put("interval","60000");
	     params.put("interval.min","100");
	     params.put("batch","10");
	     params.put("rmi.port","7199");
	     params.put("serializer.type", "com.oneapm.log.agent.flume.source.jmx.CassandraJMXSerializer");
	     context.putAll(params);
	     JMXSource source = new JMXSource();
	     source.configure(context);
	 }
	 
	 @Test(expected=None.class)
	 public void testStop(){
		 JMXSource source = new JMXSource();
		 source.stop();
	 }
	 
	 @Test(expected=None.class)
	 public void testStart(){
		 Context context = Mockito.mock(Context.class);
		 String  host = "host";
		 Integer port = 8030;
		 String  rmiHost = "rmiHost";
		 Integer rmiPort = 2;
		 Long interval = (long) 11;
		 Integer batch = 2;
		 Long minInterval = (long) 1;
		 AbstractJMXSerializer serializer = Mockito.mock(AbstractJMXSerializer.class);
		 ChannelProcessor channel = mock(ChannelProcessor.class);
		 JMXSource source = new JMXSource();
		 Whitebox.setInternalState(source, "channelProcessor", new ChannelProcessor(null));
		 Whitebox.setInternalState(source, "context", context);
		 Whitebox.setInternalState(source, "host", host);
		 Whitebox.setInternalState(source, "port", port);
		 Whitebox.setInternalState(source, "rmiHost", rmiHost);
		 Whitebox.setInternalState(source, "rmiPort", rmiPort);
		 Whitebox.setInternalState(source, "interval", interval);
		 Whitebox.setInternalState(source, "batch", batch);
		 Whitebox.setInternalState(source, "minInterval", minInterval);
		 Whitebox.setInternalState(source, "serializer", serializer);
		 Whitebox.setInternalState(source, "channel", channel);
		 source.start();
	 }
	 
	 @Test
	 public void testProcess() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
		IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		 JMXSource source = new JMXSource();
		 Event event = EventBuilder.withBody("Event/thrownAway",Charset.forName("UTF-8"));
		 List<Event> events = new ArrayList<>();
		 events.add(event);
		 Integer a = 3;
		 Class testClass = source.getClass();
	     Field field;
		try {
			List<List<Event>> batchLists = mock(List.class);
			when(batchLists.size()).thenReturn(1);
			field = testClass.getDeclaredField("process");
			field.setAccessible(true);	
			BiConsumer<List<Event>, Integer> process = mock(BiConsumer.class);
			process.accept(events,3);
			Assert.assertNotNull(field);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
	       
	 }
	 
	 @Test
	 public void testTask() throws IOException{
		 JMXSource source = new JMXSource();
		 AbstractJMXSerializer serializer = Mockito.mock(AbstractJMXSerializer.class);
		 MBeanServerConnection conn = Mockito.mock(MBeanServerConnection.class);
		 Context context = mock(Context.class);
	     Whitebox.setInternalState(source, "conn", conn);
		 List<String> metrics = mock(List.class);
		 metrics.add("hello");
		 when(metrics.size()).thenReturn(1);
		 Class testClass = source.getClass();
	     Field field;
		try {
			List<List<Event>> batchLists = mock(List.class);
			when(batchLists.size()).thenReturn(1);
			field = testClass.getDeclaredField("task");
			field.setAccessible(true);	
			Assert.assertNotNull(field);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}   
	 }
	 
	 @Test
	 public void testTask1() throws IOException{
		 Context context = Mockito.mock(Context.class);
		 String  host = "host";
		 Integer port = 8030;
		 String  rmiHost = "rmiHost";
		 Integer rmiPort = 2;
		 Long interval = (long) 11;
		 Integer batch = 2;
		 Long minInterval = (long) 1;
		 AbstractJMXSerializer serializer = Mockito.mock(AbstractJMXSerializer.class);
		 JMXSource source = new JMXSource();
		 Whitebox.setInternalState(source, "channelProcessor", new ChannelProcessor(null));
		 Whitebox.setInternalState(source, "context", context);
		 Whitebox.setInternalState(source, "host", host);
		 Whitebox.setInternalState(source, "port", port);
		 Whitebox.setInternalState(source, "rmiHost", rmiHost);
		 Whitebox.setInternalState(source, "rmiPort", rmiPort);
		 Whitebox.setInternalState(source, "interval", interval);
		 Whitebox.setInternalState(source, "batch", batch);
		 Whitebox.setInternalState(source, "minInterval", minInterval);
		 Whitebox.setInternalState(source, "serializer", serializer);
		 MBeanServerConnection conn  = mock(MBeanServerConnection.class);
		 Whitebox.setInternalState(source, "conn", conn);
		 List<String> metrics = mock(List.class);
		 List<String> list = mock(List.class);
		 Event event = EventBuilder.withBody("Event/thrownAway",Charset.forName("UTF-8"));
		 List<Event> events = new ArrayList<>();
		 events.add(event);
		 when(serializer.pullMetrics(conn, context)).thenReturn(list);
		 Assert.assertNotNull(serializer.pullMetrics(conn, context));
		 BiConsumer<List<Event>, Integer> process = mock(BiConsumer.class);
		 process.accept(events, 3);
	 }
}