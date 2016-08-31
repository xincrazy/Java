package com.oneapm.log.agent.flume.source.elasticsearch.v211;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.flume.Context;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequestBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.google.common.collect.ImmutableMap;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceEventBody;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.util.ElasticsearchSourceConstants;


public class TestElasticsearchSourceEventBodyIterator{
	
	ElasticsearchSourceEventBodyIterator elasticsearchSourceEventBodyIterator = new ElasticsearchSourceEventBodyIterator();
	
	@Test
	public void testBuild(){
		Context context = new Context();
		elasticsearchSourceEventBodyIterator.build(context);
		
	}
	
	@Test
	public void testBuilder1(){
		Map<String,String> map = new HashMap<>();
		map.put(ElasticsearchSourceConstants.DURATION, "600000");
		map.put(ElasticsearchSourceConstants.DEFAULT_NODES_FILTERS, "breaker,fs,http,jvm,os,process,script,threadpool,indices,transport");
		map.put(ElasticsearchSourceConstants.NODES_FILTERS, "breaker");
		map.put(ElasticsearchSourceConstants.BUILDER_TYPE, "com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceUniformBuilder");
		map.put(ElasticsearchSourceConstants.DEFAULT_BUILDER_TYPE, "com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceUniformBuilder");
		map.put(ElasticsearchSourceConstants.BUFFER_SIZE, "10000");
		Context context = new Context(map);
		elasticsearchSourceEventBodyIterator.build(context);
	}
	
	@Test
	public void testConfigNodesStatsRequestBuilder() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		NodesStatsRequestBuilder builder = Mockito.mock(NodesStatsRequestBuilder.class);
        String filters = "";
		Method targetMethod = ElasticsearchSourceEventBodyIterator.class.getDeclaredMethod("configNodesStatsRequestBuilder", NodesStatsRequestBuilder.class,String.class);
		targetMethod.setAccessible(true);
		Assert.assertNotNull(targetMethod.invoke(elasticsearchSourceEventBodyIterator,builder,filters));
	}
	
	@Test
	public void testRun(){
		ScheduledExecutorService scheduler = Mockito.mock(ScheduledExecutorService.class);
	    BlockingQueue<ElasticsearchSourceEventBody> queue = Mockito.mock(BlockingQueue.class);
	    Whitebox.setInternalState(elasticsearchSourceEventBodyIterator, "scheduler", scheduler);
	    Whitebox.setInternalState(elasticsearchSourceEventBodyIterator, "queue", queue);
		elasticsearchSourceEventBodyIterator.run();
	}
	
	@Test
	public void testCLose(){
		ScheduledExecutorService scheduler = Mockito.mock(ScheduledExecutorService.class);
	    BlockingQueue<ElasticsearchSourceEventBody> queue = Mockito.mock(BlockingQueue.class);
	    Whitebox.setInternalState(elasticsearchSourceEventBodyIterator, "scheduler", scheduler);
	    Whitebox.setInternalState(elasticsearchSourceEventBodyIterator, "queue", queue);
		elasticsearchSourceEventBodyIterator.close();
	}
	
	@Test
	public void testHasNext(){
	    BlockingQueue<ElasticsearchSourceEventBody> queue = Mockito.mock(BlockingQueue.class);
	    Whitebox.setInternalState(elasticsearchSourceEventBodyIterator, "queue", queue);
	    boolean result = elasticsearchSourceEventBodyIterator.hasNext();
	    Assert.assertTrue(result);		
	}
	
	@Test
	public void testNext(){
		BlockingQueue<ElasticsearchSourceEventBody> queue = Mockito.mock(BlockingQueue.class);
	    Whitebox.setInternalState(elasticsearchSourceEventBodyIterator, "queue", queue);
		ElasticsearchSourceEventBody next = elasticsearchSourceEventBodyIterator.next();
		Assert.assertNull(next);
	}
	
	@Test
	public void testGetRemaining(){
		BlockingQueue<ElasticsearchSourceEventBody> queue = Mockito.mock(BlockingQueue.class);
	    Whitebox.setInternalState(elasticsearchSourceEventBodyIterator, "queue", queue);
		int getRemaining = elasticsearchSourceEventBodyIterator.getRemaining();
		Assert.assertEquals(0, getRemaining);
	}
}






















