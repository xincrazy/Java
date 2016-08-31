package com.oneapm.log.agent.flume.sink.elasticsearch.v211;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestContentBuilderUtil{
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test
	public void testAppendField() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class c = Class.forName("com.oneapm.log.agent.flume.sink.elasticsearch.v211.ContentBuilderUtil");
		Constructor c0=c.getDeclaredConstructor();
		c0.setAccessible(true);
		ContentBuilderUtil contentBuilderUtil = (ContentBuilderUtil)c0.newInstance();
		String field = "inner";
		byte data[]=new byte[]{'{',1,1,1,1,1};
		byte data1[] = new byte[]{};
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
			contentBuilderUtil.appendField(builder, field, data);
			contentBuilderUtil.appendField(builder, field, data1);
			Assert.assertNotNull(builder);
			Assert.assertNotNull(data);
			Assert.assertNotNull(field);
			Assert.assertNotNull(data1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testAddSimpleField() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class c = Class.forName("com.oneapm.log.agent.flume.sink.elasticsearch.v211.ContentBuilderUtil");
		Constructor c0=c.getDeclaredConstructor();
		c0.setAccessible(true);
		String fieldName = "inner";
		byte data[]=new byte[]{'{',1,1,1,1,1};  
		ContentBuilderUtil contentBuilderUtil = (ContentBuilderUtil)c0.newInstance();
		XContentBuilder builder;
		try {
			builder = XContentFactory.jsonBuilder().startObject();
			ContentBuilderUtil.addSimpleField(builder, fieldName, data);
			Assert.assertNotNull(builder);
			Assert.assertNotNull(data);
			Assert.assertNotNull(fieldName);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@Test 
	public void testAddComplexField()throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String field = "inner";
		byte data[]=new byte[]{'{',1,1,1,1,1};  
		Class c = Class.forName("com.oneapm.log.agent.flume.sink.elasticsearch.v211.ContentBuilderUtil");
		Constructor c0=c.getDeclaredConstructor();
		c0.setAccessible(true);
		ContentBuilderUtil contentBuilderUtil = (ContentBuilderUtil)c0.newInstance();
		XContentType contentType =XContentFactory.xContentType(data); 
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
			contentBuilderUtil.addComplexField(builder, field, contentType, data);
			Assert.assertNotNull(builder);
			Assert.assertNotNull(data);
			Assert.assertNotNull(field);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}