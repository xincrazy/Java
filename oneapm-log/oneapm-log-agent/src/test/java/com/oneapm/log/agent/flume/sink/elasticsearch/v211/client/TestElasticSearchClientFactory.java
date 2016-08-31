package com.oneapm.log.agent.flume.sink.elasticsearch.v211.client;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.oneapm.log.agent.flume.sink.elasticsearch.v211.ElasticSearchEventSerializer;
import com.oneapm.log.agent.flume.sink.elasticsearch.v211.ElasticSearchIndexRequestBuilderFactory;
import com.oneapm.log.common.utils.HostNameUtil;



public class TestElasticSearchClientFactory{
	
	@Test
	public void testGetClient() {
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
		String clientType = "clientType";
        String[] hostNames = {HostNameUtil.getHostName()};
        String clusterName = "clusterName";
        Integer retry = 1;
        ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
        ElasticSearchIndexRequestBuilderFactory indexBuilder = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
        try {
        	ElasticSearchClient result =  elasticSearchClientFactory.getClient(clientType, hostNames, clusterName, retry, serializer, indexBuilder);
        	Assert.assertNotNull(result);
		} catch (NoSuchClientTypeException e) {
		}
	}
	@Test
	public void testGetClient1(){
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
		String clientType = "transport";
        String[] hostNames = {HostNameUtil.getHostName()};
        String clusterName = "clusterName";
        Integer retry = 1;
        ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
        ElasticSearchIndexRequestBuilderFactory indexBuilder = null;
        try {
        	ElasticSearchClient result = elasticSearchClientFactory.getClient(clientType, hostNames, clusterName, retry, serializer, indexBuilder);
			Assert.assertNotNull(result);
		} catch (NoSuchClientTypeException e) {
		}
	}
	
	@Test 
	public void testGetClient2(){
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
		String clientType = "transport";
        String[] hostNames = {HostNameUtil.getHostName()};
        String clusterName = "clusterName";
        Integer retry = 1;
        ElasticSearchEventSerializer serializer = null;
        ElasticSearchIndexRequestBuilderFactory indexBuilder = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
        try {
        	ElasticSearchClient result = elasticSearchClientFactory.getClient(clientType, hostNames, clusterName, retry, serializer, indexBuilder);
		} catch (NoSuchClientTypeException e) {
		}
	}
	
	@Test
	public void testGetClient3(){
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
		String clientType = "rest";
        String[] hostNames = {HostNameUtil.getHostName()};
        String clusterName = "clusterName";
        Integer retry = 1;
        ElasticSearchEventSerializer serializer = null;
        ElasticSearchIndexRequestBuilderFactory indexBuilder = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
        try {
        	ElasticSearchClient result = elasticSearchClientFactory.getClient(clientType, hostNames, clusterName, retry, serializer, indexBuilder);
        	Assert.assertNotNull(result);

        } catch (NoSuchClientTypeException e) {
		}
	}
	
	@Test
	public void testGetClient4(){
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
		String clientType = "rest";
        String[] hostNames = {HostNameUtil.getHostName()};
        String clusterName = "clusterName";
        Integer retry = 1;
        ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
        ElasticSearchIndexRequestBuilderFactory indexBuilder = null;
        try {
        	ElasticSearchClient result = elasticSearchClientFactory.getClient(clientType, hostNames, clusterName, retry, serializer, indexBuilder);
        	Assert.assertNotNull(result);
        } catch (NoSuchClientTypeException e) {
		}
	}	

	@Test
	public void testGetLocalClient2(){
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
		String clientType = "transport";
        ElasticSearchEventSerializer serializer1 = null;
		ElasticSearchClient result1;
        ElasticSearchIndexRequestBuilderFactory indexBuilder1 = null;
        try {
			result1 = elasticSearchClientFactory.getLocalClient(clientType, serializer1, indexBuilder1);
			Assert.assertNotNull(result1);
		} catch (NoSuchClientTypeException e) {	
		}
	}
	@Test
	public void testGetLocalClient3(){
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
		String clientType = "rest";
        ElasticSearchEventSerializer serializer1 = null;
		ElasticSearchClient result1;
        ElasticSearchIndexRequestBuilderFactory indexBuilder1 = null;
        try {
			result1 = elasticSearchClientFactory.getLocalClient(clientType, serializer1, indexBuilder1);
			Assert.assertNotNull(result1);
		} catch (NoSuchClientTypeException e) {	
		}
	}
	@Test
	public void testGetLocalClient4(){
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
		String clientType = "rest";
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
        ElasticSearchIndexRequestBuilderFactory indexBuilder1 = null;
        try {
			ElasticSearchClient result1 = elasticSearchClientFactory.getLocalClient(clientType, serializer, indexBuilder1);
			Assert.assertNotNull(result1);
		} catch (NoSuchClientTypeException e) {	
		}
	}
	
	@Test
	public void testGetLocalClient5(){
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
		String clientType = "rest";
        ElasticSearchIndexRequestBuilderFactory indexBuilder = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
        ElasticSearchEventSerializer serializer1 = null;
        try {
			ElasticSearchClient result1 = elasticSearchClientFactory.getLocalClient(clientType, serializer1, indexBuilder);
			Assert.assertNotNull(result1);
		} catch (NoSuchClientTypeException e) {	
		}
	}
	@Test
	public void testGetLocalClient6(){
		ElasticSearchClientFactory elasticSearchClientFactory = new ElasticSearchClientFactory();
        ElasticSearchEventSerializer serializer1 = null;
        ElasticSearchIndexRequestBuilderFactory indexBuilder1 = null;
        try {
			ElasticSearchClient result1 = elasticSearchClientFactory.getLocalClient("hello", serializer1, indexBuilder1);
			Assert.assertNotNull(result1);
		} catch (NoSuchClientTypeException e) {	
		}
	}
}