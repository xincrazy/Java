package com.oneapm.log.agent.flume.source.logfile;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.channel.ChannelProcessor;
import org.junit.Test;
import org.junit.Test.None;
import org.powermock.reflect.Whitebox;

public class TestLogFileSource{
	
	LogFileSource logFileSource = new LogFileSource();

	@Test(expected=None.class)
	public void testConfigure() throws InterruptedException{
		Whitebox.setInternalState(logFileSource, "channelProcessor", new ChannelProcessor(null));
		String logFilePath = "logFilePath";
		Whitebox.setInternalState(logFileSource, "logFilePath", logFilePath);
		int bufferCount = 4;
		Whitebox.setInternalState(logFileSource, "bufferCount", bufferCount);
		long batchTimeout = 2;
		Whitebox.setInternalState(logFileSource, "batchTimeout", batchTimeout);
		Charset charset = Charset.forName("UTF-8");
		Whitebox.setInternalState(logFileSource, "charset", charset);
		Context context = new Context();
		logFileSource.configure(context);
	}
	
	@Test(expected=None.class)
	public void testStart() throws EventDeliveryException,
    SecurityException, NoSuchFieldException, IllegalArgumentException,
    IllegalAccessException, InterruptedException{
		Whitebox.setInternalState(logFileSource, "channelProcessor", new ChannelProcessor(null));
		String logFilePath = "logFilePath";
		Whitebox.setInternalState(logFileSource, "logFilePath", logFilePath);
		int bufferCount = 4;
		Whitebox.setInternalState(logFileSource, "bufferCount", bufferCount);
		long batchTimeout = 2;
		Whitebox.setInternalState(logFileSource, "batchTimeout", batchTimeout);
		Charset charset = Charset.forName("UTF-8");
		Whitebox.setInternalState(logFileSource, "charset", charset);
		logFileSource.start();				
	}
	
	@Test(expected=None.class)
	public void testStop() throws InstantiationException, IllegalAccessException,ClassNotFoundException,
	NoSuchMethodException,SecurityException,IllegalArgumentException,InvocationTargetException{
		Object runner = null;
		String logFilePath = "logFilePath";
		Future<?> runnerFuture = mock(Future.class);
		Whitebox.setInternalState(logFileSource, "runner", runner);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Whitebox.setInternalState(logFileSource, "executor", executor);
		Whitebox.setInternalState(logFileSource, "logFilePath", logFilePath);
		Whitebox.setInternalState(logFileSource, "runnerFuture", runnerFuture);
		logFileSource.stop();
	}
	
	@Test(expected=None.class)
	public void testStop2()throws InstantiationException, IllegalAccessException,ClassNotFoundException,
	NoSuchMethodException,SecurityException,IllegalArgumentException,InvocationTargetException{
		Object runner = null;
		String logFilePath = "logFilePath";
		int bufferCount = 4;
		long batchTimeout = 2;
		Charset charset = Charset.forName("UTF-8");
		ChannelProcessor channelProcessor= new ChannelProcessor(null);
		Class<?> cls = Class.forName("com.oneapm.log.agent.flume.source.logfile.LogFileSource$LogFileRunnable");
		Constructor constructor = cls.getDeclaredConstructor(new Class[] { String.class,ChannelProcessor.class, int.class,
		long.class, Charset.class}); 
		constructor .setAccessible(true);
		runner = constructor.newInstance(logFilePath,channelProcessor,bufferCount,batchTimeout,charset);
		Whitebox.setInternalState(logFileSource, "runner", runner);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Whitebox.setInternalState(logFileSource, "executor", executor);
		Whitebox.setInternalState(logFileSource, "logFilePath", logFilePath);
		ScheduledExecutorService timedFlushService = mock(ScheduledExecutorService.class);
		logFileSource.stop();
	}
}