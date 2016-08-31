/**
 * Project Name:oneapm-log-agent
 * File Name:TestElasticsearchSourceUniformBuilder.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.flume.Context;
import org.elasticsearch.action.admin.cluster.node.stats.NodeStats;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.indexing.IndexingStats;
import org.elasticsearch.index.search.stats.SearchStats;
import org.elasticsearch.indices.NodeIndicesStats;
import org.elasticsearch.monitor.fs.FsInfo;
import org.elasticsearch.monitor.jvm.JvmStats;
import org.elasticsearch.monitor.jvm.JvmStats.BufferPool;
import org.elasticsearch.monitor.jvm.JvmStats.GarbageCollector;
import org.elasticsearch.monitor.jvm.JvmStats.GarbageCollectors;
import org.elasticsearch.monitor.jvm.JvmStats.Mem;
import org.elasticsearch.monitor.jvm.JvmStats.MemoryPool;
import org.elasticsearch.monitor.jvm.JvmStats.Threads;
import org.elasticsearch.monitor.os.OsStats;
import org.elasticsearch.monitor.process.ProcessStats;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.google.common.collect.Iterators;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceUniform;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceUniformBuilder;

/**
 * ClassName:TestElasticsearchSourceUniformBuilder <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {NodesStatsResponse.class, ArrayList.class, Context.class, StringUtils.class})
public class TestElasticsearchSourceUniformBuilder {
    
    /**
     * testConfig01: <br/>
     * 
     * @author hadoop
     * @throws Exception
     * @since JDK 1.8
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testConfig01() throws Exception {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.index_total");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // when
        ElasticsearchSourceUniformBuilder result = builder.config(mock);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        List metricList = (List) Whitebox.getInternalState(result,
                                                           "nodesMetrics");
        MatcherAssert.assertThat(metricList.size(),
                                 CoreMatchers.equalTo(1));
    }
    
    /**
     * testConfig02: <br/>
     * 
     * @author hadoop
     * @throws Exception
     * @since JDK 1.8
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testConfig02() throws Exception {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.index_total,nodes.indices.indexing.index_total");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // when
        ElasticsearchSourceUniformBuilder result = builder.config(mock);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        List metricList = (List) Whitebox.getInternalState(result,
                                                           "nodesMetrics");
        MatcherAssert.assertThat(metricList.size(),
                                 CoreMatchers.equalTo(1));
    }
    
    /**
     * testConfig03: <br/>
     * 
     * @author hadoop
     * @throws Exception
     * @since JDK 1.8
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testConfig03() throws Exception {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.index_total,nodes.indices.indexing.index_time_in_millis,nodes.indices.indexing.index_current,nodes.indices.indexing.index_failed,nodes.indices.indexing.delete_total,nodes.indices.indexing.delete_time_in_millis,nodes.indices.indexing.delete_current,nodes.indices.indexing.noop_update_total,nodes.indices.indexing.throttle_time_in_millis,nodes.indices.search.open_contexts,nodes.indices.search.query_total,nodes.indices.search.query_time_in_millis,nodes.indices.search.query_current,nodes.indices.search.fetch_total,nodes.indices.search.fetch_time_in_millis,nodes.indices.search.fetch_current,nodes.indices.search.scroll_total,nodes.indices.search.scroll_time_in_millis,nodes.indices.search.scroll_current,nodes.indices.indexing.index_speed,nodes.indices.search.search_latency,nodes.os.load_average,nodes.os.mem.total_in_bytes,nodes.os.mem.free_in_bytes,nodes.os.mem.used_in_bytes,nodes.os.mem.free_percent,nodes.os.mem.used_percent,nodes.process.open_file_descriptors,nodes.process.max_file_descriptors,nodes.process.cpu.percent,nodes.process.cpu.total_in_millis,nodes.fs.total.total_in_bytes,nodes.fs.total.free_in_bytes,nodes.fs.total.available_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // when
        ElasticsearchSourceUniformBuilder result = builder.config(mock);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        List metricList = (List) Whitebox.getInternalState(result,
                                                           "nodesMetrics");
        MatcherAssert.assertThat(metricList.size(),
                                 CoreMatchers.equalTo(34));
    }
    
    /**
     * testConfig04: <br/>
     * 
     * @author hadoop
     * @throws Exception
     * @since JDK 1.8
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testConfig04() throws Exception {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.index_total,nodes.indices.indexing.index_time_in_millis,nodes.indices.indexing.index_current,nodes.indices.indexing.index_failed,nodes.indices.indexing.delete_total,nodes.indices.indexing.delete_time_in_millis,nodes.indices.indexing.delete_current,nodes.indices.indexing.noop_update_total,nodes.indices.indexing.throttle_time_in_millis,nodes.indices.search.open_contexts,nodes.indices.search.query_total,nodes.indices.search.query_time_in_millis,nodes.indices.search.query_current,nodes.indices.search.fetch_total,nodes.indices.search.fetch_time_in_millis,nodes.indices.search.fetch_current,nodes.indices.search.scroll_total,nodes.indices.search.scroll_time_in_millis,nodes.indices.search.scroll_current,nodes.indices.indexing.index_speed,nodes.indices.search.search_latency,nodes.os.load_average,nodes.os.mem.total_in_bytes,nodes.os.mem.free_in_bytes,nodes.os.mem.used_in_bytes,nodes.os.mem.free_percent,nodes.os.mem.used_percent,nodes.process.open_file_descriptors,nodes.process.max_file_descriptors,nodes.process.cpu.percent,nodes.process.cpu.total_in_millis,nodes.fs.total.total_in_bytes,nodes.fs.total.free_in_bytes,nodes.fs.total.available_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(true);
        // when
        ElasticsearchSourceUniformBuilder result = builder.config(mock);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        List metricList = (List) Whitebox.getInternalState(result,
                                                           "nodesMetrics");
        MatcherAssert.assertThat(metricList.size(),
                                 CoreMatchers.equalTo(0));
    }
    
    private static NodesStatsResponse genrealResp;
    
    /**
     * init: <br/>
     * 
     * @author hadoop
     * @since JDK 1.8
     */
    @BeforeClass
    public static void init() {
        // given
        IndexingStats.Stats stats = PowerMockito.mock(IndexingStats.Stats.class);
        PowerMockito.when(stats.getIndexCount())
                    .thenReturn(100L);
        PowerMockito.when(stats.getIndexTimeInMillis())
                    .thenReturn(100L);
        PowerMockito.when(stats.getIndexCurrent())
                    .thenReturn(100L);
        PowerMockito.when(stats.getIndexFailedCount())
                    .thenReturn(100L);
        PowerMockito.when(stats.getDeleteCount())
                    .thenReturn(100L);
        PowerMockito.when(stats.getDeleteTimeInMillis())
                    .thenReturn(100L);
        PowerMockito.when(stats.getDeleteCurrent())
                    .thenReturn(100L);
        PowerMockito.when(stats.getNoopUpdateCount())
                    .thenReturn(100L);
        PowerMockito.when(stats.getThrottleTimeInMillis())
                    .thenReturn(100L);
        // given
        IndexingStats indexStats = PowerMockito.mock(IndexingStats.class);
        PowerMockito.when(indexStats.getTotal())
                    .thenReturn(stats);
        // given
        SearchStats.Stats ss = PowerMockito.mock(SearchStats.Stats.class);
        PowerMockito.when(ss.getQueryCount())
                    .thenReturn(100L);
        PowerMockito.when(ss.getQueryTimeInMillis())
                    .thenReturn(100L);
        PowerMockito.when(ss.getQueryCurrent())
                    .thenReturn(100L);
        PowerMockito.when(ss.getFetchCount())
                    .thenReturn(100L);
        PowerMockito.when(ss.getFetchTimeInMillis())
                    .thenReturn(100L);
        PowerMockito.when(ss.getFetchCurrent())
                    .thenReturn(100L);
        PowerMockito.when(ss.getScrollCount())
                    .thenReturn(100L);
        PowerMockito.when(ss.getScrollTimeInMillis())
                    .thenReturn(100L);
        PowerMockito.when(ss.getScrollCurrent())
                    .thenReturn(100L);
        // given
        SearchStats searchStats = PowerMockito.mock(SearchStats.class);
        PowerMockito.when(searchStats.getTotal())
                    .thenReturn(ss);
        PowerMockito.when(searchStats.getOpenContexts())
                    .thenReturn(100L);
        // given
        NodeIndicesStats nodeIS = PowerMockito.mock(NodeIndicesStats.class);
        PowerMockito.when(nodeIS.getIndexing())
                    .thenReturn(indexStats);
        PowerMockito.when(nodeIS.getSearch())
                    .thenReturn(searchStats);
        // given
        DiscoveryNode dNode = PowerMockito.mock(DiscoveryNode.class);
        PowerMockito.when(dNode.getName())
                    .thenReturn("node-1");
        // given
        ByteSizeValue size = PowerMockito.mock(ByteSizeValue.class);
        PowerMockito.when(size.getBytes())
                    .thenReturn(100L);
        // given
        OsStats.Mem osMem = PowerMockito.mock(OsStats.Mem.class);
        PowerMockito.when(osMem.getTotal())
                    .thenReturn(size);
        PowerMockito.when(osMem.getFree())
                    .thenReturn(size);
        PowerMockito.when(osMem.getUsed())
                    .thenReturn(size);
        PowerMockito.when(osMem.getFreePercent())
                    .thenReturn((short) 100);
        PowerMockito.when(osMem.getUsedPercent())
                    .thenReturn((short) 100);
        // given
        OsStats osStats = PowerMockito.mock(OsStats.class);
        PowerMockito.when(osStats.getMem())
                    .thenReturn(osMem);
        PowerMockito.when(osStats.getTimestamp())
                    .thenReturn(100L);
        PowerMockito.when(osStats.getLoadAverage())
                    .thenReturn(100.0);
        // given
        TimeValue tv = PowerMockito.mock(TimeValue.class);
        PowerMockito.when(tv.getMillis())
                    .thenReturn(100L);
        // given
        ProcessStats.Cpu cpu = PowerMockito.mock(ProcessStats.Cpu.class);
        PowerMockito.when(cpu.getPercent())
                    .thenReturn((short) 100);
        PowerMockito.when(cpu.getTotal())
                    .thenReturn(tv);
        // given
        ProcessStats process = PowerMockito.mock(ProcessStats.class);
        PowerMockito.when(process.getTimestamp())
                    .thenReturn(100L);
        PowerMockito.when(process.getOpenFileDescriptors())
                    .thenReturn(100L);
        PowerMockito.when(process.getMaxFileDescriptors())
                    .thenReturn(100L);
        PowerMockito.when(process.getCpu())
                    .thenReturn(cpu);
        // given
        FsInfo.Path fp = PowerMockito.mock(FsInfo.Path.class);
        PowerMockito.when(fp.getTotal())
                    .thenReturn(size);
        PowerMockito.when(fp.getFree())
                    .thenReturn(size);
        PowerMockito.when(fp.getAvailable())
                    .thenReturn(size);
        // given
        FsInfo fs = PowerMockito.mock(FsInfo.class);
        PowerMockito.when(fs.getTimestamp())
                    .thenReturn(100L);
        PowerMockito.when(fs.getTotal())
                    .thenReturn(fp);
        // given
        JvmStats jvm = PowerMockito.mock(JvmStats.class);
        PowerMockito.when(jvm.getTimestamp())
                    .thenReturn(100L);
        GarbageCollectors gc = PowerMockito.mock(GarbageCollectors.class);
        GarbageCollector c1 = PowerMockito.mock(GarbageCollector.class);
        PowerMockito.when(c1.getName())
                    .thenReturn("young");
        PowerMockito.when(c1.getCollectionCount())
                    .thenReturn(100L);
        PowerMockito.when(c1.getCollectionTime())
                    .thenReturn(tv);
        GarbageCollector c2 = PowerMockito.mock(GarbageCollector.class);
        PowerMockito.when(c2.getName())
                    .thenReturn("old");
        PowerMockito.when(c2.getCollectionCount())
                    .thenReturn(100L);
        PowerMockito.when(c2.getCollectionTime())
                    .thenReturn(tv);
        PowerMockito.when(gc.getCollectors())
                    .thenReturn(new GarbageCollector[] {c1, c2});
        PowerMockito.when(jvm.getGc())
                    .thenReturn(gc);
        Mem mem = PowerMockito.mock(Mem.class);
        PowerMockito.when(mem.getHeapCommitted())
                    .thenReturn(size);
        PowerMockito.when(mem.getHeapMax())
                    .thenReturn(size);
        PowerMockito.when(mem.getHeapUsed())
                    .thenReturn(size);
        PowerMockito.when(mem.getHeapUsedPercent())
                    .thenReturn((short) 100);
        PowerMockito.when(mem.getNonHeapCommitted())
                    .thenReturn(size);
        PowerMockito.when(mem.getNonHeapUsed())
                    .thenReturn(size);
        MemoryPool m1 = PowerMockito.mock(MemoryPool.class);
        PowerMockito.when(m1.getName())
                    .thenReturn("young");
        PowerMockito.when(m1.getMax())
                    .thenReturn(size);
        PowerMockito.when(m1.getPeakMax())
                    .thenReturn(size);
        PowerMockito.when(m1.getPeakUsed())
                    .thenReturn(size);
        PowerMockito.when(m1.getUsed())
                    .thenReturn(size);
        MemoryPool m2 = PowerMockito.mock(MemoryPool.class);
        PowerMockito.when(m2.getName())
                    .thenReturn("old");
        PowerMockito.when(m2.getMax())
                    .thenReturn(size);
        PowerMockito.when(m2.getPeakMax())
                    .thenReturn(size);
        PowerMockito.when(m2.getPeakUsed())
                    .thenReturn(size);
        PowerMockito.when(m2.getUsed())
                    .thenReturn(size);
        MemoryPool m3 = PowerMockito.mock(MemoryPool.class);
        PowerMockito.when(m3.getName())
                    .thenReturn("survivor");
        PowerMockito.when(m3.getMax())
                    .thenReturn(size);
        PowerMockito.when(m3.getPeakMax())
                    .thenReturn(size);
        PowerMockito.when(m3.getPeakUsed())
                    .thenReturn(size);
        PowerMockito.when(m3.getUsed())
                    .thenReturn(size);
        Whitebox.setInternalState(mem,
                                  "pools",
                                  new MemoryPool[] {m1, m2, m3});
        PowerMockito.when(mem.iterator())
                    .thenReturn(Iterators.forArray(new MemoryPool[] {m1, m2, m3}));
        PowerMockito.when(jvm.getMem())
                    .thenReturn(mem);
        Threads threads = PowerMockito.mock(Threads.class);
        PowerMockito.when(threads.getCount())
                    .thenReturn(100);
        PowerMockito.when(threads.getPeakCount())
                    .thenReturn(100);
        PowerMockito.when(jvm.getThreads())
                    .thenReturn(threads);
        BufferPool p1 = PowerMockito.mock(BufferPool.class);
        PowerMockito.when(p1.getName())
                    .thenReturn("direct");
        PowerMockito.when(p1.getCount())
                    .thenReturn(100L);
        PowerMockito.when(p1.getTotalCapacity())
                    .thenReturn(size);
        PowerMockito.when(p1.getUsed())
                    .thenReturn(size);
        BufferPool p2 = PowerMockito.mock(BufferPool.class);
        PowerMockito.when(p2.getName())
                    .thenReturn("mapped");
        PowerMockito.when(p2.getCount())
                    .thenReturn(100L);
        PowerMockito.when(p2.getTotalCapacity())
                    .thenReturn(size);
        PowerMockito.when(p2.getUsed())
                    .thenReturn(size);
        PowerMockito.when(jvm.getBufferPools())
                    .thenReturn(Arrays.asList(p1,
                                              p2));
        // given
        NodeStats node = PowerMockito.mock(NodeStats.class);
        PowerMockito.when(node.getIndices())
                    .thenReturn(nodeIS);
        PowerMockito.when(node.getNode())
                    .thenReturn(dNode);
        PowerMockito.when(node.getOs())
                    .thenReturn(osStats);
        PowerMockito.when(node.getProcess())
                    .thenReturn(process);
        PowerMockito.when(node.getFs())
                    .thenReturn(fs);
        PowerMockito.when(node.getJvm())
                    .thenReturn(jvm);
        PowerMockito.when(node.getTimestamp())
                    .thenReturn(100L);
        // given
        genrealResp = PowerMockito.mock(NodesStatsResponse.class);
        PowerMockito.when(genrealResp.getNodes())
                    .thenReturn(new NodeStats[] {node});
        Map<String, NodeStats> nodeMap = new HashMap<String, NodeStats>();
        nodeMap.put("_id_1",
                    node);
        PowerMockito.when(genrealResp.getNodesMap())
                    .thenReturn(nodeMap);
    }
    
    /**
     * testBuild01: <br/>
     * 
     * @author hadoop
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @since JDK 1.8
     */
    @Test
    public void testBuild01() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.index_total");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild02: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild02() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.index_time_in_millis");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild03: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild03() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.index_current");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild04: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild04() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.index_failed");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild05: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild05() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.delete_total");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild06: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild06() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.delete_time_in_millis");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild07: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild07() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.delete_current");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild08: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild08() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.noop_update_total");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild09: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild09() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.indexing.throttle_time_in_millis");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild10: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild10() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.open_contexts");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild11: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild11() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.query_total");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild12: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild12() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.query_time_in_millis");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild13: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild13() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.query_current");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild14: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild14() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.fetch_total");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild15: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild15() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.fetch_time_in_millis");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild16: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild16() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.fetch_current");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild17: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild17() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.scroll_total");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild18: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild18() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.scroll_time_in_millis");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild19: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild19() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.indices.search.scroll_current");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild20: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild20() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.os.load_average");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild21: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild21() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.os.mem.total_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild22: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild22() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.os.mem.free_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild23: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild23() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.os.mem.used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild24: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild24() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.os.mem.free_percent");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild25: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild25() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.os.mem.used_percent");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild26: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild26() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.process.open_file_descriptors");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild27: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild27() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.process.max_file_descriptors");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild28: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild28() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.process.cpu.percent");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild29: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild29() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.process.cpu.total_in_millis");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild30: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild30() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.fs.total.total_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild31: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild31() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.fs.total.free_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild32: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild32() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.fs.total.available_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild33: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild33() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.gc.collectors.young.collection_count");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild34: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild34() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.gc.collectors.young.collection_time_in_millis");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild36: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild36() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.gc.collectors.old.collection_count");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild37: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild37() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.gc.collectors.old.collection_time_in_millis");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild38: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild38() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.threads.count");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild39: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild39() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.threads.peak_count");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild40: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild40() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.heap_used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild41: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild41() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.heap_used_percent");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild42: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild42() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.heap_committed_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild43: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild43() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.heap_max_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild44: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild44() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.non_heap_used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild45: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild45() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.non_heap_committed_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild46: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild46() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.young.used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild47: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild47() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.young.max_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild48: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild48() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.young.peak_used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild49: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild49() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.young.peak_max_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild50: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild50() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.old.used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild51: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild51() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.old.max_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild52: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild52() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.old.peak_used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild53: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild53() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.old.peak_max_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild54: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild54() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.survivor.used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild55: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild55() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.survivor.max_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild56: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild56() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.survivor.peak_used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild57: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild57() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //
        init();
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.mem.pools.survivor.peak_max_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild58: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild58() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.buffer_pools.direct.count");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild59: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild59() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.buffer_pools.direct.used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild60: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild60() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.buffer_pools.direct.total_capacity_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild61: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild61() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.buffer_pools.mapped.count");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild62: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild62() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.buffer_pools.mapped.used_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
    /**
     * testBuild63: <br/>
     * 
     * @author hadoop
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @since JDK 1.8
     */
    @Test
    public void testBuild63() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // given
        ElasticsearchSourceUniformBuilder builder = new ElasticsearchSourceUniformBuilder();
        // given
        Context mock = Mockito.mock(Context.class);
        Mockito.when(mock.getString(Mockito.anyString(),
                                    Mockito.anyString()))
               .thenReturn("nodes.jvm.buffer_pools.mapped.total_capacity_in_bytes");
        // given
        PowerMockito.mockStatic(StringUtils.class);
        Mockito.when(StringUtils.isBlank(Mockito.any()))
               .thenReturn(false);
        // given
        builder.config(mock);
        // when
        List<ElasticsearchSourceUniform> result = builder.build(genrealResp);
        // then
        MatcherAssert.assertThat(result,
                                 CoreMatchers.notNullValue());
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.not(0));
        // then
        MatcherAssert.assertThat(result.size(),
                                 CoreMatchers.equalTo(1));
        // then
        MatcherAssert.assertThat(result.get(0)
                                       .getMetricdata(),
                                 CoreMatchers.equalTo(Long.valueOf(100L)
                                                          .doubleValue()));
        
    }
    
}
