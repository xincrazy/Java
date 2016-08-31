/**
 * Project Name:oneapm-log-agent
 * File Name:ElasticsearchSourceCounter.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.jmx;

import org.apache.flume.instrumentation.SourceCounter;

/**
 * ClassName:ElasticsearchSourceCounter <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public class ElasticsearchSourceCounter extends SourceCounter implements ElasticsearchSourceCounterMBean {
    
    private static final String   TIMER_KAFKA_EVENT_QUEUE_SIZE = "source.elasticsearch.event.queue.size";
    
    private static final String[] ATTRIBUTES                   = { TIMER_KAFKA_EVENT_QUEUE_SIZE };
    
    public ElasticsearchSourceCounter(String name) {
        super(name,
              ATTRIBUTES);
    }
    
    public void setQueueSize(long queueSize) {
        set(TIMER_KAFKA_EVENT_QUEUE_SIZE,
            queueSize);
    }
    
    @Override
    public long getQueueSize() {
        return get(TIMER_KAFKA_EVENT_QUEUE_SIZE);
    }
    
}
