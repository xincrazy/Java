package com.oneapm.log.agent.flume.interceptor.jvm.hotspot;

import junit.framework.TestCase;
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

/**
 * PcakgeName is com.oneapm.log.agent.flume.interceptor.jvm.hotspot.
 * Created by will on 16-8-2.
 */
public class JvmIterceptorTest extends TestCase{
    Interceptor jvmInterceptor = null;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //construct the context to configure the
        Map<String, String> context = new HashMap<>();
        context.put("appName", "junitTest");
        Context content = new Context(context);

        Interceptor.Builder builder = new JvmInterceptor.JvmInterceptorBuilder();
        builder.configure(content);
        jvmInterceptor = builder.build();
    }

    /**
     * Test method about the jvmInterceptor
     */
    @Test
    public void testJvmInterceptor() {
        String youngGcContext = "2016-07-29T11:20:44.202+0800: 866847.417: [GC (Allocation Failure) 2016-07-29T11:20:44.202+0800: 866847.417: [ParNew: 340765K->10629K(368640K), 0.0351048 secs] 3664353K->3337336K(6250496K), 0.0353582 secs] [Times: user=0.14 sys=0.00, real=0.04 secs] ";
        String fullGcContext = "2016-06-25T16:30:48.107+0800: 1016.343: [Full GC (Ergonomics) [PSYoungGen: 14848K->14848K(29184K)] [ParOldGen: 87426K->87424K(87552K)] 102274K->102272K(116736K), [Metaspace: 101003K->101003K(1144832K)], 0.1125602 secs] [Times: user=0.29 sys=0.00, real=0.11 secs]";
        String worongGcContext = "fasdgdsfgddfgdfgerthfghfgh";


        Event eventDataYoung = EventBuilder.withBody(youngGcContext, Charset.defaultCharset());
        Event eventDataFull = EventBuilder.withBody(fullGcContext, Charset.defaultCharset());
        Event eventDataWrong = EventBuilder.withBody(worongGcContext, Charset.defaultCharset());
        List<Event> eventList = new ArrayList<>();
        eventList.add(eventDataFull);
        eventList.add(eventDataYoung);
        eventList.add(eventDataWrong);
        assertEquals(8, jvmInterceptor.intercept(eventList).size());
    }

}
