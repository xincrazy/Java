package druid;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.druid.v073.DruidLogInterceptor;
import com.oneapm.log.common.utils.HostNameUtil;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link DruidLogInterceptor}.
 */
public class DruidLogInterceptorTest {

    @BeforeClass
    public static void setUpBeforeClass() {

    }

    @Test
    public void testDruidLogInterceptor() {
        String druidMetricTypes = "cache/delta/hitRate, cache/total/hitRate, request/time";
        DruidLogInterceptor druidLogInterceptor = new DruidLogInterceptor(druidMetricTypes, "broker", "300");
        String eventBody = "2016-06-15 07:18:23,763 INFO c.m.e.c.LoggingEmitter [MonitorScheduler-0] " +
                "Event [{\"feed\":\"metrics\",\"timestamp\":\"2016-06-15T07:18:23.763Z\",\"service\":\"broker\",\"host\"" +
                ":\"10.173.34.226:8080\",\"metric\":\"cache/total/hitRate\",\"value\":0.564091266576/2216,\"user1\":\"druid_metric\"}]";
        Event originalEvent = new EventBuilder().withBody(eventBody, Charsets.UTF_8);
        Event eventAfterInterceptor = druidLogInterceptor.intercept(originalEvent);
        String expectedResult = "{\"timestamp\":1465975103763,\"hostname\":\"" + HostNameUtil.getHostName() +"\",\"servicename\":\"druid\",\"servicetype\":\"broker\",\"servicesubtype\":\"druid_metric\",\"metricname\":\"cache/total/hitRate\",\"metricdata\":0.564}";

        assertEquals(new String(eventAfterInterceptor.getBody(), Charsets.UTF_8), expectedResult);

        druidMetricTypes = "completed walk through";
        druidLogInterceptor = new DruidLogInterceptor(druidMetricTypes, "realtime", "300");
        eventBody = "2016-08-12 00:07:56,804 INFO i.d.s.IndexMerger [druid_metric_10minute-incremental-persist] " +
                "outDir[/oneapm/data/druid/basePersist/druid_metric_10minute/2016-08-11T23:00:00.000Z_2016-08-12T00:00:00.000Z/42/v8-tmp] " +
                "completed walk through of 3,663 rows in 35 millis.";
        originalEvent = new EventBuilder().withBody(eventBody, Charsets.UTF_8);
        eventAfterInterceptor = druidLogInterceptor.intercept(originalEvent);
        assertEquals(new String(eventAfterInterceptor.getBody(), Charsets.UTF_8),
                "{\"timestamp\":1470960476804,\"hostname\":\"" + HostNameUtil.getHostName() +"\",\"servicename\":\"druid\",\"servicetype\"" +
                        ":\"realtime\",\"servicesubtype\":\"druid_metric_10minute-incremental\",\"metricname\":" +
                        "\"indexMerger/walked/rate\",\"metricdata\":104}");

        druidMetricTypes = "cache/delta/hitRate, cache/total/hitRate, request/time";
        druidLogInterceptor = new DruidLogInterceptor(druidMetricTypes, "broker", "300");
        eventBody = "2016-06-15 07:18:23,763 INFO c.m.e.c.LoggingEmitter [MonitorScheduler-0] " +
                "Event [{\"feed\":\"metrics\",\"timestamp\":\"2016-06-15T07:18:23.763Z\",\"service\":\"broker\",\"host\"" +
                ":\"10.173.34.226:8080\",\"metric\":\"cache/total/hitRate\",\"value\":0.564091266576/2216,\"user1\":\"druid_metric\",\"user2\":\"true\"}]";
        originalEvent = new EventBuilder().withBody(eventBody, Charsets.UTF_8);
        eventAfterInterceptor = druidLogInterceptor.intercept(originalEvent);
        assertEquals(new String(eventAfterInterceptor.getBody(), Charsets.UTF_8),
                "{\"timestamp\":1465975103763,\"hostname\":\"" + HostNameUtil.getHostName() +"\",\"servicename\":\"druid\"," +
                        "\"servicetype\":\"broker\",\"servicesubtype\":\"druid_metric\",\"metricname\":\"cache/total/hitRate\"," +
                        "\"metricdata\":0.564}");

        druidLogInterceptor = new DruidLogInterceptor(druidMetricTypes, "realtime-query", "300");
        eventBody = "2016-08-24T11:32:46.498Z\t10.173.34.226\t{\"queryType\":\"topN\",\"dataSource\":{\"type\":\"table\"," +
                "\"name\":\"druid_metric\"},\"dimension\":{\"type\":\"default\",\"dimension\":\"metricId\",\"outputName\":" +
                "\"metricId\"},\"metric\":{\"type\":\"LegacyTopNMetricSpec\",\"metric\":\"doubleSum_num2_/_doubleSum_num1\"}," +
                "\"threshold\":1000,\"intervals\":{\"type\":\"segments\",\"segments\":[{\"itvl\":" +
                "\"2016-08-24T11:29:00.000Z/2016-08-24T11:30:00.000Z\",\"ver\":\"2016-08-24T11:20:00.000Z\",\"part\":1}," +
                "{\"itvl\":\"2016-08-24T11:30:00.000Z/2016-08-24T11:31:00.000Z\",\"ver\":\"2016-08-24T11:30:00.000Z\",\"part\":1}]}," +
                "\"filter\":{\"type\":\"and\",\"fields\":[{\"type\":\"selector\",\"dimension\":\"applicationId\",\"value\":\"2271014\"}," +
                "{\"type\":\"or\",\"fields\":[{\"type\":\"selector\",\"dimension\":\"metricTypeId\",\"value\":\"-51987850\"}," +
                "{\"type\":\"selector\",\"dimension\":\"metricTypeId\",\"value\":\"-51987853\"},{\"type\":\"selector\",\"dimension\":" +
                "\"metricTypeId\",\"value\":\"-51987845\"}]},{\"type\":\"or\",\"fields\":[{\"type\":\"selector\",\"dimension\":" +
                "\"metricId\",\"value\":\"798515340\"},{\"type\":\"selector\",\"dimension\":\"metricId\",\"value\":\"798515210\"}," +
                "{\"type\":\"selector\",\"dimension\":\"metricId\",\"value\":\"798515183\"}]}]},\"granularity\":{\"type\":" +
                "\"duration\",\"duration\":120000,\"origin\":\"1970-01-01T00:01:00.000Z\"},\"aggregations\":[{\"type\":\"max\",\"name\":" +
                "\"num5\",\"fieldName\":\"num5\"},{\"type\":\"doubleSum\",\"name\":\"num1\",\"fieldName\":\"num1\"},{\"type\":" +
                "\"doubleSum\",\"name\":\"num2\",\"fieldName\":\"num2\"},{\"type\":\"doubleSum\",\"name\":\"num3\",\"fieldName\":" +
                "\"num3\"},{\"type\":\"doubleSum\",\"name\":\"num6\",\"fieldName\":\"num6\"},{\"type\":\"min\",\"name\":\"num4\"," +
                "\"fieldName\":\"num4\"}],\"postAggregations\":[{\"type\":\"arithmetic\",\"name\":\"doubleSum_num2_/_doubleSum_num1\"," +
                "\"fn\":\"/\",\"fields\":[{\"type\":\"fieldAccess\",\"name\":null,\"fieldName\":\"num2\"},{\"type\":\"fieldAccess\"," +
                "\"name\":null,\"fieldName\":\"num1\"}],\"ordering\":null}],\"context\":{\"finalize\":false,\"queryId\":" +
                "\"2774a349-426e-400e-bd40-6602aa9b5aa1\",\"timeout\":300000}}\t{\"request/time\":460,\"success\":true}";
        originalEvent = new EventBuilder().withBody(eventBody, Charsets.UTF_8);
        eventAfterInterceptor = druidLogInterceptor.intercept(originalEvent);
        assertEquals(new String(eventAfterInterceptor.getBody(), Charsets.UTF_8),
                "{\"timestamp\":1472038366498,\"hostname\":\"" + HostNameUtil.getHostName() +"\",\"servicename\":\"druid\",\"servicetype\":" +
                        "\"realtime-query\",\"servicesubtype\":\"druid_metric\",\"servicesubtype1\":\"topN\",\"metricname\":" +
                        "\"request/time\",\"metricdata\":460}");
    }
}
