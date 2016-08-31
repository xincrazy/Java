/**
 * Project Name:oneapm-log-agent
 * File Name:ElasticsearchSourceEventBody.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.builder;

/**
 * ClassName:ElasticsearchSourceEventBody <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public interface ElasticsearchSourceEventBody {
    
    public String toJSON();
    
    @Override
    public String toString();
    
    public byte[] toBytes();
    
}
