package com.oneapm.log.agent.flume.interceptor.commonlog;

import com.oneapm.log.common.utils.StringUtil;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-8-25.
 */
public class TestCommonLogInterceptor {
    public static final String INTERCEPTOR_DEFAULT_METRICS_SEPERATOR = ",";
    String CommonMetricTypes = "CommonMetricTypes1,CommonMetrics2";
    CommonLogInterceptor commonLogInterceptor = new CommonLogInterceptor(CommonMetricTypes);
    private String[] commonMetrics;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void testAkkaLogInterceptor() {

        commonMetrics = StringUtil.getArray(CommonMetricTypes, INTERCEPTOR_DEFAULT_METRICS_SEPERATOR);
        Assert.assertEquals("test CommonLogInterceptor error! ", commonMetrics[0],"CommonMetricTypes1");
        Assert.assertEquals("test CommonLogInterceptor error! ", commonMetrics[1],"CommonMetrics2");
    }

    @Test(expected=Test.None.class)
    public void testClose() {
        commonLogInterceptor .close();
    }

    @Test(expected=Test.None.class)
    public void testInitialize() {
        commonLogInterceptor .initialize();
    }

    @Test
    public void testInterceptEvent() {
        Event event = EventBuilder.withBody("CommonMetricTypes1", Charset.forName("UTF-8"));
        event = commonLogInterceptor.intercept(event);
        Event event1 = EventBuilder.withBody("hello",Charset.forName("UTF-8"));
        event1 = commonLogInterceptor.intercept(event1);
        Assert.assertNotNull(event);
        Assert.assertNull(event1);
    }

    @Test
    public void testInterceptListOfEvent() {
        Event event = EventBuilder.withBody("CommonMetricTypes1",Charset.forName("UTF-8"));
        Event event2 = EventBuilder.withBody("CommonMetrics2",Charset.forName("UTF-8"));
        Event event3 = EventBuilder.withBody("hello",Charset.forName("UTF-8"));
        List<Event> events = new ArrayList<Event>();
        events.add(event);
        events.add(event2);
        events.add(event3);
        List<Event> event1 = commonLogInterceptor.intercept(events);
        Assert.assertNotNull(event1);
        Assert.assertEquals(2, event1.size());
    }

    @Test
    public void testConfigure(){
        CommonLogInterceptor.CommonLogInterceptorBuilder commonLogInterceptorBuilder = new CommonLogInterceptor.CommonLogInterceptorBuilder();
        Context context = new Context();
        commonLogInterceptorBuilder.configure(context);
        Interceptor inte = commonLogInterceptorBuilder.build();
        Assert.assertNotNull(inte);
    }
}
