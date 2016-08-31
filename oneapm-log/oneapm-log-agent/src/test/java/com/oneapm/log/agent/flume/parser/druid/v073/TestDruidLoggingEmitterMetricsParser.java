package com.oneapm.log.agent.flume.parser.druid.v073;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDruidLoggingEmitterMetricsParser {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetParser() throws ClassNotFoundException, NoSuchMethodException,
	SecurityException, InstantiationException, IllegalAccessException, 
	IllegalArgumentException, InvocationTargetException {
		String druidNode ="druidNode";
		String tolerableRequestTime = "1.2";
		Class c = Class.forName("com.oneapm.log.agent.flume.parser.druid.v073.DruidLoggingEmitterMetricsParser");
		Constructor c1=c.getDeclaredConstructor(new Class[]{String.class,String.class});
		c1.setAccessible(true);
		DruidLoggingEmitterMetricsParser druidLoggingEmitterMetricsParser
		= (DruidLoggingEmitterMetricsParser)c1.newInstance(new Object[]{druidNode,tolerableRequestTime});	
		DruidLoggingEmitterMetricsParser dru = druidLoggingEmitterMetricsParser.getParser(druidNode, tolerableRequestTime);
		Assert.assertNotNull(dru);
	}
	
	@Test
	public void testParser() throws ClassNotFoundException, NoSuchMethodException,
	SecurityException, InstantiationException, IllegalAccessException, 
	IllegalArgumentException, InvocationTargetException{
		String druidNode ="druidNode";
		String tolerableRequestTime = "1.2";
		Class c = Class.forName("com.oneapm.log.agent.flume.parser.druid.v073.DruidLoggingEmitterMetricsParser");
		Constructor c1=c.getDeclaredConstructor(new Class[]{String.class,String.class});
		c1.setAccessible(true);
		DruidLoggingEmitterMetricsParser druidLoggingEmitterMetricsParser
		= (DruidLoggingEmitterMetricsParser)c1.newInstance(new Object[]{druidNode,tolerableRequestTime});	
		String str = "{\"timestamp\":,\"hostname\":\"\","
				+ "\"servicename\":\"druid\",\"servicetype\":\"\",\"metricname\":\"Unannouncing segment\",\"metricdata\":}";
		String result = druidLoggingEmitterMetricsParser.parser(str);
		Assert.assertNotNull(result);
	}
}
