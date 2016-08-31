package com.oneapm.log.agent.flume.sink.elasticsearch.v211.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.BytesStream;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Test.None;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.oneapm.log.agent.flume.sink.elasticsearch.v211.ElasticSearchEventSerializer;
import com.oneapm.log.agent.flume.sink.elasticsearch.v211.ElasticSearchIndexRequestBuilderFactory;
import com.oneapm.log.agent.flume.sink.elasticsearch.v211.IndexNameBuilder;
import com.oneapm.log.common.utils.HostNameUtil;

public class TestElasticSearchTransportClient{
	
	@Test(expected=None.class)
	public void testGetServerAddresses(){
		InetSocketTransportAddress [] serverAddresses = new InetSocketTransportAddress[3];
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
	    ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
	    BulkRequestBuilder bulkRequestBuilder = Mockito.mock(BulkRequestBuilder.class);
	    Client client = Mockito.mock(Client.class);
		
		String[] hostNames ={HostNameUtil.getHostName()};
        String clusterName ="clusterName";
        Integer retry = 2;
        ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(hostNames,clusterName,retry,serializer1);
		
		Whitebox.setInternalState(elasticSearchTransportClient, "serverAddresses", serverAddresses);
		Whitebox.setInternalState(elasticSearchTransportClient, "serializer", serializer);
		Whitebox.setInternalState(elasticSearchTransportClient, "indexRequestBuilderFactory", indexRequestBuilderFactory);
		Whitebox.setInternalState(elasticSearchTransportClient, "bulkRequestBuilder", bulkRequestBuilder);
		Whitebox.setInternalState(elasticSearchTransportClient, "client", client);
		Assert.assertNotNull(elasticSearchTransportClient.getServerAddresses());
		
	}

	@Test(expected=None.class)
	public void testSet(){
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
	    ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
	    BulkRequestBuilder bulkRequestBuilder = Mockito.mock(BulkRequestBuilder.class);
	    Client client = Mockito.mock(Client.class);
		
		String[] hostNames ={HostNameUtil.getHostName()};
        String clusterName ="clusterName";
        Integer retry = 2;
        ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(hostNames,clusterName,retry,serializer1);
		
		Whitebox.setInternalState(elasticSearchTransportClient, "serializer", serializer);
		Whitebox.setInternalState(elasticSearchTransportClient, "indexRequestBuilderFactory", indexRequestBuilderFactory);
		Whitebox.setInternalState(elasticSearchTransportClient, "bulkRequestBuilder", bulkRequestBuilder);
		Whitebox.setInternalState(elasticSearchTransportClient, "client", client);
		
		BulkRequestBuilder bulkRequestBuilder1 = Mockito.mock(BulkRequestBuilder.class);
		elasticSearchTransportClient.setBulkRequestBuilder(bulkRequestBuilder1);
	}
	
	@Test(expected=None.class)
	public void testStru2(){
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		BulkRequestBuilder bulkRequestBuilder = Mockito.mock(BulkRequestBuilder.class);
	    Client client = Mockito.mock(Client.class);
				
		String[] hostNames ={HostNameUtil.getHostName()};
		String clusterName ="clusterName";
		Integer retry = 2;
        ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchIndexRequestBuilderFactory indexBuilder = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(hostNames,clusterName,retry,indexBuilder);		
		Whitebox.setInternalState(elasticSearchTransportClient, "serializer", serializer);
		Whitebox.setInternalState(elasticSearchTransportClient, "indexRequestBuilderFactory", indexRequestBuilderFactory);
		Whitebox.setInternalState(elasticSearchTransportClient, "bulkRequestBuilder", bulkRequestBuilder);
		Whitebox.setInternalState(elasticSearchTransportClient, "client", client);
	}
	
	@Test(expected=None.class)
	public void testStru5(){
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
	    Client client = Mockito.mock(Client.class);
	    ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
	    Client client1 = Mockito.mock(Client.class);
		ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(client1,serializer1);
		Whitebox.setInternalState(elasticSearchTransportClient, "serializer", serializer);
		Whitebox.setInternalState(elasticSearchTransportClient, "client", client);
	}
	
	@Test(expected=None.class)
	public void tsetStu6() throws IOException{
		 Client client = Mockito.mock(Client.class);
		 ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		 Client client1 = Mockito.mock(Client.class);
		 ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory2 = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		 ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(client1,indexRequestBuilderFactory2);
		 Whitebox.setInternalState(elasticSearchTransportClient, "client", client);
		 Whitebox.setInternalState(elasticSearchTransportClient, "indexRequestBuilderFactory", indexRequestBuilderFactory);
	}
	
	@Test(expected=None.class)
	public void testClose()  throws Exception{
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		BulkRequestBuilder bulkRequestBuilder = Mockito.mock(BulkRequestBuilder.class);
		Client client = Mockito.mock(Client.class);	
		String[] hostNames ={HostNameUtil.getHostName()};
		String clusterName ="clusterName";
		Integer retry = 2;
		ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(hostNames,clusterName,retry,serializer1);
		Whitebox.setInternalState(elasticSearchTransportClient, "serializer", serializer);
		Whitebox.setInternalState(elasticSearchTransportClient, "indexRequestBuilderFactory", indexRequestBuilderFactory);
		Whitebox.setInternalState(elasticSearchTransportClient, "bulkRequestBuilder", bulkRequestBuilder);
		Whitebox.setInternalState(elasticSearchTransportClient, "client", client);
		elasticSearchTransportClient.close();
	}
	
	@Test(expected=None.class)
	public void testClose1()  throws Exception{
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		BulkRequestBuilder bulkRequestBuilder = Mockito.mock(BulkRequestBuilder.class);
		String[] hostNames ={HostNameUtil.getHostName()};
		String clusterName ="clusterName";
		Integer retry = 2;
		ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(hostNames,clusterName,retry,serializer1);
		Whitebox.setInternalState(elasticSearchTransportClient, "serializer", serializer);
		Whitebox.setInternalState(elasticSearchTransportClient, "indexRequestBuilderFactory", indexRequestBuilderFactory);
		Whitebox.setInternalState(elasticSearchTransportClient, "bulkRequestBuilder", bulkRequestBuilder);
		elasticSearchTransportClient.close();
	}
	
	@Test(expected=None.class)
	public void testAddEvent() {
		Map<String,String> map = new HashMap<>();
		map.put("indexType", "indexType");
		map.put("indexPrefix", "indexPrefix");
		map.put("document", "document");
		Event event = EventBuilder.withBody("hello", Charset.defaultCharset(),map);
        IndexNameBuilder indexNameBuilder = Mockito.mock(IndexNameBuilder.class);
        String indexType ="indexType";
        long ttlMs = 1;        
        ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		BulkRequestBuilder bulkRequestBuilder = Mockito.mock(BulkRequestBuilder.class);
		Client client = Mockito.mock(Client.class);	
		String[] hostNames ={HostNameUtil.getHostName()};
		String clusterName ="clusterName";
		Integer retry = 2;
		ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(hostNames,clusterName,retry,serializer1);
		Whitebox.setInternalState(elasticSearchTransportClient, "serializer", serializer);
		Whitebox.setInternalState(elasticSearchTransportClient, "indexRequestBuilderFactory", indexRequestBuilderFactory);
		Whitebox.setInternalState(elasticSearchTransportClient, "bulkRequestBuilder", bulkRequestBuilder);
		Whitebox.setInternalState(elasticSearchTransportClient, "client", client);
		try {
			elasticSearchTransportClient.addEvent(event, indexNameBuilder, indexType, ttlMs);
		} catch (Exception e) {
		}
	}
	
	@Test(expected=None.class)
	public void testConfigureHostnames(){
		InetSocketTransportAddress [] serverAddresses = new InetSocketTransportAddress[3];
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
	    ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
	    BulkRequestBuilder bulkRequestBuilder = Mockito.mock(BulkRequestBuilder.class);
	    Client client = Mockito.mock(Client.class);
		
		String[] hostNames ={HostNameUtil.getHostName()};
		String[] hostName ={HostNameUtil.getHostName()};
        String clusterName ="clusterName";
        Integer retry = 2;
        ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(hostNames,clusterName,retry,serializer1);
		
		Whitebox.setInternalState(elasticSearchTransportClient, "serverAddresses", serverAddresses);
		Whitebox.setInternalState(elasticSearchTransportClient, "serializer", serializer);
		Whitebox.setInternalState(elasticSearchTransportClient, "indexRequestBuilderFactory", indexRequestBuilderFactory);
		Whitebox.setInternalState(elasticSearchTransportClient, "bulkRequestBuilder", bulkRequestBuilder);
		Whitebox.setInternalState(elasticSearchTransportClient, "client", client);
		
		elasticSearchTransportClient.getClass();
		Method targetMethod;
		try {
			targetMethod = ElasticSearchTransportClient.class.getDeclaredMethod("configureHostnames", String.class);
			targetMethod.setAccessible(true);
			try {
				targetMethod.invoke(elasticSearchTransportClient,hostName);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		
	}
	
	@Test(expected=None.class)
	public void testExecute(){
		InetSocketTransportAddress [] serverAddresses = new InetSocketTransportAddress[3];
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
	    ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
	    BulkRequestBuilder bulkRequestBuilder = Mockito.mock(BulkRequestBuilder.class);
	    Client client = Mockito.mock(Client.class);
		
		String[] hostNames ={HostNameUtil.getHostName()};
		String[] hostName ={HostNameUtil.getHostName()};
        String clusterName ="clusterName";
        Integer retry = 2;
        ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
		ElasticSearchTransportClient elasticSearchTransportClient = new ElasticSearchTransportClient(hostNames,clusterName,retry,serializer1);
		
		Whitebox.setInternalState(elasticSearchTransportClient, "serverAddresses", serverAddresses);
		Whitebox.setInternalState(elasticSearchTransportClient, "serializer", serializer);
		Whitebox.setInternalState(elasticSearchTransportClient, "indexRequestBuilderFactory", indexRequestBuilderFactory);
		Whitebox.setInternalState(elasticSearchTransportClient, "bulkRequestBuilder", bulkRequestBuilder);
		Whitebox.setInternalState(elasticSearchTransportClient, "client", client);
		try {
			elasticSearchTransportClient.execute();
		} catch (Exception e) {
		}
		
	}
}