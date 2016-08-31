/**
 * Project Name:oneapm-log-agent
 * File Name:ElasticsearchSourceCounterMBean.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.jmx;

/**
 * ClassName:ElasticsearchSourceCounterMBean <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public interface ElasticsearchSourceCounterMBean {
    
    long getQueueSize();
    
    long getStartTime();
    
    long getStopTime();
    
    String getType();
    
}
