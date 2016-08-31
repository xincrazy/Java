package com.oneapm.log.agent.flume.interceptor.validateJson;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * PcakgeName is com.oneapm.log.agent.flume.interceptor.validateJson.
 * Created by will on 16-8-16.
 */
public class TestValidateJsonInterceptor {
    Interceptor.Builder builder = new ValidateJsonInterceptor.ValidateJsonInterceptorBuilder();
    Interceptor validateJsonInterceptor = null;

    @Before
    public void  initEnv(){
        Map<String,String> content = new HashMap<>();
        Context context = new Context(content);
        builder.configure(context);
        validateJsonInterceptor = builder.build();
    }

    @Test
    public void intercept() throws Exception {
        Event event = EventBuilder.withBody("{r:1}", Charset.defaultCharset());
        Event returnEvent = validateJsonInterceptor.intercept(event);
        assertNotNull(returnEvent);
    }

    @Test
    public void interceptList() throws Exception {
        List<Event> events = new ArrayList<>();
        Event event = EventBuilder.withBody("{t:1}", Charset.defaultCharset());
        events.add(event);
        List<Event> returnEvents = validateJsonInterceptor.intercept(events);
        assertNotNull(returnEvents);
        assertEquals(1,returnEvents.size());
    }

}
