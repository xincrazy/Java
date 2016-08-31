package com.oneapm.log.agent.flume.source.elasticsearch.v211.builder;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.flume.Context;
import org.elasticsearch.action.admin.cluster.node.stats.NodeStats;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.http.HttpStats;
import org.elasticsearch.index.indexing.IndexingStats;
import org.elasticsearch.index.search.stats.SearchStats;
import org.elasticsearch.indices.NodeIndicesStats;
import org.elasticsearch.indices.breaker.AllCircuitBreakerStats;
import org.elasticsearch.monitor.fs.FsInfo;
import org.elasticsearch.monitor.jvm.JvmStats;
import org.elasticsearch.monitor.os.OsStats;
import org.elasticsearch.monitor.os.OsStats.Mem;
import org.elasticsearch.monitor.process.ProcessStats;
import org.elasticsearch.script.ScriptStats;
import org.elasticsearch.threadpool.ThreadPoolStats;
import org.elasticsearch.transport.TransportStats;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;


public class TestElasticsearchSourceClusterStatsBuilder{
	
	ElasticsearchSourceClusterStatsBuilder elasticsearchSourceClusterStatsBuilder = new ElasticsearchSourceClusterStatsBuilder();
	
	@Test
	public void testMapperNodeStats() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method targetMethod = ElasticsearchSourceClusterStatsBuilder.class.getDeclaredMethod("mapperNodeStats", String.class,NodeStats.class);
		targetMethod.setAccessible(true);
		String clusterName = "elasticsearch";
        DiscoveryNode node1 = Mockito.mock(DiscoveryNode.class);
        long timestamp = 1467;
        NodeIndicesStats indices = Mockito.mock(NodeIndicesStats.class);
        OsStats os = Mockito.mock(OsStats.class);
        ProcessStats process = Mockito.mock(ProcessStats.class);
        JvmStats jvm = Mockito.mock(JvmStats.class);
        ThreadPoolStats threadPool = Mockito.mock(ThreadPoolStats.class);
        FsInfo fs = Mockito.mock(FsInfo.class);
        TransportStats transport = Mockito.mock(TransportStats.class);
        HttpStats http = Mockito.mock(HttpStats.class);
        AllCircuitBreakerStats breaker = Mockito.mock(AllCircuitBreakerStats.class);
        ScriptStats scriptStats = Mockito.mock(ScriptStats.class);
        Mem mem =mock(Mem.class);
        IndexingStats.Stats st =mock(IndexingStats.Stats.class);
        SearchStats.Stats s = mock(SearchStats.Stats.class);
        IndexingStats indexing = mock(IndexingStats.class);
        SearchStats search = mock(SearchStats.class);
        NodeStats node = new NodeStats(node1,timestamp,indices,os,process,jvm,threadPool,fs, transport, http, breaker,scriptStats);
        when(node.getOs().getMem()).thenReturn(mem);
        when(node.getIndices().getIndexing()).thenReturn(indexing);
        when(node.getIndices().getIndexing().getTotal()).thenReturn(st);
        when(node.getIndices().getSearch()).thenReturn(search);
        when(node.getIndices().getSearch().getTotal()).thenReturn(s);
        ElasticsearchSourceNodesStats result = (ElasticsearchSourceNodesStats) targetMethod.invoke(elasticsearchSourceClusterStatsBuilder,clusterName,node);
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testBuilder(){
		NodesStatsResponse stats = Mockito.mock(NodesStatsResponse.class);
		List<ElasticsearchSourceNodesStats> result = elasticsearchSourceClusterStatsBuilder.build(stats);
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}
	
	@Test
	public void testConfig(){
		Context context = new Context();
		ElasticsearchSourceClusterStatsBuilder result = elasticsearchSourceClusterStatsBuilder.config(context);
		Assert.assertNotNull(result);
	}
	
}