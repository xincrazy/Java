/**
 * Project Name:oneapm-log-agent
 * File Name:AbstractElasticsearchSourceEventBody.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ClassName:AbstractElasticsearchSourceEventBody <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public abstract class AbstractElasticsearchSourceEventBody implements ElasticsearchSourceEventBody {
    
    protected static final Logger     log    = LoggerFactory.getLogger(AbstractElasticsearchSourceEventBody.class);
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * @see com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceEventBody#toJSON()
     */
    @Override
    public String toJSON() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Convert event body to JSON failed: {}",
                      e);
            return "{}";
        }
    }
    
    /**
     * @see com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceEventBody#toBytes()
     */
    @Override
    public byte[] toBytes() {
        try {
            return mapper.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            log.error("Convert event body to byte[] failed: {}",
                      e);
            return new byte[0];
        }
    }
    
}
