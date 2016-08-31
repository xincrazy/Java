package tomcat;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.tomcat.TomcatLogInterceptor;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link TomcatLogInterceptor}
 */
public class TomcatLogInterceptorTest {
    @Test
    public void testTomcatLogInterceptor() {
        String eventBody = "10.128.6.60 - - [04/Jul/2016:10:51:44 +0800] \"GET /E_Menu/userlogin/restaurant.action HTTP/1.1\" 200 6736 22";
        TomcatLogInterceptor tomcatLogInterceptor = new TomcatLogInterceptor("%{TOMCATLOG:tomcatlog}");
        Event event = tomcatLogInterceptor.intercept(new EventBuilder().withBody(eventBody, Charsets.UTF_8));
        assertEquals(new String(event.getBody(), Charsets.UTF_8),
                "{\"timestamp\":1467600704000,\"hostname\":\"10.128.6.60\",\"servicename\":\"Tomcat\",\"servicetype\":" +
                        "\"GET\",\"servicesubtype\":\"/E_Menu/userlogin/restaurant.action\",\"metricname1\":\"HttpStatusCode\"," +
                        "\"metricdata1\":200,\"metricname2\":\"BytesSent\",\"metricdata2\":6736,\"metricname3\":\"TimeToken\"," +
                        "\"metricdata3\":22}");

        eventBody = "10.128.7.24 - - [04/Aug/2016:09:39:10 +0800] \"GET /tpm/account/1/applications/4/keyTransactions/1/xray/1.json?_=1470274753731 HTTP/1.0\" 200 388 22";
        event = tomcatLogInterceptor.intercept(new EventBuilder().withBody(eventBody, Charsets.UTF_8));
        assertEquals(new String(event.getBody(), Charsets.UTF_8),
                "{\"timestamp\":1470274750000,\"hostname\":\"10.128.7.24\",\"servicename\":\"Tomcat\",\"servicetype\":" +
                        "\"GET\",\"servicesubtype\":\"/tpm/account/1/applications/4/keyTransactions/1/xray/1.json?_=1470274753731\"," +
                        "\"metricname1\":\"HttpStatusCode\",\"metricdata1\":200,\"metricname2\":\"BytesSent\",\"metricdata2\":388," +
                        "\"metricname3\":\"TimeToken\",\"metricdata3\":22}");
    }
}
