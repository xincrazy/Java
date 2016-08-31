package com.oneapm.log.agent.flume.source.jmx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Test.None;


public class TestCassandraJMXSerializer{
	
	CassandraJMXSerializer cassandraJMXSerializer = new CassandraJMXSerializer();
	
	@Test(expected=None.class)
	public void testBuilder(){
		cassandraJMXSerializer.build();
	}
	
	
	@Test
	public void testFormateMetrics() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Map<String, Object> metrics = new HashMap<>();
		metrics.put("key", "key");
        Context ctx = new Context();
		Method targetMethod = CassandraJMXSerializer.class.getDeclaredMethod("formateMetrics", Map.class,Context.class);
		targetMethod.setAccessible(true);
		List<String> result = (List<String>) targetMethod.invoke(cassandraJMXSerializer,metrics,ctx );
		Assert.assertNotNull(result);
	}
}