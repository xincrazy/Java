package com.oneapm.log.agent.flume.sink.elasticsearch.v211.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.BytesStream;
import org.junit.Test;
import org.junit.Test.None;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.oneapm.log.agent.flume.sink.elasticsearch.v211.ElasticSearchEventSerializer;
import com.oneapm.log.agent.flume.sink.elasticsearch.v211.IndexNameBuilder;
import com.oneapm.log.common.utils.HostNameUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestElasticSearchRestClient{
	
	@Test(expected=None.class)
	public void testNoneClass(){
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
	    RoundRobinList<String> serversList = Mockito.mock(RoundRobinList.class);
	    StringBuilder  bulkBuilder = new StringBuilder() ;
	    HttpClient  httpClient = Mockito.mock(HttpClient.class);
		
		String[] hostNames = {HostNameUtil.getHostName()};
        ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
        ElasticSearchRestClient elasticSearchRestClient = new ElasticSearchRestClient(hostNames, serializer1);
        
        Whitebox.setInternalState(elasticSearchRestClient, "serializer", serializer);
        Whitebox.setInternalState(elasticSearchRestClient, "serversList", serversList);
        Whitebox.setInternalState(elasticSearchRestClient, "bulkBuilder", bulkBuilder);
        Whitebox.setInternalState(elasticSearchRestClient, "httpClient", httpClient);
        
        Context context = new Context();
        elasticSearchRestClient.configure(context);
        elasticSearchRestClient.close();
	}
	
	@Test
	public void testElasticSearchRestClient(){
		String [] hostNames ={HostNameUtil.getHostName()};
		 ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);
		 HttpClient client = Mockito.mock(HttpClient.class);
		 ElasticSearchRestClient elasticSearchRestClient = new ElasticSearchRestClient(hostNames, serializer);
		 ElasticSearchRestClient elasticSearchRestClient1 = new ElasticSearchRestClient(hostNames, serializer,client);
	}
	
	@Test
	public <T> void testExecute() throws ClientProtocolException,IOException{
	    RoundRobinList serversList = mock(RoundRobinList.class);
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);	    
	    StringBuilder  bulkBuilder = new StringBuilder("hello") ;
	    HttpClient  httpClient = Mockito.mock(HttpClient.class);
	    
	    String[] hostNames = {HostNameUtil.getHostName()};
        ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
        ElasticSearchRestClient elasticSearchRestClient = new ElasticSearchRestClient(hostNames, serializer1);

	    Whitebox.setInternalState(elasticSearchRestClient, "serializer", serializer);
        Whitebox.setInternalState(elasticSearchRestClient, "serversList", serversList);
        Whitebox.setInternalState(elasticSearchRestClient, "bulkBuilder", bulkBuilder);
        Whitebox.setInternalState(elasticSearchRestClient, "httpClient", httpClient);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpPost httpRequest = mock(HttpPost.class);
        int statusCode = 2;
		when(httpClient.execute(httpRequest)).thenReturn(response);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getStatusLine().getStatusCode()).thenReturn(statusCode);
        try {
			elasticSearchRestClient.execute();
		} catch (Exception e) {
		}
	}
	
	@Test
	public <T> void test() throws IOException{
		ElasticSearchEventSerializer serializer = Mockito.mock(ElasticSearchEventSerializer.class);	    
	    StringBuilder  bulkBuilder = new StringBuilder("hello") ;
	    HttpClient  httpClient = Mockito.mock(HttpClient.class);
	    RoundRobinList serversList = mock(RoundRobinList.class);
	    String[] hostNames = {HostNameUtil.getHostName()};
        ElasticSearchEventSerializer serializer1 = Mockito.mock(ElasticSearchEventSerializer.class);
        ElasticSearchRestClient elasticSearchRestClient = new ElasticSearchRestClient(hostNames, serializer1);

	    Whitebox.setInternalState(elasticSearchRestClient, "serializer", serializer);
        Whitebox.setInternalState(elasticSearchRestClient, "serversList", serversList);
        Whitebox.setInternalState(elasticSearchRestClient, "bulkBuilder", bulkBuilder);
        Whitebox.setInternalState(elasticSearchRestClient, "httpClient", httpClient);
        Map<String,String> headers = new  HashMap<String,String>();
        headers.put("key", "value");
        byte [] body = new byte[]{'1','1','1'};
        Event event = EventBuilder.withBody(body, headers);
		BytesReference content = mock(BytesReference.class);
		BytesStream by = mock(BytesStream.class);
		when(serializer.getContentBuilder(event)).thenReturn(by);		
		when(serializer.getContentBuilder(event).bytes()).thenReturn(content);
		IndexNameBuilder indexNameBuilder = Mockito.mock(IndexNameBuilder.class);
		BytesArray bytearray = mock(BytesArray.class);
		when(content.toBytesArray()).thenReturn(bytearray);
		when(content.toBytesArray().toUtf8()).thenReturn("hello");
        String indexType = "indexType";
        long ttlMs = 1;
        long ttlMs1 = -1;
        try {
			elasticSearchRestClient.addEvent(event, indexNameBuilder, indexType, ttlMs);
			elasticSearchRestClient.addEvent(event, indexNameBuilder, indexType, ttlMs1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}