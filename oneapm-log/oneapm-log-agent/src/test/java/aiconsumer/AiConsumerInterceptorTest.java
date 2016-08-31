package aiconsumer;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.ai.consumer.v402.AiConsumerInterceptor;
import com.oneapm.log.common.utils.HostNameUtil;
import com.oneapm.log.common.utils.TimeUtil;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test Class for {@link AiConsumerInterceptor}
 */
public class AiConsumerInterceptorTest {
    private static String today;
    @BeforeClass
    public static void setUpBeforeClass() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        today = format.format(new Date());
    }

    @Test
    public void testAiConsumerInterceptor() {
        String eventBody = "14:06:19.515 com.blueocn.dconsumer.kafka.Counter$1 INFO  metric-data-worker processed messages 2486 / min.";
        AiConsumerInterceptor aiConsumerInterceptor = new AiConsumerInterceptor();
        Event event = aiConsumerInterceptor.intercept(new EventBuilder().withBody(eventBody, Charsets.UTF_8));

        String time = today + " " + "14:06:19.515";
        String unixTimeStamp = TimeUtil.formatTZ(time,"yyyy-MM-dd hh:mm:ss.SSS");

        assertEquals(new String(event.getBody(), Charsets.UTF_8),
                "{\"timestamp\":" + unixTimeStamp + ",\"hostname\":\"" + HostNameUtil.getHostName() +"\",\"servicename\":\"AIConsumer\",\"metricname\":\"metric-data-worker\",\"metricdata\":2486}");

        eventBody = "14:07:19.515 com.blueocn.dconsumer.kafka.Counter$1 INFO  metric-data-worker processed messages 2444 / hour.";
        event = aiConsumerInterceptor.intercept(new EventBuilder().withBody(eventBody, Charsets.UTF_8));

        assertNull(event);
    }
}
