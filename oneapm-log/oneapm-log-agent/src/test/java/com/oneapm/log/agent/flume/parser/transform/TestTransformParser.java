package com.oneapm.log.agent.flume.parser.transform;

import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestTransformParser {

	public static TransformParser parser;
	TransformParser transformParser = new TransformParser ();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetParser() {
		parser = TransformParser.getParser();
		Assert.assertNotNull(parser);
	}

	@Test
	public void testGetTransformMap() {
		String string = "{\"timestamp\":\"1465368478064\",\"key\":\"system\","
		        + "\"eventCategory\":\"RawMetricEvent\",\"metrics\":{\"iZ25979ub3rZ#&maxDiskUsage\":\"0.45\"},\"ttl\":\"2400\"}";
		 LinkedHashMap<String, String> result = transformParser.getTransformMap(string);
		 Assert.assertNotNull(result);
	}

	@Test
	public void testParser() {
		String string =
		        "{\"timestamp\":\"1465368478064\",\"key\":\"system\",\"eventCategory\":"
		        + "\"RawMetricEvent\",\"metrics\":{\"iZ25979ub3rZ#&maxDiskUsage\":\"0.45\"},\"ttl\":\"2400\"}";
		 String str = "{\"timestamp\":1465368478064,\"hostname\":\"iZ25979ub3rZ\",\"maxDiskUsage\":0.45}";
		 String result = transformParser.parser(string);
		 String result1 = transformParser.parser(str);
		 Assert.assertNotNull(result);
		 Assert.assertNotNull(result1);
	}
	
	@Test 
	public void testParser1(){
		String string = "{\"timestamp\":\"1465368478064\",\"key\":\"system\",\"eventCategory\":"
				+ "\"RawMetricEvent\",\"metrics\":{\"iZ25979ub3rZ#&maxDiskUsage\":\"0.45\"},\"ttl\":\"2400\"}";
		String str = "{\"timestamp\":1465368478064,\"hostname\":\"iZ25979ub3rZ\",\"maxDiskUsage\":0.45}";
		String result = transformParser.parser1(string);
		String result1 = transformParser.parser1(str);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result1);	
	}

}
