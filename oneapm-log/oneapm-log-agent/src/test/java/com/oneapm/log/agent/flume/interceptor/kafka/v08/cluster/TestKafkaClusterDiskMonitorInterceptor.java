package com.oneapm.log.agent.flume.interceptor.kafka.v08.cluster;

import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tang on 16-8-11.
 */
public class TestKafkaClusterDiskMonitorInterceptor {
    KafkaClusterDiskMonitorInterceptor.KafkaClusterDiskMonitorInterceptorBuilder interceptorBuilder = new  KafkaClusterDiskMonitorInterceptor.KafkaClusterDiskMonitorInterceptorBuilder();
    KafkaClusterDiskMonitorInterceptor interceptor = (KafkaClusterDiskMonitorInterceptor) interceptorBuilder.build();
    @Test
    public void intercept() throws Exception {
        Event event = EventBuilder.withBody("1", Charset.defaultCharset());
        assertNotNull(interceptor.intercept(event));
        Event event1 = EventBuilder.withBody("1", Charset.defaultCharset());
        assertNotNull(new String(interceptor.intercept(event1).getBody(),Charset.defaultCharset()));
    }

    @Test
    public void interceptList() throws Exception {
        Event event = EventBuilder.withBody("1", Charset.defaultCharset());
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        assertEquals(1,interceptor.intercept(eventList).size());
    }

}