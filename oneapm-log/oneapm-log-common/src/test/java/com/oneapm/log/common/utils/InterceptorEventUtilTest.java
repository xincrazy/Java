package com.oneapm.log.common.utils;

import com.google.common.base.Charsets;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link InterceptorEventUtil}.
 */
public class InterceptorEventUtilTest {
    @Test
    public void testInterceptorEventUtilTest() {
        String eventBody = "{\"timestamp\":1234567891122,\"hostname\":\"localhost\",\"servicename\":\"druid\"," +
                "\"servicetype\":\"broker\",\"servicesubtype\":\"druid_metric\",\"metricname\":\"request/time\"," +
                "\"metricdata\":301,\"metricname1\":\"diskspaceused\",\"metricdata\":12345678}";
        Event event = new EventBuilder().withBody(eventBody, Charsets.UTF_8);
        List<Event> events = InterceptorEventUtil.eventTransformer(event);

        assertEquals(events.size(), 2);
        assertEquals(new String(events.get(0).getBody(), Charsets.UTF_8),
                "{\"timestamp\":1234567891122,\"hostname\":\"localhost\",\"servicename\":\"druid\"," +
                        "\"servicetype\":\"broker\",\"servicesubtype\":\"druid_metric\",\"metricname\":\"request/time\"," +
                        "\"metricdata\":301}");
        assertEquals(new String(events.get(1).getBody(), Charsets.UTF_8),
                "{\"timestamp\":1234567891122,\"hostname\":\"localhost\",\"servicename\":\"druid\"," +
                "\"servicetype\":\"broker\",\"servicesubtype\":\"druid_metric\",\"metricname\":\"diskspaceused\"," +
                "\"metricdata\":12345678}");
    }
}
