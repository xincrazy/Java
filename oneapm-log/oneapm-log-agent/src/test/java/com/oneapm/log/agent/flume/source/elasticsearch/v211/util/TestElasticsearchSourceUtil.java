package com.oneapm.log.agent.flume.source.elasticsearch.v211.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.flume.Context;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Assert;
import org.junit.Test;

public class TestElasticsearchSourceUtil{
	
	ElasticsearchSourceUtil elasticsearchSourceUtil = new ElasticsearchSourceUtil();
	
	@Test
	public void testGetElasticProperties(){
		Context context = new Context();
		Properties properties = elasticsearchSourceUtil.getElasticProperties(context);
		Assert.assertNotNull(properties);
	}
	
	@Test
	public void testGetTransClient(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("HOST_NAME", "host.name");
		map.put("DEFAULT_HOST_NAME", "127.0.0.1");
		Context context = new Context(map);
		TransportClient TransportClient = elasticsearchSourceUtil.getTransClient(context);
		Assert.assertNotNull(TransportClient);
	}
	
	@Test
	public void testGetAdminClient(){
		Context context = new Context();
		ClusterAdminClient clusterAdminClient = elasticsearchSourceUtil.getAdminClient(context);
		Assert.assertNotNull(clusterAdminClient);
	}
	
	@Test
	public void testGetIndicesClient(){
		Context context = new Context();
		IndicesAdminClient indicesAdminClient = elasticsearchSourceUtil.getIndicesClient(context);
		Assert.assertNotNull(indicesAdminClient);
	}
	
	@Test
	public void testSetElasticProps() throws ClassNotFoundException,NoSuchMethodException,SecurityException,
	IllegalAccessException,IllegalArgumentException,InvocationTargetException{
		Class c = Class.forName("com.oneapm.log.agent.flume.source.elasticsearch.v211.util.ElasticsearchSourceUtil");
		Method format;
		format = c.getDeclaredMethod("setElasticProps", Context.class,Properties.class);
		format.setAccessible(true);
		Map<String,String> map = new HashMap<String,String>();
		map.put("PROPERTY_PREFIX", "elasticsearch");
		Context context = new Context(map);
		Properties props = new Properties();
		format.invoke(elasticsearchSourceUtil, context,props);	
		Assert.assertNotNull(props);
		Assert.assertNotNull(context);
	}
}