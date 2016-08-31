package com.oneapm.log.agent.flume.source.logfile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.SystemClock;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.oneapm.log.common.constants.GrokConstants;
import com.oneapm.log.common.constants.LogConstants;
import com.oneapm.log.common.grok.GrokBean;
import com.oneapm.log.common.utils.FileUtil;
import com.oneapm.log.common.utils.HostNameUtil;
import com.oneapm.log.common.utils.JsonUtil;
import com.oneapm.log.common.utils.TimeUtil;

/**
 *
 * [A] Description
 * 
 * A source to read the new line from a specified log file once it changes to
 * the channel.
 * 
 * 
 * [B] Describe/configure the source
a1.sources.src-1.type = com.oneapm.log.agent.flume.source.logfile.LogFileSource
a1.sources.src-1.logFilePath = /home/hadoop/temp/flume/input.log
a1.sources.src-1.batchSize = 20
a1.sources.src-1.batchTimeout = 20000
a1.sources.src-1.charset = UTF-8
a1.sources.src-1.logType = cassandra
a1.sources.src-1.grokPattern = %{CASSANDRALOG:cassandra}
a1.sources.src-1.subType1 = cach
 * 
 * [C] Output: 
{"timestamp":"1464711665886","hostname":"localhost","type":"cassandra","level":"INFO","message":"Loading settings from file:/home/hadoop/software/cassandra-2.1.14/conf/cassandra.yaml"}
 * 
 * [D] Prerequistes: 
- Copy src/main/resources/grok/patterns/log.grok file into ${FLUME_HOME}/conf dir
- Copy dependencies named-regexp-0.2.3.jar, grok-0.1.4.jar, commons-lang3-3.1.jar and jackson-core-2.6.2.jar into ${FLUME_HOME}/lib dir
- Resolve the dependencies conflicts with Flume and ES: jackson-core-asl-1.9.3.jar 
 * 
 *
 * @author liuqiyun
 *
 */
public class LogFileSource extends AbstractSource implements EventDrivenSource,
		Configurable, GrokConstants, LogConstants {

	private static final Logger logger = LoggerFactory
			.getLogger(LogFileSource.class);

	private String logFilePath;
	private int bufferCount;
	private long batchTimeout;
	private Charset charset;
	private static String grokPattern;
	private static String logType;
	private static String subType1;
	private static String subType2;
	private static String subType3;
	private ExecutorService executor;
	private LogFileRunnable runner;
	private Future<?> runnerFuture;

	@Override
	public void configure(Context context) {
		logFilePath = context
				.getString(LogFileConfigurationConstants.CONFIG_LOG_FILE_PATH);
		bufferCount = context.getInteger(
				LogFileConfigurationConstants.CONFIG_BATCH_SIZE,
				LogFileConfigurationConstants.DEFAULT_BATCH_SIZE);
		batchTimeout = context.getLong(
				LogFileConfigurationConstants.CONFIG_BATCH_TIME_OUT,
				LogFileConfigurationConstants.DEFAULT_BATCH_TIME_OUT);
		charset = Charset.forName(context.getString(
				LogFileConfigurationConstants.CHARSET,
				LogFileConfigurationConstants.DEFAULT_CHARSET));
		grokPattern = context
				.getString(LogFileConfigurationConstants.CONFIG_GROK_PATTERN);
		logType = context
				.getString(LogFileConfigurationConstants.CONFIG_LOG_TYPE);
		subType1=context.getString(LogFileConfigurationConstants.CONFIG_SUBLOG1_TYPE,null);
		subType2=context.getString(LogFileConfigurationConstants.CONFIG_SUBLOG2_TYPE,null);
		subType3=context.getString(LogFileConfigurationConstants.CONFIG_SUBLOG3_TYPE,null);
	}

	@Override
	public void start() {
		logger.info("Log File source starting with logFilePath:{}", logFilePath);

		executor = Executors.newSingleThreadExecutor();
		runner = new LogFileRunnable(logFilePath, getChannelProcessor(),
				bufferCount, batchTimeout, charset);

		runnerFuture = executor.submit(runner);

		super.start();

		logger.debug("Exec source started");
	}

	@Override
	public void stop() {
		logger.info("Stopping Log File source with logFilePath:{}", logFilePath);

		if (runner != null) {
			runner.kill();
		}

		if (runnerFuture != null) {
			logger.debug("Stopping exec runner");
			runnerFuture.cancel(true);
			logger.debug("Exec runner stopped");
		}
		executor.shutdown();

		while (!executor.isTerminated()) {
			logger.debug("Waiting for exec executor service to stop");
			try {
				executor.awaitTermination(500, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				logger.debug("Interrupted while waiting for exec executor service "
						+ "to stop. Just exiting.");
				Thread.currentThread().interrupt();
			}
		}
		super.stop();

		logger.debug("Log File source with logFilePath:{} stopped. Metrics:{}",
				logFilePath);
	}

	private static class LogFileRunnable implements Runnable {

		private final String logFilePath;
		private final ChannelProcessor channelProcessor;
		private SystemClock systemClock = new SystemClock();
		private Long lastPushToChannel = systemClock.currentTimeMillis();
		private final int bufferCount;
		private final long batchTimeout;
		private final Charset charset;
		ScheduledExecutorService timedFlushService;
		ScheduledFuture<?> future;

		public LogFileRunnable(String logFilePath,
				ChannelProcessor channelProcessor, int bufferCount,
				long batchTimeout, Charset charset) {
			this.logFilePath = logFilePath;
			this.channelProcessor = channelProcessor;
			this.bufferCount = bufferCount;
			this.batchTimeout = batchTimeout;
			this.charset = charset;
		}

		@Override
		public void run() {
			final List<Event> eventList = new ArrayList<Event>();
			// timedFlushService targets to flush received data from source to
			// channel
			timedFlushService = Executors
					.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
							.setNameFormat(
									"timedFlushExecService"
											+ Thread.currentThread().getId()
											+ "-%d").build());

			future = timedFlushService.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					try {
						synchronized (eventList) {
							if (!eventList.isEmpty() && timeout()) {
								flushEventBatch(eventList);
							}
						}
					} catch (Exception e) {
						logger.error(
								"Exception occured when processing event batch",
								e);
						if (e instanceof InterruptedException) {
							Thread.currentThread().interrupt();
						}
					}
				}
			}, batchTimeout, batchTimeout, TimeUnit.MILLISECONDS);

			try {
				tailFile(eventList,4000,4096);


			} catch (Exception e) {
				e.printStackTrace();
			}


		}
		public void tailFile(List<Event> eventList,long delayMillis,int bufSize) throws Exception {

			File file = new File(logFilePath);
			Tailer tailer = new Tailer(file, new TailerListenerAdapter() {
				@Override
				public void fileNotFound() {
					System.out.println("文件没有找到");
					super.fileNotFound();
				}
				@Override
				public void fileRotated() {
				}
				String lastLine;
				String preLastLine;
				String preLastLineGrokMatchResult = DEFAULT_GROK_NOT_MATCH_RESULT;
				String lineSeparator = System.getProperty("line.separator");
				@Override
				public void handle(String line) { //增加的文件的内容
					//System.out.println("文件line:" + line);

					GrokBean gb = new GrokBean(LogFileSource.class.getResource(
							File.separator).getPath()
							+ DEFAULT_GROK_PATTERN_FILE_PATH, grokPattern);
					lastLine=line;
					if (lastLine != null && !lastLine.equals("")) {
						// TODO need to add max waiting time to flush
						// the last line
						String lastLineGrokMatchResult = gb
								.matchLog(lastLine).toJson(true)
								.orElse("");
						if (!lastLineGrokMatchResult
								.equals(DEFAULT_GROK_NOT_MATCH_RESULT)) {
							if (preLastLine != null
									&& !preLastLineGrokMatchResult
									.equals(DEFAULT_GROK_NOT_MATCH_RESULT)) {
								// Need to compute
								// preLastLineGrokMatchResult again as
								// the preLastLine is might already
								// changed.
								preLastLineGrokMatchResult = gb
										.matchLog(preLastLine)
										.toJson(true).orElse("");
								CommonLog commLog = gb.matchLog(
										preLastLine).getObject(
										CommonLog.class);
								// Add preLastLine to a event
								// Add the new added log line as a new
								// event
								eventList.add(EventBuilder
										.withBody(convertToJsonStr(
												commLog).getBytes(
												charset)));
								if (eventList.size() >= bufferCount
										|| timeout()) {
									flushEventBatch(eventList);
								}
							}
							preLastLine = lastLine;
							preLastLineGrokMatchResult = lastLineGrokMatchResult;

						} else {
							// If preLastLine is a matched log line
							if (preLastLine != null
									&& !preLastLineGrokMatchResult
									.equals(DEFAULT_GROK_NOT_MATCH_RESULT)) {
								preLastLine = preLastLine
										+ lineSeparator + lastLine;
							}
						}
					}

					super.handle(line);
				}
				@Override
				public void handle(Exception ex) {
					ex.printStackTrace();
					super.handle(ex);
				}
			}, delayMillis, true,bufSize);

			new Thread(tailer).start();

		}

		private String convertToJsonStr(CommonLog commLog) {
			LinkedHashMap<String, String> finalJsonMap = new LinkedHashMap<String, String>();
			finalJsonMap.put("timestamp", getTimeStringFromGrokOutput(commLog));
			finalJsonMap.put("hostname", HostNameUtil.getHostName());
			finalJsonMap.put("type", logType);
			finalJsonMap.put("subType1", subType1);
			finalJsonMap.put("subType2", subType2);
			finalJsonMap.put("subType3", subType3);
			finalJsonMap.put("logFilePath", logFilePath);
			finalJsonMap.put("level", commLog.getLoglevel());
			finalJsonMap.put("message", commLog.getLogmessage());
			return JsonUtil.getJacksonString(finalJsonMap);
		}

		/**
		 * Will construct a time format like "yyyy-MM-dd'T'HH:mm:ss.SSSX",
		 * basing on Grok output
		 * 
		 * 
		 * @param
		 * @return
		 */
		private static String getTimeStringFromGrokOutput(CommonLog commonLog) {
			StringBuffer time = new StringBuffer();
			time.append(commonLog.getYear());
			time.append("-");
			time.append(commonLog.getMonth());
			time.append("-");
			time.append(commonLog.getDay());
			time.append("T");
			time.append(commonLog.getHour());
			time.append(":");
			time.append(commonLog.getMinute());
			time.append(":");
			time.append(commonLog.getSecond());
			time.append(".");
			time.append(commonLog.getMills());
			time.append("Z");

			return TimeUtil.formatTZ(time.toString());
		}

		private boolean timeout() {
			return (systemClock.currentTimeMillis() - lastPushToChannel) >= batchTimeout;
		}

		private void flushEventBatch(List<Event> eventList) {
			channelProcessor.processEventBatch(eventList);
			eventList.clear();
		}

		public void kill() {
			if (timedFlushService != null) {
				timedFlushService.shutdown();
				while (!timedFlushService.isTerminated()) {
					try {
						timedFlushService.awaitTermination(500,
								TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						logger.debug("Interrupted while waiting for exec executor service "
								+ "to stop. Just exiting.");
						Thread.currentThread().interrupt();
					}
				}
			}
		}

	}

	@ToString
	@Getter
	@Setter
	public static class CommonLog {
		String logmessage;
		String loglevel;
		String year;
		String month;
		String day;
		String hour;
		String minute;
		String second;
		String mills;
	}

}
