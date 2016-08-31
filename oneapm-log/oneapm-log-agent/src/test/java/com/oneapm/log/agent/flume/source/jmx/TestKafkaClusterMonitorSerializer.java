package com.oneapm.log.agent.flume.source.jmx;

import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Test.None;


public class TestKafkaClusterMonitorSerializer{
	
	KafkaClusterMonitorSerializer kafkaClusterMonitorSerializer = new KafkaClusterMonitorSerializer ();
	
	@Test(expected=None.class)
	public void testBuild(){
		kafkaClusterMonitorSerializer.build();
	}
	
	@Test
	public void testFormateMetrics() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Map<String, Object> metrics = new HashMap<>();
		  metrics.put("timestamp", System.currentTimeMillis());
          metrics.put("hostname","www");
          metrics.put("servicename","KafkaClusterMonitor");
          metrics.put("servicetype","kafkaClusterMonitor");
          metrics.put("metricname","eee");
          metrics.put("metricdata",1);
		Context ctx = mock(Context.class);
		Method targetMethod = KafkaClusterMonitorSerializer.class.getDeclaredMethod("formateMetrics", Map.class,Context.class);
		targetMethod.setAccessible(true);
		List<String> result = (List<String>) targetMethod.invoke(kafkaClusterMonitorSerializer,metrics,ctx );
		Assert.assertNotNull(result);
	}
	
}