package com.oneapm.log.agent.flume.source.logfile;

import org.junit.Assert;
import org.junit.Test;


public class TestLogFileConfigurationConstants{
	LogFileConfigurationConstants logFileConfigurationConstants = new LogFileConfigurationConstants();
	
	@Test
	public void testContains(){
		Assert.assertEquals(3000l, logFileConfigurationConstants.DEFAULT_BATCH_TIME_OUT);
		Assert.assertEquals("charset", logFileConfigurationConstants.CHARSET);
		Assert.assertEquals("batchSize", logFileConfigurationConstants.CONFIG_BATCH_SIZE);
		Assert.assertEquals("batchTimeout", logFileConfigurationConstants.CONFIG_BATCH_TIME_OUT);
		Assert.assertEquals("grokPattern", logFileConfigurationConstants.CONFIG_GROK_PATTERN);
		Assert.assertEquals("logFilePath", logFileConfigurationConstants.CONFIG_LOG_FILE_PATH);
		Assert.assertEquals("logType", logFileConfigurationConstants.CONFIG_LOG_TYPE);
		Assert.assertEquals(20, logFileConfigurationConstants.DEFAULT_BATCH_SIZE);
		Assert.assertEquals("UTF-8", logFileConfigurationConstants.DEFAULT_CHARSET);
	}
}