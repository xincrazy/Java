package com.oneapm.log.agent.flume.parser.druid.v073;

import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestDruidBatchDataSegmentAnnouncerMetricsParser {
	
	private static DruidBatchDataSegmentAnnouncerMetricsParser parser;
	DruidBatchDataSegmentAnnouncerMetricsParser druidBatchDataSegmentAnnouncerMetricsParser 
	= new DruidBatchDataSegmentAnnouncerMetricsParser();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Test
	public void testGetParser() {
		parser = new DruidBatchDataSegmentAnnouncerMetricsParser();
		Assert.assertNotNull(parser);
	}

	@Test
	public void testGetMap() {
		String content = "hello i am here welcome 2016_2016-05-09T00:00:00.007Z4 Unannouncing segment "
				+ "thrown Away Unannouncing segmentat path"
				+ " processed at path hello Unannouncing segmenthello";
		LinkedHashMap<String, String> map = druidBatchDataSegmentAnnouncerMetricsParser.getMap(content);
		Assert.assertNotNull(map);
		
	}

	@Test
	public void testGetLogTimeRaw() {
		
		String content = "2016-05-11 09:14:39,2016";
		String time = druidBatchDataSegmentAnnouncerMetricsParser.getLogTimeRaw(content);
		Assert.assertEquals("GetLogTimeRaw error!",time,"2016-05-11 09:14:39");
		
	}

	@Test
	public void testGetLogTime() {
		String content = "2016-05-11 09:14:39,2016,09,12";
		String result = druidBatchDataSegmentAnnouncerMetricsParser.getLogTime(content);
		Assert.assertEquals("get log time error!", result,"1462930756016");
	}

	@Test
	public void testGetLogTimeUnixTimeStamp() {
		String content = "2016-05-11 09:14:39,";
		String result = druidBatchDataSegmentAnnouncerMetricsParser.getLogTimeUnixTimeStamp(content);
		Assert.assertEquals("get log time error!",result,"1462930754000"); 	
	}

	@Test
	public void testGetLogArray() {
		String content = "2016-05-11 09:14:39";
		String getArray [] =druidBatchDataSegmentAnnouncerMetricsParser.getLogArray(content); 
		Assert.assertEquals("get log array error!", getArray [0],"2016-05-11");
		Assert.assertEquals("get log array error!", getArray [1],"09:14:39");
	}

	@Test
	public void testGetLogArrayByEvent() {
		String content = "hello i am here welcome 2016_2016-05-09T00:00:00.007Z4 Unannouncing segment "
				+ "thrown Away Unannouncing segmentat path"
				+ " processed at path hello Unannouncing segmenthello";
		String[] result = druidBatchDataSegmentAnnouncerMetricsParser.getLogArrayByEvent(content);
		Assert.assertEquals("get log array by event error!",result[5],"2016_2016-05-09T00:00:00.007Z4");	
	}

	@Test
	public void testGetLogMillsNum() {
		String [] logArray = {"2016,09,12","2016,09,12"};
		String result = druidBatchDataSegmentAnnouncerMetricsParser.getLogMillsNum(logArray);
		Assert.assertEquals("get log malls num error!",result,"09");
	}

	@Test
	public void testGetLogLevel() {
		String [] logArray = {"2016","07","29","17","07","00"};
		String level = druidBatchDataSegmentAnnouncerMetricsParser.getLogLevel(logArray);
		Assert.assertEquals("get log level error!", level,"29");		
	}

	@Test
	public void testGetLogType() {
		String [] logArray = {"2016","07","29","17","07","00"};
		String type = druidBatchDataSegmentAnnouncerMetricsParser.getLogType(logArray);
		Assert.assertEquals("get log type error!", type,"17");
	}

	@Test
	public void testGetLogSource() {
		String [] logArray = {"2016","07","29","17","0708025","00"};
		String result = druidBatchDataSegmentAnnouncerMetricsParser.getLogSource(logArray);
		Assert.assertEquals("get log Source error", result,"70802");
	}

	@Test
	public void testGetDateSource() {
		String [] logArray = {"2016","07","29","17","07","2016_2016-05-09T00:00:00.007Z4"};
		String result = druidBatchDataSegmentAnnouncerMetricsParser.getDateSource(logArray);
		Assert.assertEquals("016", result);
	}

	@Test
	public void testGetMetric() {
		String metric = druidBatchDataSegmentAnnouncerMetricsParser.getMetric();
		Assert.assertEquals("get metric error!", metric,"Unannouncing segment");
	}

	@Test
	public void testGetValue() {
		String value = druidBatchDataSegmentAnnouncerMetricsParser.getValue();
		Assert.assertEquals("get value error!", value,"1");
	}

	@Test
	public void testGetTimeStamp() {
		String [] logArray = {"getLogArrayByEvent","07","29","17","07","2016_2016-05-09T00:00:00.007Z4"};
		String result = druidBatchDataSegmentAnnouncerMetricsParser.getTimeStamp(logArray);
		Assert.assertNotNull(result);	
	}
	@Test
	public void testParser() {
		String content = "hello i am here welcome 2016_2016-05-09T00:00:00.007Z4 Unannouncing segment "
				+ "thrown Away Unannouncing segmentat path"
				+ " processed at path hello Unannouncing segmenthello";
		String result = druidBatchDataSegmentAnnouncerMetricsParser.parser(content);
		Assert.assertNotNull(result);
	}
}
