package com.oneapm.log.agent.flume.sink.elasticsearch.v211;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.instrumentation.SinkCounter;
import org.codehaus.jackson.JsonProcessingException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;




public class ElasticSearchSinkTest {
	
	ElasticSearchSink elasticSearchSink = new ElasticSearchSink();
	private List<Event> events;
	
	@Before
	  public void setup() throws Exception {
	  }
	
	@After
	public void testStop(){
		ElasticSearchIndexRequestBuilderFactory indexRequestFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		SinkCounter sinkCounter = Mockito.mock(SinkCounter.class);
		ElasticSearchEventSerializer eventSerializer = Mockito.mock(ElasticSearchEventSerializer.class);
		Whitebox.setInternalState(elasticSearchSink, "eventSerializer", eventSerializer);
		IndexNameBuilder indexNameBuilder = Mockito.mock(IndexNameBuilder.class);
		Whitebox.setInternalState(elasticSearchSink, "indexNameBuilder", indexNameBuilder);
		Whitebox.setInternalState(elasticSearchSink, "sinkCounter", sinkCounter);
		Whitebox.setInternalState(elasticSearchSink, "indexRequestFactory", indexRequestFactory);
		elasticSearchSink.stop();
		
	}
	
	@Test
	public void testGetClusterName(){
		String result = elasticSearchSink.getClusterName();
		Assert.assertEquals("elasticsearch", result);
	}
	
	@Test
	public void testGetServerAddresses(){
		String[] result = elasticSearchSink.getServerAddresses() ;
		Assert.assertNull(result);
	}
	
	@Test
	public void testGetIndexName(){
		String result = elasticSearchSink.getIndexName();
		Assert.assertEquals("flume", result);
	}
	
	@Test
	public void testGetIndexType(){
		String result = elasticSearchSink.getIndexType();
		Assert.assertEquals("log", result);
	}
	
	@Test
	public void testGetTTLMs(){
		long result = elasticSearchSink.getTTLMs();
		Assert.assertEquals(-1, result);
	}
	
	@Test
	public void testGetEventSerializer(){
		ElasticSearchEventSerializer eventSerializer = Mockito.mock(ElasticSearchEventSerializer.class);
		Whitebox.setInternalState(elasticSearchSink, "eventSerializer", eventSerializer);
		ElasticSearchEventSerializer result = elasticSearchSink.getEventSerializer();
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testGetIndexNameBuilder(){
		IndexNameBuilder indexNameBuilder = Mockito.mock(IndexNameBuilder.class);
		Whitebox.setInternalState(elasticSearchSink, "indexNameBuilder", indexNameBuilder);
		IndexNameBuilder result = elasticSearchSink.getIndexNameBuilder();
		Assert.assertNotNull(result);
	}
	
	
	@Test
	public void testconfigure(){
		Map<String,String> map = new HashMap<>();
		String  hostnames = "http://192.168.1.1";
		map.put("hostNames", hostnames);
		Context context = new Context(map);
		ElasticSearchIndexRequestBuilderFactory indexRequestFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		SinkCounter sinkCounter = Mockito.mock(SinkCounter.class);
		ElasticSearchEventSerializer eventSerializer = Mockito.mock(ElasticSearchEventSerializer.class);
		Whitebox.setInternalState(elasticSearchSink, "eventSerializer", eventSerializer);
		IndexNameBuilder indexNameBuilder = Mockito.mock(IndexNameBuilder.class);
		Whitebox.setInternalState(elasticSearchSink, "indexNameBuilder", indexNameBuilder);
		Whitebox.setInternalState(elasticSearchSink, "sinkCounter", sinkCounter);
		Whitebox.setInternalState(elasticSearchSink, "indexRequestFactory", indexRequestFactory);
		elasticSearchSink.configure(context);
	}
	
	@Test
	public void testConfigure1() throws InterruptedException,EventDeliveryException{
		Map<String,String> ctx = new HashMap<>();
		ctx.put(ElasticSearchSinkConstants.HOSTNAMES,
                "127.0.0.1");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME,
                "sink-1");
        ctx.put(ElasticSearchSinkConstants.INDEX_TYPE,
                "logs");
        ctx.put(ElasticSearchSinkConstants.CLUSTER_NAME,
                "test_1");
        ctx.put(ElasticSearchSinkConstants.CLIENT_TYPE,
                "transport");
        ctx.put(ElasticSearchSinkConstants.SERIALIZER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.serializer.ElasticSearchSourceSerializer");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME_BUILDER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.TimeBasedIndexNameBuilder");
		Context context = new Context(ctx);
		ElasticSearchIndexRequestBuilderFactory indexRequestFactory = Mockito.mock(ElasticSearchIndexRequestBuilderFactory.class);
		SinkCounter sinkCounter = Mockito.mock(SinkCounter.class);
		ElasticSearchEventSerializer eventSerializer = Mockito.mock(ElasticSearchEventSerializer.class);
	//	Whitebox.setInternalState(eventSerializer, "channelProcessor", new ChannelProcessor(null));
		Whitebox.setInternalState(elasticSearchSink, "eventSerializer", eventSerializer);
		IndexNameBuilder indexNameBuilder = Mockito.mock(IndexNameBuilder.class);
		Whitebox.setInternalState(elasticSearchSink, "indexNameBuilder", indexNameBuilder);
		Whitebox.setInternalState(elasticSearchSink, "sinkCounter", sinkCounter);
		Whitebox.setInternalState(elasticSearchSink, "indexRequestFactory", indexRequestFactory);
		elasticSearchSink.configure(context);
		try {
			elasticSearchSink.start();
        } catch (Exception e) {
        }		
	}
	
	
	
	
	@Test
	public void testParseTTL() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String ttl = "1d";
		Method targetMethod = ElasticSearchSink.class.getDeclaredMethod("parseTTL", String.class);
		targetMethod.setAccessible(true);
		long result = (long) targetMethod.invoke(elasticSearchSink,ttl );
		Assert.assertEquals(86400000, result);		
		long result1 = (long) targetMethod.invoke(elasticSearchSink,"10m" );
		Assert.assertEquals(600000, result1);
		long result2 = (long) targetMethod.invoke(elasticSearchSink,"10s" );
		Assert.assertEquals(10000, result2);
		long result3 = (long) targetMethod.invoke(elasticSearchSink,"1h" );
		Assert.assertEquals(3600000, result3);
		long result4 = (long) targetMethod.invoke(elasticSearchSink,"1w" );
		Assert.assertEquals(604800000, result4);
		long result5 = (long) targetMethod.invoke(elasticSearchSink,"1ms" );
		Assert.assertEquals(1, result5);
		long result6 = (long) targetMethod.invoke(elasticSearchSink,"" );
		Assert.assertEquals(0, result6);
	}
	
	@Test
	public void testStart() throws JsonProcessingException, Exception{
		Context ctx = new Context();
        ctx.put(ElasticSearchSinkConstants.HOSTNAMES,
                "127.0.0.1");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME,
                "sink-1");
        ctx.put(ElasticSearchSinkConstants.INDEX_TYPE,
                "logs");
        ctx.put(ElasticSearchSinkConstants.CLUSTER_NAME,
                "test_1");
        ctx.put(ElasticSearchSinkConstants.CLIENT_TYPE,
                "transport");
        ctx.put(ElasticSearchSinkConstants.SERIALIZER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.serializer.ElasticSearchSourceSerializer");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME_BUILDER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.TimeBasedIndexNameBuilder");
        ElasticSearchSink sink = new ElasticSearchSink();
        try {
            sink.start();
        } catch (Exception e) {
        }
	}
	
	@Test
	public void testProcess() throws JsonProcessingException, Exception{
		Context ctx = new Context();
        ctx.put(ElasticSearchSinkConstants.HOSTNAMES,
                "127.0.0.1");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME,
                "sink-1");
        ctx.put(ElasticSearchSinkConstants.INDEX_TYPE,
                "logs");
        ctx.put(ElasticSearchSinkConstants.CLUSTER_NAME,
                "test_1");
        ctx.put(ElasticSearchSinkConstants.CLIENT_TYPE,
                "transport");
        ctx.put(ElasticSearchSinkConstants.SERIALIZER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.serializer.ElasticSearchSourceSerializer");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME_BUILDER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.TimeBasedIndexNameBuilder");
        ElasticSearchSink sink = new ElasticSearchSink();
        sink.configure(ctx);
        try {
            sink.process();
        } catch (Exception e) {
        }
	}
}