package com.oneapm.log.agent.flume.source.jmx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Test.None;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestSampleJMXSerializer{
	
	SampleJMXSerializer sampleJMXSerializer = new SampleJMXSerializer();
	
	@Test(expected=None.class)
	public void testBuilder(){
		sampleJMXSerializer.build();
	}
	
	@Test
	public void testFormateMetrics() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method targetMethod = SampleJMXSerializer.class.getDeclaredMethod("formateMetrics", Map.class,Context.class);
		targetMethod.setAccessible(true);
		Map<String, Object> metrics = mock(Map.class);
		Context context = mock(Context.class);
		List<String> result = (List<String>) targetMethod.invoke(sampleJMXSerializer,metrics,context);
		Assert.assertNotNull(result);
	}
	
}