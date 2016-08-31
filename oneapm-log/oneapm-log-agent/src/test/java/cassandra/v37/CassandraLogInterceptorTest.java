package cassandra.v37;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.cassandra.v37.CassandraLogInterceptor;
import com.oneapm.log.common.utils.DiskUtil;
import com.oneapm.log.common.utils.HostNameUtil;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link CassandraLogInterceptor}
 */
public class CassandraLogInterceptorTest {
    @Test
    public void testCassandraLogInterceptor_Latency() {
        String eventBody = "1468842961,3,10.090808,4.845563,1.358103,4.860314,2.816159,10.090808,10.090808,10.090808,10.090808,10.090808,0.049978,0.022661,0.008536,0.003162,calls/second,milliseconds";
        CassandraLogInterceptor cassandraLogInterceptor = new CassandraLogInterceptor("ClientReadRequestLatency", "");
        Event event = cassandraLogInterceptor.intercept(new EventBuilder().withBody(eventBody, Charsets.UTF_8));
        assertEquals(new String(event.getBody(), Charsets.UTF_8),
                "{\"timestamp\":1468842961000,\"hostname\":\"" + HostNameUtil.getHostName() +"\",\"servicename\":\"Cassandra\",\"metricname\":\"CR_R_Latency_1min\",\"metricdata\":0.022661}");

        eventBody = "t,count,max,mean,min,stddev,p50,p75,p95,p98,p99,p999,mean_rate,m1_rate,m5_rate,m15_rate,rate_unit,duration_unit";
        event = cassandraLogInterceptor.intercept(new EventBuilder().withBody(eventBody, Charsets.UTF_8));
        assertNull(event);
    }

    @Test
    public void testCassandraLogInterceptor_DiskSpaceUsed() {
        String eventBody = "1468842961,7018575617";
        CassandraLogInterceptor cassandraLogInterceptor = new CassandraLogInterceptor("StorageLoad", this.getClass().getResource("/").getPath());
        Event event = cassandraLogInterceptor.intercept(new EventBuilder().withBody(eventBody, Charsets.UTF_8));
        assertEquals(new String(event.getBody(), Charsets.UTF_8),
                "{\"timestamp\":1468842961000,\"hostname\":\"" + HostNameUtil.getHostName() +"\",\"servicename\":\"Cassandra\"," +
                        "\"metricname\":\"DiskSpaceUsed\",\"metricdata\":7018575617,\"metricname1\":\"DiskFreeSpaceRate\",\"metricdata1\":"
                        + DiskUtil.getFreeDiskSpaceRate(this.getClass().getResource("/").getPath())+"}");

        eventBody = "t,count";
        event = cassandraLogInterceptor.intercept(new EventBuilder().withBody(eventBody, Charsets.UTF_8));
        assertNull(event);
    }
}
