/**
 * Project Name:oneapm-log-agent
 * File Name:ElasticsearchSourceNodesStats.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.builder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ClassName:ElasticsearchSourceNodesStats <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
@Getter
@Setter
@ToString
public class ElasticsearchSourceNodesStats extends AbstractElasticsearchSourceEventBody {
    
    private String clusterName;
    
    private String nodeName;
    
    private Long   timestamp;
    
    private String hostName;
    
    @Setter
    @Getter
    @ToString
    public static class OSStats {
        
        private Long   timestamp;
        
        private Double loadAverage;
        
        private Short  memUsedPercent;
        
    }
    
    private OSStats osStats;
    
    @Getter
    @Setter
    @ToString
    public static class IndexingStats {
        
        private Long indexTotal;
        
        private Long indexTimeInMillis;
        
        private Long indexFailed;
        
    }
    
    private IndexingStats indexingStats;
    
    @Getter
    @Setter
    @ToString
    public static class SearchStats {
        
        private Long queryTotal;
        
        private Long queryTimeInMillis;
        
        private Long fetchTotal;
        
        private Long fetchTimeInMillis;
        
    }
    
    private SearchStats searchStats;
    
}
