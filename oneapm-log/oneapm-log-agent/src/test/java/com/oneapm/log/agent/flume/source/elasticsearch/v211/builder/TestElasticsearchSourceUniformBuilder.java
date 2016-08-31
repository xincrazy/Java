package com.oneapm.log.agent.flume.source.elasticsearch.v211.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.elasticsearch.index.search.stats.SearchStats;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.oneapm.log.agent.flume.source.elasticsearch.v211.util.ElasticsearchSourceConstants;



public class TestElasticsearchSourceUniformBuilder{
	
	ElasticsearchSourceUniformBuilder elasticsearchSourceUniformBuilder = new ElasticsearchSourceUniformBuilder();
	
	@Test
	public void testBuilder(){
		SearchStats stats = Mockito.mock(SearchStats.class);
		List<ElasticsearchSourceUniform> result = elasticsearchSourceUniformBuilder.build(stats);
		Assert.assertEquals(0, result.size());
	}
	
	@Test
	public void testBuilder1(){
		SearchStats stats = null;
		List<ElasticsearchSourceUniform> result =  elasticsearchSourceUniformBuilder.build(stats);
		Assert.assertEquals(0, result.size());
	}
	
	@Test
	public void testConfig(){
		Map<String,String> map = new HashMap<>();
		map.put(ElasticsearchSourceConstants.NODES_METRICS, "nodes.metrics");
		map.put(ElasticsearchSourceConstants.DEFAULT_NODES_METRICS, "nodes.indices.indexing.index_total");
		map.put(ElasticsearchSourceConstants.INDICES_METRICS, "indices.metrics");
		map.put(ElasticsearchSourceConstants.DEFAULT_INDICES_METRICS, "indices.total.search.query_total");
		Context context = new Context(map);
		ElasticsearchSourceUniformBuilder config = elasticsearchSourceUniformBuilder.config(context);
		Assert.assertNotNull(config);
	}
	
}