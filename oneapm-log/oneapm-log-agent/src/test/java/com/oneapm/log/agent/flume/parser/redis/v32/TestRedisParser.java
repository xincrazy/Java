package com.oneapm.log.agent.flume.parser.redis.v32;

import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRedisParser {
	
	private static RedisParser parser;
	RedisParser redisParser = new RedisParser();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetParser() {
		parser = RedisParser .getParser();
		Assert.assertNotNull(parser);
	}

	@Test
	public void testGetRawRedisData() {
		String content = "redis1,redis2,redis3,redis4,";
		String content1 = "redis1,redis2,redis3,redis4";
		String result = redisParser .getRawRedisData(content);
		String result1 = redisParser .getRawRedisData(content1);
		Assert.assertEquals("test getRawRedisData error!",result,"redis1,redis2,redis3,redis4,");
		Assert.assertEquals("test getRawRedisData error!",result1,"");
	}

	@Test
	public void testGetUsedMemory() {
		String string = "used_memory:2,";
		String string1 = "used_memory:2";
		String usedMemory = redisParser.getUsedMemory(string);
		String usedMemory1 = redisParser.getUsedMemory(string1);
		Assert.assertEquals("test getUsedMemory error!",usedMemory,"2" );
		Assert.assertEquals("test getUsedMemory error!",usedMemory1,"" );
	}

	@Test
	public void testGetUsedMemoryUsage() {
		String string = "used_memory:2,total_system_memory:4,";
		String result =redisParser.getUsedMemoryUsage(string);
		Assert.assertEquals("test getUsedMemoryUsage error!", result,"0.50 ");
	}

	@Test
	public void testGetHits() {
		String string = "keyspace_hits:6";
		String string1 = "keyspace_hits:6,";
		String result =redisParser.getHits(string);
		String result1 =redisParser.getHits(string1);
		Assert.assertEquals("test getHits error!",result,"");
		Assert.assertEquals("test getHits error!",result1,"6");
	}

	@Test
	public void testGetHitRate() {
		String string = "keyspace_hits:6,keyspace_misses:6,";
		String result = redisParser.getHitRate(string);
		Assert.assertEquals("test getUsedMemoryUsage error!", result,"0.50 ");		
	}

	@Test
	public void testGetKeysSum() {
		String string = "keys=3,";
		String result = redisParser.getKeysSum(string);
		String string1 = "keys=3";
		String result1 = redisParser.getKeysSum(string1);
		Assert.assertEquals("test getkeySum error!",result1,"");
		Assert.assertEquals("test getHits error!",result,"3");
		
	}

	@Test
	public void testGetRedisMap() {
		String content = "used_memory:2,total_system_memory:4,keyspace_hits:6,keyspace_misses:6,keys=3,";
		LinkedHashMap<String, String> map = redisParser.getRedisMap(content); 
		map.get("used_Memory");
		Assert.assertEquals("3",map.get("keys"));
		Assert.assertEquals("2",map.get("usedMemory"));
		Assert.assertEquals("0.50 ",map.get("usedMemoryUsage"));
		Assert.assertEquals("6",map.get("hits"));
		Assert.assertEquals("0.50 ",map.get("hitRate"));
	}

	@Test
	public void testParser() {
		String content = "used_memory:2,total_system_memory:4,keyspace_hits:6,keyspace_misses:6,keys=3,";
		String parser = redisParser.parser(content);
		String result = "{\"keys\":\"3\",\"usedMemory\":\"2\",\"usedMemoryUsage\":\"0.50 \",\"hits\":\"6\",\"hitRate\":\"0.50 \"}";
		Assert.assertEquals("test parser error!",parser,result);
	}

}
