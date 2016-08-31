package com.oneapm.log.agent.flume.parser.eswrapper;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestEsSinkWrapperParser{
	
	String indexName = "indexName";
	String indexType = "indexType";
	EsSinkWrapperParser esSinkWrapperParser = new EsSinkWrapperParser(indexName,indexType);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testEsSinkWrapperParser(){
		indexName = indexName;
		indexType = indexType;
		Assert.assertEquals("indexName", indexName);
		Assert.assertEquals("indexType", indexType);
	}
	
	@Test
	public void testParser(){
		String content = "hello";
		String result = esSinkWrapperParser .parser(content);
		String str ="{\"indexName\":\"indexName\",\"indexType\":\"indexType\",\"document\":hello}";
		Assert.assertEquals(str, result);
	}
	
	@Test
	public void testGetparser(){
		String indexName = "indexName";
		String indexType = "indexType";
		EsSinkWrapperParser parser = esSinkWrapperParser.getParser(indexName, indexType);	
		Assert.assertNotNull(parser);
	}
}