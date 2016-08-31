package com.oneapm.log.agent.flume.interceptor.kafka.v08.cluster;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.constants.OverallConstants;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.InetAddress;
import java.util.*;

/**
 * Created by tang on 16-8-10.
 */
public class KafkaClusterDiskMonitorInterceptor implements Interceptor ,OverallConstants {
    Logger logger = LoggerFactory.getLogger(KafkaClusterDiskMonitorInterceptor.class);
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        String eventBodyStr = new String(event.getBody(), DEFAULT_CHARSET);
        Map<String,Object> returnEvent = new HashMap<>();
        String hostname = "unknow";
        try{
            hostname = InetAddress.getLocalHost().getHostName();
        }catch (Exception e) {
            logger.debug("error get hostname",e);
        }
        returnEvent.put("timestamp",String.valueOf(System.currentTimeMillis()));
        returnEvent.put("hostname",hostname);
        returnEvent.put("servicename","KafkaClusterMonitor");
        returnEvent.put("servicetype","kafkaClusterMonitor");
        returnEvent.put("metricname","kafkaDiskRemainRatio");
        returnEvent.put("metricdata",100-Integer.valueOf(eventBodyStr));
        String returnEventJson=null;
        try {
            returnEventJson = new ObjectMapper().writeValueAsString(returnEvent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        event.setBody(returnEventJson.getBytes(DEFAULT_CHARSET));
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
            Event next = iterator.next();
            if (next == null) {
                iterator.remove();
            }
            Event current = intercept(next);
            if (current == null) {
                iterator.remove();
            }
            }
        return events ;
    }

    @Override
    public void close() {

    }

    public static class KafkaClusterDiskMonitorInterceptorBuilder implements Builder {
        @Override
        public void configure(Context context) {

        }

        @Override
        public Interceptor build() {
            return new KafkaClusterDiskMonitorInterceptor();
        }
    }
}
