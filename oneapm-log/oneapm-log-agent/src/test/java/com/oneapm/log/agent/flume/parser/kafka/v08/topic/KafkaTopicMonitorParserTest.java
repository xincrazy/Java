package com.oneapm.log.agent.flume.parser.kafka.v08.topic;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;


public class KafkaTopicMonitorParserTest {
    @Test
    public void testParser() throws Exception {
        KafkaTopicMonitorParser mock = Mockito.mock(KafkaTopicMonitorParser.class);
        List<String> fakeReturnList = new ArrayList<>();
        fakeReturnList.add("pid:1,group:1,topic:1,offset:1,logsize:1,lag:1");
        Mockito.when(mock.getKafkaTopicMonitorData(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(fakeReturnList);
        String a = mock.parser("");
        System.out.println(a);
    }

}