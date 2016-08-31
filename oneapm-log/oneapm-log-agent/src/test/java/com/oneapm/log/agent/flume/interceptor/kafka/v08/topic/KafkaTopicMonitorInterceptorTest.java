package com.oneapm.log.agent.flume.interceptor.kafka.v08.topic;

import com.oneapm.log.agent.flume.parser.kafka.v08.topic.KafkaTopicMonitorParser;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oneapm.log.agent.flume.interceptor.kafka.v08.topic.KafkaTopicMonitorInterceptor.*;
import static org.junit.Assert.*;

/**
 * Created by tang on 16-7-29.
 */

public class KafkaTopicMonitorInterceptorTest {
    KafkaTopicMonitorInterceptor mock;
    Event event;
    @Before
    public void setUp() throws Exception {
        mock = Mockito.mock(KafkaTopicMonitorInterceptor.class);
        event = EventBuilder.withBody(Mockito.anyString().getBytes());

    }

    @Test
    public void intercept() throws Exception {
        Event returnEvent= EventBuilder.withBody("1".getBytes());
        Mockito.when(mock.intercept(event)).thenReturn(returnEvent);
        assertEquals(1,mock.intercept(event).getBody().length);
    }

    @Test
    public void interceptList() throws Exception {
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        List<Event> returnEvent = new ArrayList<>();
        Mockito.when(mock.intercept(eventList)).thenReturn(returnEvent);
        assertEquals(0,mock.intercept(eventList).size());
    }

}