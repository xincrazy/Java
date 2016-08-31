package com.oneapm.log.agent.flume.interceptor.kafka.v08.cluster;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 暂时不需要interceptor，暂时添加空的interceptor
 * @author  tangyang
 *
 */
public class KafkaClusterMonitorInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(KafkaClusterMonitorInterceptor.class);

    private KafkaClusterMonitorInterceptor(){}

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {

        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        return events;
    }

    @Override
    public void close() {

    }

    public static class KafkaClusterMonitorInterceptorBuilder implements Builder{
        @Override
        public void configure(Context context) {

        }

        @Override
        public Interceptor build() {
            return new KafkaClusterMonitorInterceptor();
        }
    }


}
