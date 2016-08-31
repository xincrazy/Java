package akka.parse;

import com.oneapm.log.agent.flume.parser.akka.AkkaLoggingParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;



/**
 * Created by root on 16-7-8.
 */
public class AkkaParserTest {

    /**
     * Test for parse the data
     */
    private static AkkaLoggingParser parser=null;
    @BeforeClass
    public static void setup(){
        parser=new AkkaLoggingParser();
    }

    @Test
    public void TestGetData() {
        String content= "2016-06-27 09:56:20.950 [Thread-4] INFO  c.o.research.alert.data.cassandra.EventServiceImpl - |[{\"feed\":\"metrics\",\"timestamp\":\"2016-06-27 09:56:20:950\",\"service\":\"alert\",\"host\":\"10.128.7.245\",\"metric\":\"event/commit/speed\",\"value\":4565}]\n";AkkaParserTest t=new AkkaParserTest();
        Assert.assertEquals(parser.getDataArray(content),"{\"timestamp\":\"1466992580000\",\"hostname\":\"10.128.7.245\",\"servicename\":\"alert\",\"servicetype\":\"akka\",\"metricname\":\"event/commit/speed\",\"metricdata\":4565.0}");
    }
    @Test
    public void TestConvertJson(){
        String content= "{\"feed\":\"metrics\",\"timestamp\":\"2016-06-27 09:56:20:950\",\"service\":\"alert\",\"host\":\"10.128.7.245\",\"metric\":\"event/commit/speed\",\"value\":4565}";
        Assert.assertEquals(parser.convertJson(content),"{\"timestamp\":\"1466992580000\",\"hostname\":\"10.128.7.245\",\"servicename\":\"alert\",\"servicetype\":\"akka\",\"metricname\":\"event/commit/speed\",\"metricdata\":4565.0}");
    }
    @Test
    public void TestGetParser(){
        AkkaLoggingParser akkaLoggingParser=AkkaLoggingParser.getParser();
        Assert.assertNotNull(akkaLoggingParser);
    }
}
