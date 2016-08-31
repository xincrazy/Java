/**
 * Project Name:oneapm-log-agent
 * File Name:ElasticsearchSourceUniform.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211.builder
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.builder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ClassName:ElasticsearchSourceUniform <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
@ToString
public class ElasticsearchSourceUniform extends AbstractElasticsearchSourceEventBody {
    
    private Long   timestamp;
    
    private String hostname;
    
    private String servicename;
    
    private String servicetype;
    
    private String metricname;
    
    private Double metricdata;
    
}
