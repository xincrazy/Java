/**
 * Project Name:oneapm-log-agent
 * File Name:ElasticsearchSourceClusterStatsBuilder.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.flume.Context;
import org.elasticsearch.action.admin.cluster.node.stats.NodeStats;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;

import com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceNodesStats.IndexingStats;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceNodesStats.OSStats;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceNodesStats.SearchStats;

/**
 * ClassName:ElasticsearchSourceClusterStatsBuilder <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public class ElasticsearchSourceClusterStatsBuilder implements ElasticsearchSourceStatsBuilder<ElasticsearchSourceNodesStats> {
    
    /**
     * @param <U>
     * @see com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceStatsBuilder#build(java.util.Optional)
     */
    @Override
    public <U> List<ElasticsearchSourceNodesStats> build(U stats) {
        NodesStatsResponse response = (NodesStatsResponse) stats;
        //
        List<ElasticsearchSourceNodesStats> resultSet = new ArrayList<ElasticsearchSourceNodesStats>();
        //
        response.forEach(node -> {
            resultSet.add(mapperNodeStats(response.getClusterName()
                                                  .value(),
                                          node));
        });
        return resultSet;
    }
    
    /**
     * mapperNodeStats: <br/>
     * 
     * @author hadoop
     * @param clusterName
     * @param node
     * @return
     * @since JDK 1.7
     */
    private ElasticsearchSourceNodesStats mapperNodeStats(String clusterName,
                                                          NodeStats node) {
        //
        ElasticsearchSourceNodesStats nodeStats = new ElasticsearchSourceNodesStats();
        //
        nodeStats.setClusterName(clusterName);
        nodeStats.setNodeName(node.getNode()
                                  .name());
        nodeStats.setHostName(node.getNode()
                                  .getHostName());
        nodeStats.setTimestamp(node.getTimestamp());
        //
        OSStats osStats = new OSStats();
        osStats.setTimestamp(node.getOs()
                                 .getTimestamp());
        osStats.setLoadAverage(node.getOs()
                                   .getLoadAverage());
        osStats.setMemUsedPercent(node.getOs()
                                      .getMem()
                                      .getUsedPercent());
        nodeStats.setOsStats(osStats);
        //
        IndexingStats indexingStats = new IndexingStats();
        indexingStats.setIndexTotal(node.getIndices()
                                        .getIndexing()
                                        .getTotal()
                                        .getIndexCount());
        indexingStats.setIndexTimeInMillis(node.getIndices()
                                               .getIndexing()
                                               .getTotal()
                                               .getIndexTimeInMillis());
        indexingStats.setIndexFailed(node.getIndices()
                                         .getIndexing()
                                         .getTotal()
                                         .getIndexFailedCount());
        nodeStats.setIndexingStats(indexingStats);
        //
        SearchStats searchStats = new SearchStats();
        searchStats.setQueryTotal(node.getIndices()
                                      .getSearch()
                                      .getTotal()
                                      .getQueryCount());
        searchStats.setQueryTimeInMillis(node.getIndices()
                                             .getSearch()
                                             .getTotal()
                                             .getQueryTimeInMillis());
        searchStats.setFetchTotal(node.getIndices()
                                      .getSearch()
                                      .getTotal()
                                      .getFetchCount());
        searchStats.setFetchTimeInMillis(node.getIndices()
                                             .getSearch()
                                             .getTotal()
                                             .getFetchTimeInMillis());
        nodeStats.setSearchStats(searchStats);
        //
        return nodeStats;
    }
    
    /**
     * @see com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceStatsBuilder#config(org.apache.flume.Context)
     */
    @Override
    public ElasticsearchSourceClusterStatsBuilder config(Context context) {
        // skip
        return this;
    }
    
}
