package com.oneapm.log.agent.flume.source.elasticsearch.v211.util;

import org.junit.Assert;
import org.junit.Test;


public class TestElasticsearchSourceConstants{
	
	ElasticsearchSourceConstants elasticsearchSourceConstants = new ElasticsearchSourceConstants();
	
	@Test
	public void testContains(){
		Assert.assertEquals("batch.size", elasticsearchSourceConstants.BATCH_SIZE);
		Assert.assertEquals(60000, elasticsearchSourceConstants.DEFAULT_DURATION);
		Assert.assertEquals("buffer.size", elasticsearchSourceConstants.BUFFER_SIZE);
		Assert.assertEquals("builder.type", elasticsearchSourceConstants.BUILDER_TYPE);
		Assert.assertEquals("cluster.name", elasticsearchSourceConstants.CLUSTER_NAME);
		Assert.assertEquals(50, elasticsearchSourceConstants.DEFAULT_BATCH_SIZE);
		Assert.assertEquals(5000, elasticsearchSourceConstants.DEFAULT_BUFFER_SIZE);
		Assert.assertEquals("com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceUniformBuilder", elasticsearchSourceConstants.DEFAULT_BUILDER_TYPE);
		Assert.assertEquals("UTF-8", elasticsearchSourceConstants.DEFAULT_CHARACTER);
		Assert.assertEquals("elasticsearch", elasticsearchSourceConstants.DEFAULT_CLUSTER_NAME);
		Assert.assertEquals("127.0.0.1", elasticsearchSourceConstants.DEFAULT_HOST_NAME);
		Assert.assertEquals("completion,docs,fielddata,flush,get,indexing,merge,percolate,querycache,recovery,refresh,requestcache,search,segments,store,suggest,translog,warmer", elasticsearchSourceConstants.DEFAULT_INDICES_FILTERS);
		Assert.assertEquals("_all", elasticsearchSourceConstants.DEFAULT_INDICES_INDEXNAME);
		Assert.assertEquals("breaker,fs,http,jvm,os,process,script,threadpool,indices,transport", elasticsearchSourceConstants.DEFAULT_NODES_FILTERS);
		Assert.assertEquals(9300, elasticsearchSourceConstants.DEFAULT_PORT);
		Assert.assertEquals("elasticsearch", elasticsearchSourceConstants.DEFAULT_SERVICE_TYPE);
		Assert.assertEquals("\\s*,\\s*", elasticsearchSourceConstants.DEFAULT_SPLITS);
		Assert.assertEquals("duration", elasticsearchSourceConstants.DURATION);
		Assert.assertEquals("host.name", elasticsearchSourceConstants.HOST_NAME);
		Assert.assertEquals("indices.filters", elasticsearchSourceConstants.INDICES_FILTERS);
		Assert.assertEquals("indices.metrics", elasticsearchSourceConstants.INDICES_METRICS);
		Assert.assertEquals("indices.indexname", elasticsearchSourceConstants.INDICES_INDEXNAME);
		Assert.assertEquals("nodes.filters", elasticsearchSourceConstants.NODES_FILTERS);
		Assert.assertEquals("nodes.metrics", elasticsearchSourceConstants.NODES_METRICS);
		Assert.assertEquals("port", elasticsearchSourceConstants.PORT);
		Assert.assertEquals("elasticsearch.", elasticsearchSourceConstants.PROPERTY_PREFIX);
		
	}
}