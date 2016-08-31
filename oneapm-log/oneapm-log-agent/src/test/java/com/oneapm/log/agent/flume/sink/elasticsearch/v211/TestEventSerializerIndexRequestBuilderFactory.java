package com.oneapm.log.agent.flume.sink.elasticsearch.v211;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.event.EventBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
public class TestEventSerializerIndexRequestBuilderFactory{
	
	
	@Test
	public void testConfigure(){
		ElasticSearchEventSerializer serializer =  (ElasticSearchEventSerializer) Mockito.mock(ElasticSearchEventSerializer.class); 
		EventSerializerIndexRequestBuilderFactory eventSerializer =new EventSerializerIndexRequestBuilderFactory(serializer);
		Whitebox.setInternalState(eventSerializer, "serializer", serializer);
		Context context = new Context();
		eventSerializer.configure(context);
		ComponentConfiguration config = null;
		eventSerializer.configure(config);
		Assert.assertNotNull(context);
	}
	
	@Test
	public void testPrepareIndexRequest(){
		IndexRequestBuilder indexRequest = Mockito.mock(IndexRequestBuilder.class);
        String indexName = "indexName";
        String indexType ="indexType";
        Map<String,String> map = new HashMap<String,String>();
        Event event = EventBuilder.withBody("hello", Charset.defaultCharset(), map);
		ElasticSearchEventSerializer serializer =  (ElasticSearchEventSerializer) Mockito.mock(ElasticSearchEventSerializer.class); 
		EventSerializerIndexRequestBuilderFactory eventSerializer =new EventSerializerIndexRequestBuilderFactory(serializer);
		Whitebox.setInternalState(eventSerializer, "serializer", serializer);
		Method targetMethod;
		try {
			targetMethod = EventSerializerIndexRequestBuilderFactory.class.getDeclaredMethod("prepareIndexRequest", IndexRequestBuilder.class,String.class,String.class,Event.class);
			targetMethod.setAccessible(true);
			try {
				targetMethod.invoke(eventSerializer,indexRequest,indexName,indexType,event );
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
	}
}