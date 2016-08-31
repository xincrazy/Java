/**
 * Project Name:oneapm-log-agent
 * File Name:ElasticsearchSourceUniformBuilder.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211.builder
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.Getter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.flume.Context;
import org.elasticsearch.action.admin.cluster.node.stats.NodeStats;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.monitor.jvm.JvmStats.BufferPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterators;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.util.ElasticsearchSourceConstants;

/**
 * ClassName:ElasticsearchSourceUniformBuilder <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
public class ElasticsearchSourceUniformBuilder implements ElasticsearchSourceStatsBuilder<ElasticsearchSourceUniform> {
    
    public static final Logger  log            = LoggerFactory.getLogger(ElasticsearchSourceUniformBuilder.class);
    
    private final List<Metrics> nodesMetrics   = new ArrayList<ElasticsearchSourceUniformBuilder.Metrics>();
    
    private final List<Metrics> indicesMetrics = new ArrayList<ElasticsearchSourceUniformBuilder.Metrics>();
    
    /**
     * @see com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceStatsBuilder#build(java.lang.Object)
     */
    @Override
    public <U> List<ElasticsearchSourceUniform> build(U stats) {
        //
        List<ElasticsearchSourceUniform> resultSet = new ArrayList<ElasticsearchSourceUniform>();
        // traverse all Metrics and build all nodes' Metric data to a list which is add-all into
        // resultSet
        if (stats != null) {
            //
            Optional<List<Metrics>> metrics;
            metrics = NodesStatsResponse.class.isAssignableFrom(stats.getClass())
                                                                                 ? Optional.ofNullable(nodesMetrics)
                                                                                 : IndicesStatsResponse.class.isAssignableFrom(stats.getClass())
                                                                                                                                                ? Optional.ofNullable(indicesMetrics)
                                                                                                                                                : Optional.empty();
            // get all metrics from current response
            metrics.ifPresent(mlst -> mlst.forEach(metric -> resultSet.addAll(metric.getAsUniform(stats))));
            // register current response to context for further computing
            BacktraceContext.set(stats);
        }
        return resultSet;
    }
    
    /**
     * ClassName: ResponseWrapper <br/>
     * Function: <br/>
     * date: <br/>
     *
     * @author hadoop
     * @version ElasticsearchSourceUniformBuilder@param <T>
     * @since JDK 1.8
     */
    private static class ResponseWrapper<T> {
        
        @Getter
        private T       response;
        
        @Getter
        private Instant timestamp;
        
        /**
         * Creates a new instance of ResponseWrapper.
         * 
         * @param response
         */
        public ResponseWrapper(T response) {
            this.response = response;
            this.timestamp = Instant.now();
        }
        
    }
    
    /**
     * ClassName: BacktraceContext <br/>
     * Function: <br/>
     * date: <br/>
     *
     * @author hadoop
     * @version ElasticsearchSourceUniformBuilder
     * @since JDK 1.8
     */
    private static class BacktraceContext {
        
        private static ResponseWrapper<NodesStatsResponse>   nodesBack   = null;
        
        private static ResponseWrapper<IndicesStatsResponse> indicesBack = null;
        
        /**
         * set: <br/>
         * 
         * @author hadoop
         * @param back
         * @since JDK 1.8
         */
        public static <U> void set(U back) {
            // back is sub-type of NodesStatsResponse
            if (NodesStatsResponse.class.isAssignableFrom(back.getClass())) {
                nodesBack = new ResponseWrapper<NodesStatsResponse>((NodesStatsResponse) back);
                return;
            }
            // back is sub-type of IndicesStatsResponse
            if (IndicesStatsResponse.class.isAssignableFrom(back.getClass())) {
                indicesBack = new ResponseWrapper<IndicesStatsResponse>((IndicesStatsResponse) back);
                return;
            }
        }
        
        /**
         * getBackNodeStats: <br/>
         * 
         * @author hadoop
         * @param current
         * @param nodeId
         * @return
         * @since JDK 1.8
         */
        private static Optional<Pair<NodeStats, NodeStats>> getBackNodeStats(NodesStatsResponse current,
                                                                             String nodeId) {
            //
            NodesStatsResponse back = nodesBack.getResponse();
            if (back == null ||
                current == null) {
                log.warn("No backtrace response instance found.");
                return Optional.empty();
            }
            //
            NodeStats backStats = back.getNodesMap()
                                      .get(nodeId);
            NodeStats currentStats = current.getNodesMap()
                                            .get(nodeId);
            if (backStats == null ||
                currentStats == null) {
                return Optional.empty();
            }
            //
            return Optional.ofNullable(ImmutablePair.of(backStats,
                                                        currentStats));
        }
        
        /**
         * forNodesIndexingSpeed: <br/>
         * 
         * @author hadoop
         * @param current
         * @param nodeId
         * @return
         * @since JDK 1.8
         */
        public static Double forNodesIndexingSpeed(NodesStatsResponse current,
                                                   String nodeId) {
            BigDecimal result = BigDecimal.ZERO;
            //
            Optional<Pair<NodeStats, NodeStats>> pair = getBackNodeStats(current,
                                                                         nodeId);
            if (pair.isPresent()) {
                //
                NodeStats backStats = pair.get()
                                          .getLeft();
                NodeStats currentStats = pair.get()
                                             .getRight();
                //
                BigDecimal indexCount = new BigDecimal(currentStats.getIndices()
                                                                   .getIndexing()
                                                                   .getTotal()
                                                                   .getIndexCount() -
                                                       backStats.getIndices()
                                                                .getIndexing()
                                                                .getTotal()
                                                                .getIndexCount());
                BigDecimal timetotal = new BigDecimal((currentStats.getTimestamp() - backStats.getTimestamp()) / 1000);
                //
                try {
                    result = indexCount.divide(timetotal,
                                               5,
                                               RoundingMode.HALF_UP);
                    log.debug("Compute indexing speed, indexCount:[{}], timeTotal:[{}]s, speed:[{}]/s",
                              new Object[] {indexCount, timetotal, result.doubleValue()});
                } catch (Exception e) {
                    log.error("Compute indexing speed by divide indexCount / timetotal failed: {}",
                              e);
                    return BigDecimal.ZERO.doubleValue();
                }
            }
            // return speed or 0.0
            return result.compareTo(BigDecimal.ZERO) > 0
                                                        ? result.doubleValue()
                                                        : BigDecimal.ZERO.doubleValue();
        }
        
        /**
         * forNodesSearchLatency: <br/>
         * 
         * @author hadoop
         * @param current
         * @param nodeId
         * @return
         * @since JDK 1.8
         */
        public static Double forNodesSearchLatency(NodesStatsResponse current,
                                                   String nodeId) {
            BigDecimal result = BigDecimal.ZERO;
            //
            Optional<Pair<NodeStats, NodeStats>> pair = getBackNodeStats(current,
                                                                         nodeId);
            if (pair.isPresent()) {
                //
                NodeStats backStats = pair.get()
                                          .getLeft();
                NodeStats currentStats = pair.get()
                                             .getRight();
                //
                BigDecimal queryCount = new BigDecimal(currentStats.getIndices()
                                                                   .getSearch()
                                                                   .getTotal()
                                                                   .getQueryCount() -
                                                       backStats.getIndices()
                                                                .getSearch()
                                                                .getTotal()
                                                                .getQueryCount());
                BigDecimal timetotal = new BigDecimal(currentStats.getIndices()
                                                                  .getSearch()
                                                                  .getTotal()
                                                                  .getQueryTimeInMillis() -
                                                      backStats.getIndices()
                                                               .getSearch()
                                                               .getTotal()
                                                               .getQueryTimeInMillis());
                //
                if (queryCount.compareTo(BigDecimal.ZERO) == 0) {
                    return BigDecimal.ZERO.doubleValue();
                }
                //
                try {
                    result = timetotal.divide(queryCount,
                                              5,
                                              RoundingMode.HALF_UP);
                    log.debug("Compute search latency, queryCount:[{}], timeTotal:[{}]s, latency:[{}]ms/q",
                              new Object[] {queryCount, timetotal, result.doubleValue()});
                } catch (Exception e) {
                    log.error("Compute search latency by divide timetotal / queryCount failed: {}",
                              e);
                    return BigDecimal.ZERO.doubleValue();
                }
            }
            // return speed or 0.0
            return result.compareTo(BigDecimal.ZERO) > 0
                                                        ? result.doubleValue()
                                                        : BigDecimal.ZERO.doubleValue();
        }
        
        /**
         * forDivideMetrics: <br/>
         * get two number by function, then calc the divide <br>
         * 
         * @author hadoop
         * @param current
         * @param element
         * @param getDivide
         * @param getBeDivided
         * @return
         * @since JDK 1.8
         */
        @SuppressWarnings("unchecked")
        private static <T> BigDecimal forDivideMetrics(T current,
                                                       String element,
                                                       BiFunction<ResponseWrapper<T>, String, BigDecimal> getDivide,
                                                       BiFunction<ResponseWrapper<T>, String, BigDecimal> getBeDivided) {
            try {
                // nodes
                if (NodesStatsResponse.class.isAssignableFrom(current.getClass())) {
                    Objects.requireNonNull(nodesBack,
                                           "May be first time, or may be set function failed, the nodes back instance is null");
                    //
                    BigDecimal b1 = getDivide.apply(new ResponseWrapper<T>(current),
                                                    element);
                    BigDecimal b2 = getDivide.apply((ResponseWrapper<T>) nodesBack,
                                                    element);
                    BigDecimal divide = b1.compareTo(b2) > 0
                                                            ? b1.subtract(b2)
                                                            : BigDecimal.ZERO;
                    //
                    b1 = getBeDivided.apply(new ResponseWrapper<T>(current),
                                            element);
                    b2 = getBeDivided.apply((ResponseWrapper<T>) nodesBack,
                                            element);
                    BigDecimal beDivided = b1.compareTo(b2) > 0
                                                               ? b1.subtract(b2)
                                                               : BigDecimal.ZERO;
                    return divide.divide(beDivided,
                                         5,
                                         RoundingMode.HALF_UP);
                }
                // indices
                if (IndicesStatsResponse.class.isAssignableFrom(current.getClass())) {
                    Objects.requireNonNull(indicesBack,
                                           "May be first time, or may be set function failed, the indices back instance is null");
                    //
                    BigDecimal b1 = getDivide.apply(new ResponseWrapper<T>(current),
                                                    element);
                    BigDecimal b2 = getDivide.apply((ResponseWrapper<T>) indicesBack,
                                                    element);
                    BigDecimal divide = b1.compareTo(b2) > 0
                                                            ? b1.subtract(b2)
                                                            : BigDecimal.ZERO;
                    //
                    b1 = getBeDivided.apply(new ResponseWrapper<T>(current),
                                            element);
                    b2 = getBeDivided.apply((ResponseWrapper<T>) indicesBack,
                                            element);
                    BigDecimal beDivided = b1.compareTo(b2) > 0
                                                               ? b1.subtract(b2)
                                                               : BigDecimal.ZERO;
                    return divide.divide(beDivided,
                                         5,
                                         RoundingMode.HALF_UP);
                }
                return BigDecimal.ZERO;
            } catch (ArithmeticException e) {
                log.warn(">>>DIVIDE FAILED: {}",
                         e);
            } catch (Exception e) {
                log.error(">>>DIVIDE FAILED: {}",
                          e);
            }
            return BigDecimal.ZERO;
        }
        
        /**
         * forIndicesTotalSearchQueryLatency: <br/>
         * 
         * @author hadoop
         * @param current
         * @param indexName
         * @return
         * @since JDK 1.8
         */
        public static Double forIndicesTotalSearchQueryLatency(IndicesStatsResponse current,
                                                               String indexName) {
            return forDivideMetrics(current,
                                    indexName,
                                    (getTime,
                                     index) -> new BigDecimal(getTime.getResponse()
                                                                     .getIndex(index)
                                                                     .getTotal()
                                                                     .getSearch()
                                                                     .getTotal()
                                                                     .getQueryTimeInMillis()),
                                    (getTotal,
                                     index) -> new BigDecimal(getTotal.getResponse()
                                                                      .getIndex(index)
                                                                      .getTotal()
                                                                      .getSearch()
                                                                      .getTotal()
                                                                      .getQueryCount())).doubleValue();
        }
        
        /**
         * forIndicesTotalSearchFetchLatency: <br/>
         * 
         * @author hadoop
         * @param current
         * @param indexName
         * @return
         * @since JDK 1.8
         */
        public static Double forIndicesTotalSearchFetchLatency(IndicesStatsResponse current,
                                                               String indexName) {
            return forDivideMetrics(current,
                                    indexName,
                                    (getTime,
                                     index) -> new BigDecimal(getTime.getResponse()
                                                                     .getIndex(index)
                                                                     .getTotal()
                                                                     .getSearch()
                                                                     .getTotal()
                                                                     .getFetchTimeInMillis()),
                                    (getTotal,
                                     index) -> new BigDecimal(getTotal.getResponse()
                                                                      .getIndex(index)
                                                                      .getTotal()
                                                                      .getSearch()
                                                                      .getTotal()
                                                                      .getFetchCount())).doubleValue();
        }
        
        /**
         * forIndicesTotalSearchScrollLatency: <br/>
         * 
         * @author hadoop
         * @param current
         * @param indexName
         * @return
         * @since JDK 1.8
         */
        public static Double forIndicesTotalSearchScrollLatency(IndicesStatsResponse current,
                                                                String indexName) {
            return forDivideMetrics(current,
                                    indexName,
                                    (getTime,
                                     index) -> new BigDecimal(getTime.getResponse()
                                                                     .getIndex(index)
                                                                     .getTotal()
                                                                     .getSearch()
                                                                     .getTotal()
                                                                     .getScrollTimeInMillis()),
                                    (getTotal,
                                     index) -> new BigDecimal(getTotal.getResponse()
                                                                      .getIndex(index)
                                                                      .getTotal()
                                                                      .getSearch()
                                                                      .getTotal()
                                                                      .getScrollCount())).doubleValue();
        }
        
        /**
         * forIndicesPrimariesIndexingIndexSpeed: <br/>
         * 
         * @author hadoop
         * @param current
         * @param indexName
         * @return
         * @since JDK 1.8
         */
        public static Double forIndicesPrimariesIndexingIndexSpeed(IndicesStatsResponse current,
                                                                   String indexName) {
            return forDivideMetrics(current,
                                    indexName,
                                    (getTotal,
                                     index) -> new BigDecimal(getTotal.getResponse()
                                                                      .getIndex(index)
                                                                      .getPrimaries()
                                                                      .getIndexing()
                                                                      .getTotal()
                                                                      .getIndexCount()),
                                    (getTime,
                                     index) -> new BigDecimal(getTime.getResponse()
                                                                     .getIndex(index)
                                                                     .getPrimaries()
                                                                     .getIndexing()
                                                                     .getTotal()
                                                                     .getIndexTimeInMillis())).multiply(new BigDecimal(1000L))
                                                                                              .doubleValue();
        }
        
        /**
         * forIndicesTotalRefreshTotalLatency: <br/>
         * 
         * @author hadoop
         * @param current
         * @param indexName
         * @return
         * @since JDK 1.8
         */
        public static Double forIndicesTotalRefreshTotalLatency(IndicesStatsResponse current,
                                                                String indexName) {
            return forDivideMetrics(current,
                                    indexName,
                                    (getTime,
                                     index) -> new BigDecimal(getTime.getResponse()
                                                                     .getIndex(index)
                                                                     .getTotal()
                                                                     .getRefresh()
                                                                     .getTotalTimeInMillis()),
                                    (getTotal,
                                     index) -> new BigDecimal(getTotal.getResponse()
                                                                      .getIndex(index)
                                                                      .getTotal()
                                                                      .getRefresh()
                                                                      .getTotal())).doubleValue();
        }
        
        /**
         * forIndicesTotalFlushTotalLatency: <br/>
         * 
         * @author hadoop
         * @param current
         * @param indexName
         * @return
         * @since JDK 1.8
         */
        public static Double forIndicesTotalFlushTotalLatency(IndicesStatsResponse current,
                                                              String indexName) {
            return forDivideMetrics(current,
                                    indexName,
                                    (getTime,
                                     index) -> new BigDecimal(getTime.getResponse()
                                                                     .getIndex(index)
                                                                     .getTotal()
                                                                     .getFlush()
                                                                     .getTotalTimeInMillis()),
                                    (getTotal,
                                     index) -> new BigDecimal(getTotal.getResponse()
                                                                      .getIndex(index)
                                                                      .getTotal()
                                                                      .getFlush()
                                                                      .getTotal())).doubleValue();
        }
        
        /**
         * forIndicesTotalMergesTotalLatency: <br/>
         * 
         * @author hadoop
         * @param current
         * @param indexName
         * @return
         * @since JDK 1.8
         */
        public static Double forIndicesTotalMergesTotalLatency(IndicesStatsResponse current,
                                                               String indexName) {
            return forDivideMetrics(current,
                                    indexName,
                                    (getTime,
                                     index) -> new BigDecimal(getTime.getResponse()
                                                                     .getIndex(index)
                                                                     .getTotal()
                                                                     .getMerge()
                                                                     .getTotalTimeInMillis()),
                                    (getTotal,
                                     index) -> new BigDecimal(getTotal.getResponse()
                                                                      .getIndex(index)
                                                                      .getTotal()
                                                                      .getMerge()
                                                                      .getTotal())).doubleValue();
        }
        
        /**
         * forNodesJvmGcYoungCollectionLatency: <br/>
         * 
         * @author hadoop
         * @param current
         * @param nodeId
         * @return
         * @since JDK 1.8
         */
        public static Double forNodesJvmGcYoungCollectionLatency(NodesStatsResponse current,
                                                                 String nodeId) {
            return forDivideMetrics(current,
                                    nodeId,
                                    (getTime,
                                     nid) -> {
                                        NodeStats node = getTime.getResponse()
                                                                .getNodesMap()
                                                                .get(nid);
                                        return node == null
                                                           ? BigDecimal.ZERO
                                                           : new BigDecimal(Stream.of(node.getJvm()
                                                                                          .getGc()
                                                                                          .getCollectors())
                                                                                  .filter(collector -> ElasticsearchSourceConstants.JVM_GC_COLLECTOR_YOUNG.equalsIgnoreCase(collector.getName()))
                                                                                  .findFirst()
                                                                                  .map(unique -> unique.getCollectionTime()
                                                                                                       .getMillis())
                                                                                  .orElse(0L));
                                    },
                                    (getTotal,
                                     nid) -> {
                                        NodeStats node = getTotal.getResponse()
                                                                 .getNodesMap()
                                                                 .get(nid);
                                        return node == null
                                                           ? BigDecimal.ZERO
                                                           : new BigDecimal(Stream.of(node.getJvm()
                                                                                          .getGc()
                                                                                          .getCollectors())
                                                                                  .filter(collector -> ElasticsearchSourceConstants.JVM_GC_COLLECTOR_YOUNG.equalsIgnoreCase(collector.getName()))
                                                                                  .findFirst()
                                                                                  .map(unique -> unique.getCollectionCount())
                                                                                  .orElse(0L));
                                        
                                    }).doubleValue();
        }
        
        /**
         * forNodesJvmGcOldCollectionLatency: <br/>
         * 
         * @author hadoop
         * @param current
         * @param nodeId
         * @return
         * @since JDK 1.8
         */
        public static Double forNodesJvmGcOldCollectionLatency(NodesStatsResponse current,
                                                               String nodeId) {
            return forDivideMetrics(current,
                                    nodeId,
                                    (getTime,
                                     nid) -> {
                                        NodeStats node = getTime.getResponse()
                                                                .getNodesMap()
                                                                .get(nid);
                                        return node == null
                                                           ? BigDecimal.ZERO
                                                           : new BigDecimal(Stream.of(node.getJvm()
                                                                                          .getGc()
                                                                                          .getCollectors())
                                                                                  .filter(collector -> ElasticsearchSourceConstants.JVM_GC_COLLECTOR_OLD.equalsIgnoreCase(collector.getName()))
                                                                                  .findFirst()
                                                                                  .map(unique -> unique.getCollectionTime()
                                                                                                       .getMillis())
                                                                                  .orElse(0L));
                                    },
                                    (getTotal,
                                     nid) -> {
                                        NodeStats node = getTotal.getResponse()
                                                                 .getNodesMap()
                                                                 .get(nid);
                                        return node == null
                                                           ? BigDecimal.ZERO
                                                           : new BigDecimal(Stream.of(node.getJvm()
                                                                                          .getGc()
                                                                                          .getCollectors())
                                                                                  .filter(collector -> ElasticsearchSourceConstants.JVM_GC_COLLECTOR_OLD.equalsIgnoreCase(collector.getName()))
                                                                                  .findFirst()
                                                                                  .map(unique -> unique.getCollectionCount())
                                                                                  .orElse(0L));
                                        
                                    }).doubleValue();
        }
        
        /**
         * forNodesTransportThroughput: <br/>
         * 
         * @author hadoop
         * @param current
         * @param nodeId
         * @param digital
         * @return
         * @since JDK 1.8
         */
        public static Double forNodesTransportThroughput(NodesStatsResponse current,
                                                         String nodeId,
                                                         Function<NodeStats, Long> digital) {
            return forDivideMetrics(current,
                                    nodeId,
                                    (getSize,
                                     nid) -> {
                                        NodeStats node = getSize.getResponse()
                                                                .getNodesMap()
                                                                .get(nid);
                                        return node == null
                                                           ? BigDecimal.ZERO
                                                           : new BigDecimal(digital.apply(node));
                                    },
                                    (getTime,
                                     nid) -> {
                                        NodeStats node = getTime.getResponse()
                                                                .getNodesMap()
                                                                .get(nid);
                                        return node == null
                                                           ? BigDecimal.ZERO
                                                           : new BigDecimal(node.getTimestamp());
                                    }).multiply(new BigDecimal(1000L))
                                      .doubleValue();
            
        }
        
    }
    
    /**
     * ClassName: Metrics <br/>
     * Function: <br/>
     * date: <br/>
     *
     * @author hadoop
     * @version ElasticsearchSourceUniformBuilder
     * @since JDK 1.8
     */
    private enum Metrics {
        // -----------------------------------------------------------------------------------------------------
        // nodes.indices.indexing
        NODES_INDICES_INDEXING_INDEX_TOTAL("nodes.indices.indexing.index_total", (NodesStatsResponse response,
                                                                                  String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getIndexing()
                                                  .getTotal()
                                                  .getIndexCount())
                                     .doubleValue();
        }),
        NODES_INDICES_INDEXING_INDEX_TIME_IN_MILLIS("nodes.indices.indexing.index_time_in_millis", (NodesStatsResponse response,
                                                                                                    String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getIndexing()
                                                  .getTotal()
                                                  .getIndexTimeInMillis())
                                     .doubleValue();
        }),
        NODES_INDICES_INDEXING_INDEX_CURRENT("nodes.indices.indexing.index_current", (NodesStatsResponse response,
                                                                                      String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getIndexing()
                                                  .getTotal()
                                                  .getIndexCurrent())
                                     .doubleValue();
        }),
        NODES_INDICES_INDEXING_INDEX_FAILED("nodes.indices.indexing.index_failed", (NodesStatsResponse response,
                                                                                    String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getIndexing()
                                                  .getTotal()
                                                  .getIndexFailedCount())
                                     .doubleValue();
        }),
        NODES_INDICES_INDEXING_DELETE_TOTAL("nodes.indices.indexing.delete_total", (NodesStatsResponse response,
                                                                                    String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getIndexing()
                                                  .getTotal()
                                                  .getDeleteCount())
                                     .doubleValue();
        }),
        NODES_INDICES_INDEXING_DELETE_TIME_IN_MILLIS("nodes.indices.indexing.delete_time_in_millis", (NodesStatsResponse response,
                                                                                                      String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getIndexing()
                                                  .getTotal()
                                                  .getDeleteTimeInMillis())
                                     .doubleValue();
        }),
        NODES_INDICES_INDEXING_DELETE_CURRENT("nodes.indices.indexing.delete_current", (NodesStatsResponse response,
                                                                                        String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getIndexing()
                                                  .getTotal()
                                                  .getDeleteCurrent())
                                     .doubleValue();
        }),
        NODES_INDICES_INDEXING_NOOP_UPDATE_TOTAL("nodes.indices.indexing.noop_update_total", (NodesStatsResponse response,
                                                                                              String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getIndexing()
                                                  .getTotal()
                                                  .getNoopUpdateCount())
                                     .doubleValue();
        }),
        NODES_INDICES_INDEXING_THROTTLE_TIME_IN_MILLIS("nodes.indices.indexing.throttle_time_in_millis", (NodesStatsResponse response,
                                                                                                          String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getIndexing()
                                                  .getTotal()
                                                  .getThrottleTimeInMillis())
                                     .doubleValue();
        }),
        // -----------------------------------------------------------------------------------------------------
        // nodes.indices.search
        NODES_INDICES_SEARCH_OPEN_CONTEXTS("nodes.indices.search.open_contexts", (NodesStatsResponse response,
                                                                                  String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getOpenContexts())
                                     .doubleValue();
        }),
        NODES_INDICES_SEARCH_QUERY_TOTAL("nodes.indices.search.query_total", (NodesStatsResponse response,
                                                                              String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getTotal()
                                                  .getQueryCount())
                                     .doubleValue();
        }),
        NODES_INDICES_SEARCH_QUERY_TIME_IN_MILLIS("nodes.indices.search.query_time_in_millis", (NodesStatsResponse response,
                                                                                                String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getTotal()
                                                  .getQueryTimeInMillis())
                                     .doubleValue();
        }),
        NODES_INDICES_SEARCH_QUERY_CURRENT("nodes.indices.search.query_current", (NodesStatsResponse response,
                                                                                  String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getTotal()
                                                  .getQueryCurrent())
                                     .doubleValue();
        }),
        NODES_INDICES_SEARCH_FETCH_TOTAL("nodes.indices.search.fetch_total", (NodesStatsResponse response,
                                                                              String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getTotal()
                                                  .getFetchCount())
                                     .doubleValue();
        }),
        NODES_INDICES_SEARCH_FETCH_TIME_IN_MILLIS("nodes.indices.search.fetch_time_in_millis", (NodesStatsResponse response,
                                                                                                String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getTotal()
                                                  .getFetchTimeInMillis())
                                     .doubleValue();
        }),
        NODES_INDICES_SEARCH_FETCH_CURRENT("nodes.indices.search.fetch_current", (NodesStatsResponse response,
                                                                                  String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getTotal()
                                                  .getFetchCurrent())
                                     .doubleValue();
        }),
        NODES_INDICES_SEARCH_SCROLL_TOTAL("nodes.indices.search.scroll_total", (NodesStatsResponse response,
                                                                                String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getTotal()
                                                  .getScrollCount())
                                     .doubleValue();
        }),
        NODES_INDICES_SEARCH_SCROLL_TIME_IN_MILLIS("nodes.indices.search.scroll_time_in_millis", (NodesStatsResponse response,
                                                                                                  String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getTotal()
                                                  .getScrollTimeInMillis())
                                     .doubleValue();
        }),
        NODES_INDICES_SEARCH_SCROLL_CURRENT("nodes.indices.search.scroll_current", (NodesStatsResponse response,
                                                                                    String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getIndices()
                                                  .getSearch()
                                                  .getTotal()
                                                  .getScrollCurrent())
                                     .doubleValue();
        }),
        NODES_INDICES_INDEXING_INDEX_SPEED("nodes.indices.indexing.index_speed", (BiFunction<NodesStatsResponse, String, Double>) BacktraceContext::forNodesIndexingSpeed),
        NODES_INDICES_SEARCH_SEARCH_LATENCY("nodes.indices.search.search_latency", (BiFunction<NodesStatsResponse, String, Double>) BacktraceContext::forNodesSearchLatency),
        // -----------------------------------------------------------------------------------------------------
        // nodes.os.mem
        NODES_OS_LOAD_AVERAGE("nodes.os.load_average", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesOsTimestamp, (NodesStatsResponse response,
                                                                                                                                          String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : node.getOs()
                                     .getLoadAverage();
        }),
        NODES_OS_MEM_TOTAL_IN_BYTES("nodes.os.mem.total_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesOsTimestamp, (NodesStatsResponse response,
                                                                                                                                                      String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getOs()
                                                  .getMem()
                                                  .getTotal()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_OS_MEM_FREE_IN_BYTES("nodes.os.mem.free_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesOsTimestamp, (NodesStatsResponse response,
                                                                                                                                                    String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getOs()
                                                  .getMem()
                                                  .getFree()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_OS_MEM_USED_IN_BYTES("nodes.os.mem.used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesOsTimestamp, (NodesStatsResponse response,
                                                                                                                                                    String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getOs()
                                                  .getMem()
                                                  .getUsed()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_OS_MEM_FREE_PERCENT("nodes.os.mem.free_percent", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesOsTimestamp, (NodesStatsResponse response,
                                                                                                                                                  String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Short.valueOf(node.getOs()
                                                   .getMem()
                                                   .getFreePercent())
                                      .doubleValue();
        }),
        NODES_OS_MEM_USED_PERCENT("nodes.os.mem.used_percent", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesOsTimestamp, (NodesStatsResponse response,
                                                                                                                                                  String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Short.valueOf(node.getOs()
                                                   .getMem()
                                                   .getUsedPercent())
                                      .doubleValue();
        }),
        // -----------------------------------------------------------------------------------------------------
        // nodes.process
        NODES_PROCESS_OPEN_FILE_DESCRIPTORS("nodes.process.open_file_descriptors", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesProcessTimestamp, (NodesStatsResponse response,
                                                                                                                                                                           String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getProcess()
                                                  .getOpenFileDescriptors())
                                     .doubleValue();
        }),
        NODES_PROCESS_MAX_FILE_DESCRIPTORS("nodes.process.max_file_descriptors", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesProcessTimestamp, (NodesStatsResponse response,
                                                                                                                                                                         String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getProcess()
                                                  .getMaxFileDescriptors())
                                     .doubleValue();
        }),
        NODES_PROCESS_CPU_PERCENT("nodes.process.cpu.percent", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesProcessTimestamp, (NodesStatsResponse response,
                                                                                                                                                       String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Short.valueOf(node.getProcess()
                                                   .getCpu()
                                                   .getPercent())
                                      .doubleValue();
        }),
        NODES_PROCESS_CPU_TOTAL_IN_MILLIS("nodes.process.cpu.total_in_millis", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesProcessTimestamp, (NodesStatsResponse response,
                                                                                                                                                                       String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getProcess()
                                                  .getCpu()
                                                  .getTotal()
                                                  .getMillis())
                                     .doubleValue();
        }),
        // -----------------------------------------------------------------------------------------------------
        // nodes.fs
        NODES_FS_TOTAL_TOTAL_IN_BYTES("nodes.fs.total.total_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesFsTimestamp, (NodesStatsResponse response,
                                                                                                                                                          String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getFs()
                                                  .getTotal()
                                                  .getTotal()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_FS_TOTAL_FREE_IN_BYTES("nodes.fs.total.free_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesFsTimestamp, (NodesStatsResponse response,
                                                                                                                                                        String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getFs()
                                                  .getTotal()
                                                  .getFree()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_FS_TOTAL_AVAILABLE_IN_BYTES("nodes.fs.total.available_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesFsTimestamp, (NodesStatsResponse response,
                                                                                                                                                                  String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getFs()
                                                  .getTotal()
                                                  .getAvailable()
                                                  .getBytes())
                                     .doubleValue();
        }),
        // -----------------------------------------------------------------------------------------------------
        // nodes.jvm
        NODES_JVM_GC_COLLECTORS_YOUNG_COLLECTION_COUNT("nodes.jvm.gc.collectors.young.collection_count", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                             String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(Stream.of(node.getJvm()
                                                            .getGc()
                                                            .getCollectors())
                                                    .filter(collector -> ElasticsearchSourceConstants.JVM_GC_COLLECTOR_YOUNG.equalsIgnoreCase(collector.getName()))
                                                    .findFirst()
                                                    .map(unique -> unique.getCollectionCount())
                                                    .orElse(0L))
                                     .doubleValue();
        }),
        NODES_JVM_GC_COLLECTORS_YOUNG_COLLECTION_TIME_IN_MILLIS("nodes.jvm.gc.collectors.young.collection_time_in_millis", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                                               String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(Stream.of(node.getJvm()
                                                            .getGc()
                                                            .getCollectors())
                                                    .filter(collector -> ElasticsearchSourceConstants.JVM_GC_COLLECTOR_YOUNG.equalsIgnoreCase(collector.getName()))
                                                    .findFirst()
                                                    .map(unique -> unique.getCollectionTime()
                                                                         .getMillis())
                                                    .orElse(0L))
                                     .doubleValue();
        }),
        NODES_JVM_GC_COLLECTORS_YOUNG_COLLECTION_LATENCY("nodes.jvm.gc.collectors.young.collection_latency", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (BiFunction<NodesStatsResponse, String, Double>) BacktraceContext::forNodesJvmGcYoungCollectionLatency),
        NODES_JVM_GC_COLLECTORS_OLD_COLLECTION_COUNT("nodes.jvm.gc.collectors.old.collection_count", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                         String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(Stream.of(node.getJvm()
                                                            .getGc()
                                                            .getCollectors())
                                                    .filter(collector -> ElasticsearchSourceConstants.JVM_GC_COLLECTOR_OLD.equalsIgnoreCase(collector.getName()))
                                                    .findFirst()
                                                    .map(unique -> unique.getCollectionCount())
                                                    .orElse(0L))
                                     .doubleValue();
        }),
        NODES_JVM_GC_COLLECTORS_OLD_COLLECTION_TIME_IN_MILLIS("nodes.jvm.gc.collectors.old.collection_time_in_millis", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                                           String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(Stream.of(node.getJvm()
                                                            .getGc()
                                                            .getCollectors())
                                                    .filter(collector -> ElasticsearchSourceConstants.JVM_GC_COLLECTOR_OLD.equalsIgnoreCase(collector.getName()))
                                                    .findFirst()
                                                    .map(unique -> unique.getCollectionTime()
                                                                         .getMillis())
                                                    .orElse(0L))
                                     .doubleValue();
        }),
        NODES_JVM_GC_COLLECTORS_OLD_COLLECTION_LATENCY("nodes.jvm.gc.collectors.old.collection_latency", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (BiFunction<NodesStatsResponse, String, Double>) BacktraceContext::forNodesJvmGcOldCollectionLatency),
        NODES_JVM_THREADS_COUNT("nodes.jvm.threads.count", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                               String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getJvm()
                                                  .getThreads()
                                                  .getCount())
                                     .doubleValue();
        }),
        NODES_JVM_THREADS_PEAK_COUNT("nodes.jvm.threads.peak_count", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                         String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getJvm()
                                                  .getThreads()
                                                  .getPeakCount())
                                     .doubleValue();
        }),
        NODES_JVM_MEM_HEAP_USED_IN_BYTES("nodes.jvm.mem.heap_used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                 String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getJvm()
                                                  .getMem()
                                                  .getHeapUsed()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_JVM_MEM_HEAP_USED_PERCENT("nodes.jvm.mem.heap_used_percent", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                               String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Short.valueOf(node.getJvm()
                                                   .getMem()
                                                   .getHeapUsedPercent())
                                      .doubleValue();
        }),
        NODES_JVM_MEM_HEAP_COMMITTED_IN_BYTES("nodes.jvm.mem.heap_committed_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                           String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getJvm()
                                                  .getMem()
                                                  .getHeapCommitted()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_JVM_MEM_HEAP_MAX_IN_BYTES("nodes.jvm.mem.heap_max_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                               String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getJvm()
                                                  .getMem()
                                                  .getHeapMax()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_JVM_MEM_NON_HEAP_USED_IN_BYTES("nodes.jvm.mem.non_heap_used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                         String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getJvm()
                                                  .getMem()
                                                  .getNonHeapUsed()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_JVM_MEM_NON_HEAP_COMMITTED_IN_BYTES("nodes.jvm.mem.non_heap_committed_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                   String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : Long.valueOf(node.getJvm()
                                                  .getMem()
                                                  .getNonHeapCommitted()
                                                  .getBytes())
                                     .doubleValue();
        }),
        NODES_JVM_MEM_POOLS_YOUNG_USED_IN_BYTES("nodes.jvm.mem.pools.young.used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                               String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsUsedInBytes(node,
                                                                           ElasticsearchSourceConstants.JVM_MEM_POOLS_YOUNG);
        }),
        NODES_JVM_MEM_POOLS_YOUNG_MAX_IN_BYTES("nodes.jvm.mem.pools.young.max_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                             String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsMaxInBytes(node,
                                                                          ElasticsearchSourceConstants.JVM_MEM_POOLS_YOUNG);
        }),
        NODES_JVM_MEM_POOLS_YOUNG_PEAK_USED_IN_BYTES("nodes.jvm.mem.pools.young.peak_used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                         String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsPeakUsedInBytes(node,
                                                                               ElasticsearchSourceConstants.JVM_MEM_POOLS_YOUNG);
        }),
        NODES_JVM_MEM_POOLS_YOUNG_PEAK_MAX_IN_BYTES("nodes.jvm.mem.pools.young.peak_max_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                       String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsPeakMaxInBytes(node,
                                                                              ElasticsearchSourceConstants.JVM_MEM_POOLS_YOUNG);
        }),
        NODES_JVM_MEM_POOLS_OLD_USED_IN_BYTES("nodes.jvm.mem.pools.old.used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                           String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsUsedInBytes(node,
                                                                           ElasticsearchSourceConstants.JVM_MEM_POOLS_OLD);
        }),
        NODES_JVM_MEM_POOLS_OLD_MAX_IN_BYTES("nodes.jvm.mem.pools.old.max_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                         String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsMaxInBytes(node,
                                                                          ElasticsearchSourceConstants.JVM_MEM_POOLS_OLD);
        }),
        NODES_JVM_MEM_POOLS_OLD_PEAK_USED_IN_BYTES("nodes.jvm.mem.pools.old.peak_used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                     String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsPeakUsedInBytes(node,
                                                                               ElasticsearchSourceConstants.JVM_MEM_POOLS_OLD);
        }),
        NODES_JVM_MEM_POOLS_OLD_PEAK_MAX_IN_BYTES("nodes.jvm.mem.pools.old.peak_max_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                   String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsPeakMaxInBytes(node,
                                                                              ElasticsearchSourceConstants.JVM_MEM_POOLS_OLD);
        }),
        NODES_JVM_MEM_POOLS_SURVIVOR_USED_IN_BYTES("nodes.jvm.mem.pools.survivor.used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                     String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsUsedInBytes(node,
                                                                           ElasticsearchSourceConstants.JVM_MEM_POOLS_SURVIVOR);
        }),
        NODES_JVM_MEM_POOLS_SURVIVOR_MAX_IN_BYTES("nodes.jvm.mem.pools.survivor.max_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                   String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsMaxInBytes(node,
                                                                          ElasticsearchSourceConstants.JVM_MEM_POOLS_SURVIVOR);
        }),
        NODES_JVM_MEM_POOLS_SURVIVOR_PEAK_USED_IN_BYTES("nodes.jvm.mem.pools.survivor.peak_used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                               String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsPeakUsedInBytes(node,
                                                                               ElasticsearchSourceConstants.JVM_MEM_POOLS_SURVIVOR);
        }),
        NODES_JVM_MEM_POOLS_SURVIVOR_PEAK_MAX_IN_BYTES("nodes.jvm.mem.pools.survivor.peak_max_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                             String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmMemPoolsPeakMaxInBytes(node,
                                                                              ElasticsearchSourceConstants.JVM_MEM_POOLS_SURVIVOR);
        }),
        NODES_JVM_BUFFER_POOLS_DIRECT_COUNT("nodes.jvm.buffer_pools.direct.count", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                       String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmBufferPoolsMetrics(node,
                                                                          ElasticsearchSourceConstants.JVM_BUFFER_POOLS_DIRECT,
                                                                          pool -> pool.getCount());
        }),
        NODES_JVM_BUFFER_POOLS_DIRECT_USED_IN_BYTES("nodes.jvm.buffer_pools.direct.used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                       String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmBufferPoolsMetrics(node,
                                                                          ElasticsearchSourceConstants.JVM_BUFFER_POOLS_DIRECT,
                                                                          pool -> pool.getUsed()
                                                                                      .getBytes());
        }),
        NODES_JVM_BUFFER_POOLS_DIRECT_TOTAL_CAPACITY_IN_BYTES("nodes.jvm.buffer_pools.direct.total_capacity_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                                           String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmBufferPoolsMetrics(node,
                                                                          ElasticsearchSourceConstants.JVM_BUFFER_POOLS_DIRECT,
                                                                          pool -> pool.getTotalCapacity()
                                                                                      .getBytes());
        }),
        NODES_JVM_BUFFER_POOLS_MAPPED_COUNT("nodes.jvm.buffer_pools.mapped.count", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                       String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmBufferPoolsMetrics(node,
                                                                          ElasticsearchSourceConstants.JVM_BUFFER_POOLS_MAPPED,
                                                                          pool -> pool.getCount());
        }),
        NODES_JVM_BUFFER_POOLS_MAPPED_USED_IN_BYTES("nodes.jvm.buffer_pools.mapped.used_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                       String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmBufferPoolsMetrics(node,
                                                                          ElasticsearchSourceConstants.JVM_BUFFER_POOLS_MAPPED,
                                                                          pool -> pool.getUsed()
                                                                                      .getBytes());
        }),
        NODES_JVM_BUFFER_POOLS_MAPPED_TOTAL_CAPACITY_IN_BYTES("nodes.jvm.buffer_pools.mapped.total_capacity_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesJvmTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                                           String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : GeneralFunction.getJvmBufferPoolsMetrics(node,
                                                                          ElasticsearchSourceConstants.JVM_BUFFER_POOLS_MAPPED,
                                                                          pool -> pool.getTotalCapacity()
                                                                                      .getBytes());
        }),
        // -----------------------------------------------------------------------------------------------------
        // nodes.transport
        NODES_TRANSPORT_SERVER_OPEN("nodes.transport.server_open", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp, (NodesStatsResponse response,
                                                                                                                                                    String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : node.getTransport()
                                     .getServerOpen();
        }),
        NODES_TRANSPORT_RX_COUNT("nodes.transport.rx_count", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp, (NodesStatsResponse response,
                                                                                                                                              String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : node.getTransport()
                                     .getRxCount();
        }),
        NODES_TRANSPORT_RX_COUNT_THROUGHPUT("nodes.transport.rx_count.throughput", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp, (NodesStatsResponse response,
                                                                                                                                                                    String nodeId) -> {
            return BacktraceContext.forNodesTransportThroughput(response,
                                                                nodeId,
                                                                nstats -> nstats.getTransport()
                                                                                .getRxCount());
        }),
        NODES_TRANSPORT_RX_SIZE_IN_BYTES("nodes.transport.rx_size_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp, (NodesStatsResponse response,
                                                                                                                                                              String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : node.getTransport()
                                     .getRxSize()
                                     .getBytes();
        }),
        NODES_TRANSPORT_RX_SIZE_IN_BYTES_THROUGHPUT("nodes.transport.rx_size_in_bytes.throughput", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                    String nodeId) -> {
            return BacktraceContext.forNodesTransportThroughput(response,
                                                                nodeId,
                                                                nstats -> nstats.getTransport()
                                                                                .getRxSize()
                                                                                .getBytes());
        }),
        NODES_TRANSPORT_TX_COUNT("nodes.transport.tx_count", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp, (NodesStatsResponse response,
                                                                                                                                              String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : node.getTransport()
                                     .getTxCount();
        }),
        NODES_TRANSPORT_TX_COUNT_THROUGHPUT("nodes.transport.tx_count.throughput", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp, (NodesStatsResponse response,
                                                                                                                                                                    String nodeId) -> {
            return BacktraceContext.forNodesTransportThroughput(response,
                                                                nodeId,
                                                                nstats -> nstats.getTransport()
                                                                                .getTxCount());
        }),
        NODES_TRANSPORT_TX_SIZE_IN_BYTES("nodes.transport.tx_size_in_bytes", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp, (NodesStatsResponse response,
                                                                                                                                                              String nodeId) -> {
            NodeStats node = response.getNodesMap()
                                     .get(nodeId);
            return node == null
                               ? 0.0
                               : node.getTransport()
                                     .getTxSize()
                                     .getBytes();
        }),
        NODES_TRANSPORT_TX_SIZE_IN_BYTES_THROUGHPUT("nodes.transport.tx_size_in_bytes.throughput", (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp, (NodesStatsResponse response,
                                                                                                                                                                                    String nodeId) -> {
            return BacktraceContext.forNodesTransportThroughput(response,
                                                                nodeId,
                                                                nstats -> nstats.getTransport()
                                                                                .getTxSize()
                                                                                .getBytes());
        }),
        // -----------------------------------------------------------------------------------------------------
        // indices.total.search
        INDICES_TOTAL_SEARCH_QUERY_TOTAL("indices.total.search.query_total", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (IndicesStatsResponse response,
                                                                                                                                                                                                                                                                                                                                     String indexName) -> {
            IndexStats index = response.getIndex(indexName);
            return index == null
                                ? 0.0
                                : Long.valueOf(index.getTotal()
                                                    .getSearch()
                                                    .getTotal()
                                                    .getQueryCount())
                                      .doubleValue();
        }),
        INDICES_TOTAL_SEARCH_FETCH_TOTAL("indices.total.search.fetch_total", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (IndicesStatsResponse response,
                                                                                                                                                                                                                                                                                                                                     String indexName) -> {
            IndexStats index = response.getIndex(indexName);
            return index == null
                                ? 0.0
                                : Long.valueOf(index.getTotal()
                                                    .getSearch()
                                                    .getTotal()
                                                    .getFetchCount())
                                      .doubleValue();
        }),
        INDICES_TOTAL_SEARCH_SCROLL_TOTAL("indices.total.search.scroll_total", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (IndicesStatsResponse response,
                                                                                                                                                                                                                                                                                                                                       String indexName) -> {
            IndexStats index = response.getIndex(indexName);
            return index == null
                                ? 0.0
                                : Long.valueOf(index.getTotal()
                                                    .getSearch()
                                                    .getTotal()
                                                    .getScrollCount())
                                      .doubleValue();
        }),
        INDICES_TOTAL_SEARCH_QUERY_LATENCY("indices.total.search.query_latency", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (BiFunction<IndicesStatsResponse, String, Double>) BacktraceContext::forIndicesTotalSearchQueryLatency),
        INDICES_TOTAL_SEARCH_FETCH_LATENCY("indices.total.search.fetch_latency", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (BiFunction<IndicesStatsResponse, String, Double>) BacktraceContext::forIndicesTotalSearchFetchLatency),
        INDICES_TOTAL_SEARCH_SCROLL_LATENCY("indices.total.search.scroll_latency", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (BiFunction<IndicesStatsResponse, String, Double>) BacktraceContext::forIndicesTotalSearchScrollLatency),
        // -----------------------------------------------------------------------------------------------------
        // indices.primaries.indexing
        INDICES_PRIMARIES_INDEXING_INDEX_TOTAL("indices.primaries.indexing.index_total", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (IndicesStatsResponse response,
                                                                                                                                                                                                                                                                                                                                                 String indexName) -> {
            IndexStats index = response.getIndex(indexName);
            return index == null
                                ? 0.0
                                : Long.valueOf(index.getPrimaries()
                                                    .getIndexing()
                                                    .getTotal()
                                                    .getIndexCount())
                                      .doubleValue();
        }),
        INDICES_PRIMARIES_INDEXING_INDEX_SPEED("indices.primaries.indexing.index_speed", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (BiFunction<IndicesStatsResponse, String, Double>) BacktraceContext::forIndicesPrimariesIndexingIndexSpeed),
        // -----------------------------------------------------------------------------------------------------
        // indices.total.refresh
        INDICES_TOTAL_REFRESH_TOTAL("indices.total.refresh.total", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (IndicesStatsResponse response,
                                                                                                                                                                                                                                                                                                                           String indexName) -> {
            IndexStats index = response.getIndex(indexName);
            return index == null
                                ? 0.0
                                : Long.valueOf(index.getTotal()
                                                    .getRefresh()
                                                    .getTotal())
                                      .doubleValue();
        }),
        INDICES_TOTAL_REFRESH_TOTAL_LATENCY("indices.total.refresh.total_latency", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (BiFunction<IndicesStatsResponse, String, Double>) BacktraceContext::forIndicesTotalRefreshTotalLatency),
        // -----------------------------------------------------------------------------------------------------
        // indices.total.flush
        INDICES_TOTAL_FLUSH_TOTAL("indices.total.flush.total", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (IndicesStatsResponse response,
                                                                                                                                                                                                                                                                                                                       String indexName) -> {
            IndexStats index = response.getIndex(indexName);
            return index == null
                                ? 0.0
                                : Long.valueOf(index.getTotal()
                                                    .getFlush()
                                                    .getTotal())
                                      .doubleValue();
        }),
        INDICES_TOTAL_FLUSH_TOTAL_LATENCY("indices.total.flush.total_latency", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (BiFunction<IndicesStatsResponse, String, Double>) BacktraceContext::forIndicesTotalFlushTotalLatency),
        // -----------------------------------------------------------------------------------------------------
        // indices.total.segments
        INDICES_TOTAL_SEGMENTS_TOTAL("indices.total.segments.count", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (IndicesStatsResponse response,
                                                                                                                                                                                                                                                                                                                             String indexName) -> {
            IndexStats index = response.getIndex(indexName);
            return index == null
                                ? 0.0
                                : Long.valueOf(index.getTotal()
                                                    .getSegments()
                                                    .getCount())
                                      .doubleValue();
        }),
        // -----------------------------------------------------------------------------------------------------
        // indices.total.merges
        INDICES_TOTAL_MERGES_TOTAL("indices.total.merges.total", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (IndicesStatsResponse response,
                                                                                                                                                                                                                                                                                                                         String indexName) -> {
            IndexStats index = response.getIndex(indexName);
            return index == null
                                ? 0.0
                                : Long.valueOf(index.getTotal()
                                                    .getMerge()
                                                    .getTotal())
                                      .doubleValue();
        }),
        INDICES_TOTAL_MERGES_TOTAL_LATENCY("indices.total.merges.total_latency", (Function<IndicesStatsResponse, String>) GeneralFunction::indicesServicename, (BiFunction<IndicesStatsResponse, String, String>) GeneralFunction::indicesHostname, (BiFunction<IndicesStatsResponse, String, Long>) GeneralFunction::indicesTimestamp, (BiFunction<IndicesStatsResponse, String, Double>) BacktraceContext::forIndicesTotalMergesTotalLatency);
        
        @Getter
        private String                        metricname;
        
        private Function<?, String>           servicename;
        
        private BiFunction<?, String, String> hostname;
        
        private BiFunction<?, String, Long>   timestamp;
        
        private BiFunction<?, String, Double> metricdata;
        
        /**
         * ClassName: GeneralFunction <br/>
         * Function: <br/>
         * date: <br/>
         *
         * @author hadoop
         * @version ElasticsearchSourceUniformBuilder.Metrics
         * @since JDK 1.8
         */
        private static class GeneralFunction {
            
            /**
             * getJvmBufferPoolsMetrics: <br/>
             * 
             * @author hadoop
             * @param node
             * @param name
             * @return
             * @since JDK 1.8
             */
            public static Double getJvmBufferPoolsMetrics(NodeStats node,
                                                          String name,
                                                          Function<BufferPool, Long> convertor) {
                return Stream.of((BufferPool[]) node.getJvm()
                                                    .getBufferPools()
                                                    .toArray())
                             .filter(bp -> name.equalsIgnoreCase(bp.getName()))
                             .map(pool -> convertor.apply(pool))
                             .findFirst()
                             .orElse(0L)
                             .doubleValue();
                
            }
            
            /**
             * getJvmMemPoolsUsedInBytes: <br/>
             * 
             * @author hadoop
             * @param node
             * @param name
             * @return
             * @since JDK 1.8
             */
            public static Double getJvmMemPoolsUsedInBytes(NodeStats node,
                                                           String name) {
                return Iterators.tryFind(node.getJvm()
                                             .getMem()
                                             .iterator(),
                                         mp -> name.equalsIgnoreCase(mp.getName()))
                                .transform(pool -> pool.getUsed()
                                                       .getBytes())
                                .or(0L)
                                .doubleValue();
            }
            
            /**
             * getJvmMemPoolsMaxInBytes: <br/>
             * 
             * @author hadoop
             * @param node
             * @param name
             * @return
             * @since JDK 1.8
             */
            public static Double getJvmMemPoolsMaxInBytes(NodeStats node,
                                                          String name) {
                return Iterators.tryFind(node.getJvm()
                                             .getMem()
                                             .iterator(),
                                         mp -> name.equalsIgnoreCase(mp.getName()))
                                .transform(pool -> pool.getMax()
                                                       .getBytes())
                                .or(0L)
                                .doubleValue();
            }
            
            /**
             * getJvmMemPoolsPeakUsedInBytes: <br/>
             * 
             * @author hadoop
             * @param node
             * @param name
             * @return
             * @since JDK 1.8
             */
            public static Double getJvmMemPoolsPeakUsedInBytes(NodeStats node,
                                                               String name) {
                return Iterators.tryFind(node.getJvm()
                                             .getMem()
                                             .iterator(),
                                         mp -> name.equalsIgnoreCase(mp.getName()))
                                .transform(pool -> pool.getPeakUsed()
                                                       .getBytes())
                                .or(0L)
                                .doubleValue();
            }
            
            /**
             * getJvmMemPoolsPeakMaxInBytes: <br/>
             * 
             * @author hadoop
             * @param node
             * @param name
             * @return
             * @since JDK 1.8
             */
            public static Double getJvmMemPoolsPeakMaxInBytes(NodeStats node,
                                                              String name) {
                return Iterators.tryFind(node.getJvm()
                                             .getMem()
                                             .iterator(),
                                         mp -> name.equalsIgnoreCase(mp.getName()))
                                .transform(pool -> pool.getPeakMax()
                                                       .getBytes())
                                .or(0L)
                                .doubleValue();
            }
            
            /**
             * nodesServicename: <br/>
             * 
             * @author hadoop
             * @param response
             * @return
             * @since JDK 1.8
             */
            public static String nodesServicename(NodesStatsResponse response) {
                return response.getClusterNameAsString();
            }
            
            /**
             * nodesHostname: <br/>
             * 
             * @author hadoop
             * @param response
             * @param nodeId
             * @return
             * @since JDK 1.8
             */
            public static String nodesHostname(NodesStatsResponse response,
                                               String nodeId) {
                NodeStats node = response.getNodesMap()
                                         .get(nodeId);
                return node == null
                                   ? ""
                                   : node.getNode()
                                         .getName();
                
            }
            
            /**
             * nodesTimestamp: <br/>
             * 
             * @author hadoop
             * @param response
             * @param nodeId
             * @return
             * @since JDK 1.8
             */
            public static long nodesTimestamp(NodesStatsResponse response,
                                              String nodeId) {
                NodeStats node = response.getNodesMap()
                                         .get(nodeId);
                return node == null
                                   ? 0L
                                   : node.getTimestamp();
                
            }
            
            /**
             * nodesOsTimestamp: <br/>
             * 
             * @author hadoop
             * @param response
             * @param nodeId
             * @return
             * @since JDK 1.8
             */
            public static long nodesOsTimestamp(NodesStatsResponse response,
                                                String nodeId) {
                NodeStats node = response.getNodesMap()
                                         .get(nodeId);
                return node == null
                                   ? 0L
                                   : node.getOs()
                                         .getTimestamp();
            }
            
            /**
             * nodesProcessTimestamp: <br/>
             * 
             * @author hadoop
             * @param response
             * @param nodeId
             * @return
             * @since JDK 1.8
             */
            public static long nodesProcessTimestamp(NodesStatsResponse response,
                                                     String nodeId) {
                NodeStats node = response.getNodesMap()
                                         .get(nodeId);
                return node == null
                                   ? 0L
                                   : node.getProcess()
                                         .getTimestamp();
            }
            
            /**
             * nodesFsTimestamp: <br/>
             * 
             * @author hadoop
             * @param response
             * @param nodeId
             * @return
             * @since JDK 1.8
             */
            public static long nodesFsTimestamp(NodesStatsResponse response,
                                                String nodeId) {
                NodeStats node = response.getNodesMap()
                                         .get(nodeId);
                return node == null
                                   ? 0L
                                   : node.getFs()
                                         .getTimestamp();
            }
            
            /**
             * nodesJvmTimestamp: <br/>
             * 
             * @author hadoop
             * @param response
             * @param nodeId
             * @return
             * @since JDK 1.8
             */
            public static long nodesJvmTimestamp(NodesStatsResponse response,
                                                 String nodeId) {
                NodeStats node = response.getNodesMap()
                                         .get(nodeId);
                return node == null
                                   ? 0L
                                   : node.getJvm()
                                         .getTimestamp();
            }
            
            /**
             * indicesServicename: <br/>
             * 
             * @author hadoop
             * @param response
             * @return
             * @since JDK 1.8
             */
            public static String indicesServicename(IndicesStatsResponse response) {
                return "";
            }
            
            /**
             * indicesHostname: <br/>
             * 
             * @author hadoop
             * @param response
             * @param indexName
             * @return
             * @since JDK 1.8
             */
            public static String indicesHostname(IndicesStatsResponse response,
                                                 String indexName) {
                IndexStats index = response.getIndex(indexName);
                return index == null
                                    ? ""
                                    : index.getIndex();
            }
            
            /**
             * indicesTimestamp: <br/>
             * 
             * @author hadoop
             * @param response
             * @param indexName
             * @return
             * @since JDK 1.8
             */
            public static Long indicesTimestamp(IndicesStatsResponse response,
                                                String indexName) {
                return System.currentTimeMillis();
            }
            
        }
        
        /**
         * Creates a new instance of Metrics.
         * 
         * @param metricname
         * @param metricdata
         */
        private Metrics(String metricname,
                        BiFunction<?, String, Double> metricdata) {
            this(metricname,
                 (Function<NodesStatsResponse, String>) GeneralFunction::nodesServicename,
                 (BiFunction<NodesStatsResponse, String, String>) GeneralFunction::nodesHostname,
                 (BiFunction<NodesStatsResponse, String, Long>) GeneralFunction::nodesTimestamp,
                 metricdata);
        }
        
        /**
         * Creates a new instance of Metrics.
         * 
         * @param metricname
         * @param timestamp
         * @param metricdata
         */
        private Metrics(String metricname,
                        BiFunction<?, String, Long> timestamp,
                        BiFunction<?, String, Double> metricdata) {
            this(metricname,
                 (Function<NodesStatsResponse, String>) GeneralFunction::nodesServicename,
                 (BiFunction<NodesStatsResponse, String, String>) GeneralFunction::nodesHostname,
                 timestamp,
                 metricdata);
        }
        
        /**
         * Creates a new instance of Metrics.
         * 
         * @param metricname
         * @param servicename
         * @param hostname
         * @param timestamp
         * @param metricdata
         */
        private Metrics(String metricname,
                        Function<?, String> servicename,
                        BiFunction<?, String, String> hostname,
                        BiFunction<?, String, Long> timestamp,
                        BiFunction<?, String, Double> metricdata) {
            this.metricname = metricname;
            this.servicename = servicename;
            this.hostname = hostname;
            this.timestamp = timestamp;
            this.metricdata = metricdata;
        }
        
        /**
         * getAsUniform: <br/>
         * 
         * @author hadoop
         * @param response
         * @return
         * @since JDK 1.8
         */
        public <U> List<ElasticsearchSourceUniform> getAsUniform(U response) {
            // back is sub-type of NodesStatsResponse
            if (NodesStatsResponse.class.isAssignableFrom(response.getClass())) {
                return getAsNodesUniform((NodesStatsResponse) response);
            }
            // back is sub-type of IndicesStatsResponse
            if (IndicesStatsResponse.class.isAssignableFrom(response.getClass())) {
                return getAsIndicesUniform((IndicesStatsResponse) response);
            }
            return new ArrayList<ElasticsearchSourceUniform>();
        }
        
        /**
         * getAsNodesUniform: <br/>
         * 
         * @author hadoop
         * @param response
         * @return
         * @since JDK 1.8
         */
        @SuppressWarnings("unchecked")
        private List<ElasticsearchSourceUniform> getAsNodesUniform(NodesStatsResponse response) {
            //
            List<ElasticsearchSourceUniform> uniforms = new ArrayList<ElasticsearchSourceUniform>();
            //
            response.getNodesMap()
                    .forEach((nodeId,
                              nodeStats) -> {
                        ElasticsearchSourceUniform uniform = new ElasticsearchSourceUniform();
                        uniform.setMetricname(metricname);
                        uniform.setServicetype(ElasticsearchSourceConstants.DEFAULT_SERVICE_TYPE);
                        try {
                            uniform.setServicename(((Function<NodesStatsResponse, String>) servicename).apply(response));
                            uniform.setHostname(((BiFunction<NodesStatsResponse, String, String>) hostname).apply(response,
                                                                                                                  nodeId));
                            uniform.setTimestamp(((BiFunction<NodesStatsResponse, String, Long>) timestamp).apply(response,
                                                                                                                  nodeId));
                            uniform.setMetricdata(((BiFunction<NodesStatsResponse, String, Double>) metricdata).apply(response,
                                                                                                                      nodeId));
                            uniforms.add(uniform);
                        } catch (Exception e) {
                            log.error("Format nodes-metric to uniform failed because of: {}",
                                      e);
                        }
                    });
            return uniforms;
        }
        
        /**
         * getAsIndicesUniform: <br/>
         * 
         * @author hadoop
         * @param response
         * @return
         * @since JDK 1.8
         */
        @SuppressWarnings("unchecked")
        private List<ElasticsearchSourceUniform> getAsIndicesUniform(IndicesStatsResponse response) {
            //
            List<ElasticsearchSourceUniform> uniforms = new ArrayList<ElasticsearchSourceUniform>();
            //
            response.getIndices()
                    .forEach((indexName,
                              indexStats) -> {
                        ElasticsearchSourceUniform uniform = new ElasticsearchSourceUniform();
                        uniform.setMetricname(metricname);
                        uniform.setServicetype(ElasticsearchSourceConstants.DEFAULT_SERVICE_TYPE);
                        try {
                            
                            uniform.setServicename(((Function<IndicesStatsResponse, String>) servicename).apply(response));
                            uniform.setHostname(((BiFunction<IndicesStatsResponse, String, String>) hostname).apply(response,
                                                                                                                    indexName));
                            uniform.setTimestamp(((BiFunction<IndicesStatsResponse, String, Long>) timestamp).apply(response,
                                                                                                                    indexName));
                            uniform.setMetricdata(((BiFunction<IndicesStatsResponse, String, Double>) metricdata).apply(response,
                                                                                                                        indexName));
                            uniforms.add(uniform);
                        } catch (Exception e) {
                            log.error("Format indices-metric to uniform failed because of: {}",
                                      e);
                        }
                    });
            return uniforms;
        }
        
        /**
         * of: <br/>
         * 
         * @author hadoop
         * @param metricname
         * @return
         * @since JDK 1.8
         */
        public static Optional<Metrics> of(String metricname) {
            return Arrays.asList(Metrics.values())
                         .stream()
                         .filter(m -> m.getMetricname()
                                       .equalsIgnoreCase(metricname))
                         .findAny();
        }
        
    }
    
    /**
     * @see com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceStatsBuilder#config(org.apache.flume.Context)
     */
    @Override
    public ElasticsearchSourceUniformBuilder config(Context context) {
        // config nodesMetrics
        incrementAdd(context.getString(ElasticsearchSourceConstants.NODES_METRICS,
                                       ElasticsearchSourceConstants.DEFAULT_NODES_METRICS),
                     nodesMetrics);
        // config indicesMetrics
        incrementAdd(context.getString(ElasticsearchSourceConstants.INDICES_METRICS,
                                       ElasticsearchSourceConstants.DEFAULT_INDICES_METRICS),
                     indicesMetrics);
        return this;
    }
    
    /**
     * incrementAdd: <br/>
     * 
     * @author hadoop
     * @param metrics
     * @param mlist
     * @since JDK 1.8
     */
    private void incrementAdd(String metrics,
                              List<Metrics> mlist) {
        // check blank
        if (StringUtils.isBlank(metrics)) {
            log.warn("Metrics list is blank!");
            return;
        }
        // increment add
        Arrays.asList(metrics.split(ElasticsearchSourceConstants.DEFAULT_SPLITS))
              .stream()
              .map(metric -> Metrics.of(metric.trim()
                                              .toLowerCase()))
              .forEach(option -> option.ifPresent(m -> {
                  if (!mlist.contains(m)) {
                      mlist.add(m);
                  }
              }));
    }
    
}
