package com.oneapm.log.agent.flume.sink.elasticsearch.v211;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.event.EventBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Test.None;
import org.mockito.internal.util.reflection.Whitebox;




public class TestTimeBasedIndexNameBuilder{
	
	TimeBasedIndexNameBuilder TimeBasedIndexNameBuilder = new TimeBasedIndexNameBuilder();
	
	@Test
	public void testGetFastDateFormat(){
		FastDateFormat result = TimeBasedIndexNameBuilder.getFastDateFormat();
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testGetIndexName(){
		String indexPrefix = "indexPrefix";
		Whitebox.setInternalState( TimeBasedIndexNameBuilder, "indexPrefix", indexPrefix );
		Map<String,String> map = new HashMap<String,String>();
		map.put("timestamp","1466992580000");
		map.put("source", "src_path");
		map.put("type", "type");
		map.put("host", "10.128.6.169");
		map.put("src_path", "src_path");
		map.put("@timestamp","");
		map.put("@source","");
		map.put("@type", "");
		map.put("@host", "");
		map.put("@src_path","");
		byte data[]=new byte[]{'-','-','-','1'};
		Event event = EventBuilder.withBody(data, map);
		String result = TimeBasedIndexNameBuilder.getIndexName(event);
		Assert.assertNotNull(result);
		Assert.assertEquals("indexPrefix-2016-06-27", result);
	}
	
	@Test
	public void testConfigure(){
		Map<String,String> map = new HashMap<String,String>();
		Context context = new Context(map);
		TimeBasedIndexNameBuilder.configure(context);	
	}
	
	@Test(expected=None.class)
	public void tesConfiguret1(){
		ComponentConfiguration conf = null;
		TimeBasedIndexNameBuilder.configure(conf);
	}
	
	@Test
	public void testGetDocument(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("@timestamp","1466992580000");
		map.put("@source", "src_path");
		map.put("@type", "type");
		map.put("@host", "10.128.6.169");
		map.put("@src_path", "src_path");
		map.put("timestamp","");
		map.put("source","");
		map.put("type", "");
		map.put("host", "");
		map.put("src_path","");
		byte data[]=new byte[]{'-','-','-','1'};
		Event event= EventBuilder.withBody(data, map);
		Event result = TimeBasedIndexNameBuilder.getDocument(event);
		Assert.assertNull(result);
	}
	
	@Test 
	public void testCetIndexType(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("@timestamp","1466992580000");
		map.put("@source", "src_path");
		map.put("@type", "type");
		map.put("@host", "10.128.6.169");
		map.put("@src_path", "src_path");
		map.put("timestamp","");
		map.put("source","");
		map.put("type", "");
		map.put("host", "");
		map.put("src_path","");
		byte data[]=new byte[]{'-','-','-','1'};
		Event event= EventBuilder.withBody(data, map);
		String result = TimeBasedIndexNameBuilder.getIndexType(event);
		Assert.assertNull(result);
	}
	
	@Test
	public void testGetIndexPrefix(){
		String indexPrefix = "indexPrefix";
		Whitebox.setInternalState( TimeBasedIndexNameBuilder, "indexPrefix", indexPrefix );
		Map<String,String> map = new HashMap<String,String>();
		map.put("@timestamp","1466992580000");
		map.put("@source", "src_path");
		map.put("@type", "type");
		map.put("@host", "10.128.6.169");
		map.put("@src_path", "src_path");
		map.put("timestamp","");
		map.put("source","");
		map.put("type", "");
		map.put("host", "");
		map.put("src_path","");
		byte data[]=new byte[]{'-','-','-','1'};
		Event event= EventBuilder.withBody(data, map);
		String result = TimeBasedIndexNameBuilder.getIndexPrefix(event);
		Assert.assertEquals(indexPrefix, result);
	}
}