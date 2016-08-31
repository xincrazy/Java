/**
 * Project Name:oneapm-log-agent
 * File Name:ElasticsearchSourceStats.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.builder;

import java.util.List;

import org.apache.flume.Context;

/**
 * ClassName:ElasticsearchSourceStats <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public interface ElasticsearchSourceStatsBuilder<T extends ElasticsearchSourceEventBody> {
    
    /**
     * config: <br/>
     * 
     * @author hadoop
     * @param context
     * @return
     * @since JDK 1.8
     */
    public ElasticsearchSourceStatsBuilder<T> config(Context context);
    
    /**
     * build: <br/>
     * 
     * @author hadoop
     * @param stats
     * @return
     * @since JDK 1.8
     */
    public <U> List<T> build(U stats);
    
}
