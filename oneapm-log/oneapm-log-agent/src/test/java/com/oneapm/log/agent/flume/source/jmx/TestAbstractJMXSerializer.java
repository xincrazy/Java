package com.oneapm.log.agent.flume.source.jmx;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.flume.Context;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.oneapm.log.agent.flume.source.jmx.AbstractJMXSerializer.ObjectElement;


public class TestAbstractJMXSerializer{
	
	AbstractJMXSerializer abstractJMXSerializer;
	@Before
	public void setUp() throws Exception {
		abstractJMXSerializer = Mockito.mock(AbstractJMXSerializer.class, Mockito.CALLS_REAL_METHODS);
 }
	
	@Test(expected=None.class)
	public void testObjectElement(){
		ObjectName name = mock(ObjectName.class);
		ObjectName name1 = mock(ObjectName.class);
        String key = "key";
        String alias = "alias";
		AbstractJMXSerializer.ObjectElement obj =  new AbstractJMXSerializer.ObjectElement(name1, "key", "alias");
		Whitebox.setInternalState(obj, "name", name);
		Whitebox.setInternalState(obj, "key", key);
		Whitebox.setInternalState(obj, "alias", alias);	
	}
	
	@Test
	public void testPullMetrics()throws MalformedObjectNameException{
		ObjectName name = mock(ObjectName.class);
		ObjectName name1 = mock(ObjectName.class);
        String key = "key";
        String alias = "alias";
		AbstractJMXSerializer.ObjectElement obj =  new AbstractJMXSerializer.ObjectElement(name1, "key", "alias");
		Whitebox.setInternalState(obj, "name", name);
		Whitebox.setInternalState(obj, "key", key);
		Whitebox.setInternalState(obj, "alias", alias);	
		List<ObjectElement> elements = new ArrayList<AbstractJMXSerializer.ObjectElement>();
		Whitebox.setInternalState(abstractJMXSerializer, "elements", elements);
		MBeanServerConnection conn = mock(MBeanServerConnection.class);
        Context ctx = mock(Context.class);		
        try {
        	  Set<ObjectName> names = mock(Set.class);
        	  
        	  when(conn.queryNames(ObjectName.WILDCARD,
                      null)).thenReturn(names);
        	  ObjectElement oe = mock(ObjectElement.class);
        	  when(names.contains(oe.getName())).thenReturn(true);
        	  when (oe.getName()).thenReturn(name1);
        	List<String> result = abstractJMXSerializer.pullMetrics(conn, ctx);
        	Assert.assertNotNull(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected=None.class)
	public void testParseAttribute() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		ObjectName name = mock(ObjectName.class);
		ObjectElement oe = new ObjectElement(name, "path", "alias");
        String path = "path";
        Map<String, Object> metrics = mock(Map.class);
        Object o = mock(Object.class);
		Method targetMethod = AbstractJMXSerializer.class.getDeclaredMethod("parseAttribute", ObjectElement.class,String.class,Map.class,Object.class);
		targetMethod.setAccessible(true);
		targetMethod.invoke(abstractJMXSerializer,oe,path,metrics,o );	
	}
	
	@Test(expected=None.class)
	public void testParseAttribute1() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		ObjectName name = mock(ObjectName.class);
		ObjectElement oe = new ObjectElement(name, "path", "alias");
        String path = "pa";
        Map<String, Object> metrics = mock(Map.class);
        Object o = mock(Object.class);
        Object o1 = null;
		Method targetMethod = AbstractJMXSerializer.class.getDeclaredMethod("parseAttribute", ObjectElement.class,String.class,Map.class,Object.class);
		targetMethod.setAccessible(true);
		targetMethod.invoke(abstractJMXSerializer,oe,path,metrics,o );
		targetMethod.invoke(abstractJMXSerializer,oe,path,metrics,o1 );
	}
	
	@Test(expected=None.class)
	public void testParseAttribute2() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		ObjectName name = mock(ObjectName.class);
		ObjectElement oe = new ObjectElement(name, "path", "alias");
        String path = "hello";
        Map<String, Object> metrics = mock(Map.class);
        Object o = mock(Object.class);
		Method targetMethod = AbstractJMXSerializer.class.getDeclaredMethod("parseAttribute", ObjectElement.class,String.class,Map.class,Object.class);
		targetMethod.setAccessible(true);
		targetMethod.invoke(abstractJMXSerializer,oe,path,metrics,o );
	}
	
	@Test(expected=None.class)
	public void testParseAttribute3() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		ObjectName name = mock(ObjectName.class);
		ObjectElement oe = new ObjectElement(name, "path", "alias");
        String path = "hello,i am a test";
        Map<String, Object> metrics = mock(Map.class);
        Object o = mock(Object.class);
		Method targetMethod = AbstractJMXSerializer.class.getDeclaredMethod("parseAttribute", ObjectElement.class,String.class,Map.class,Object.class);
		targetMethod.setAccessible(true);
		targetMethod.invoke(abstractJMXSerializer,oe,path,metrics,o );
	}
	
	
	
	
}