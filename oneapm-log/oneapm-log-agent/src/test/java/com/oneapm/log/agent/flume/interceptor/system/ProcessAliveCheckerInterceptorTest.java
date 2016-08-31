package com.oneapm.log.agent.flume.interceptor.system;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by tang on 16-7-29.
 */
public class ProcessAliveCheckerInterceptorTest {
    Interceptor.Builder processAliveCheckerInterceptorBuilder = new ProcessAliveCheckerInterceptor.ProcessAliveCheckerInterceptorBuilder();
    Interceptor processAliveCheckerInterceptor=null;
    @Before
    public void  initEnv(){
        Map<String,String> content = new HashMap<>();
        content.put("processName","init");
        content.put("processKeyword","/sbin/init");
        Context context = new Context(content);
        processAliveCheckerInterceptorBuilder.configure(context);
        processAliveCheckerInterceptor = processAliveCheckerInterceptorBuilder.build();
    }

    @Test
    public void intercept() throws Exception {
        Event event = EventBuilder.withBody("", Charset.defaultCharset());
        Event returnEvent = processAliveCheckerInterceptor.intercept(event);
        assertNotNull(returnEvent);
    }

    @Test
    public void interceptList() throws Exception {
        List<Event> events = new ArrayList<>();
        Event event = EventBuilder.withBody("", Charset.defaultCharset());
        events.add(event);
        List<Event> returnEvents = processAliveCheckerInterceptor.intercept(events);
        assertNotNull(returnEvents);
        assertEquals(1,returnEvents.size());
    }

}