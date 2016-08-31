package com.oneapm.log.agent.flume.source.elasticsearch.v211;

import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.channel.ChannelProcessor;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class ElasticsearchSourceTest{

	@Test
	public void testProcess() throws EventDeliveryException{
		Context context = new Context();
        Map<String, String> params = new HashMap<String, String>();
        params.put("cluster.name","test_1");
        params.put("host.name","127.0.0.1");
        params.put("port","9300");
        params.put("batch.size","100");
        params.put("duration", "600000");
        params.put("buffer.size","10000");
    	ElasticsearchSource elasticsearchSource = new ElasticsearchSource();
    	Whitebox.setInternalState(elasticsearchSource, "context", context);
		elasticsearchSource.process();
		elasticsearchSource.configure(context);
	}
	
	@Test
	public void testStop(){
		ElasticsearchSource elasticsearchSource = new ElasticsearchSource();
		elasticsearchSource.stop();
	}
	
	@Test
	public void testStart(){
		ElasticsearchSource elasticsearchSource = new ElasticsearchSource();
		Whitebox.setInternalState(elasticsearchSource, "channelProcessor", new ChannelProcessor(null));
		elasticsearchSource.start();

	}
}