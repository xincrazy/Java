package com.oneapm.log.agent.flume.interceptor.cassandra.v37;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.Constants;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.utils.InterceptorEventUtil;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CassandraLogInterceptor implements Interceptor, Constants {
	public static final String METRIC_TYPE = "metricType";
	public static final String CASSANDRA_PATH = "cassandraPath";
	private static final Logger logger             = LoggerFactory.getLogger(CassandraLogInterceptor.class);
	private Map<String, String> paramMap = new HashMap<>();

	public CassandraLogInterceptor(String metricType, String cassandraPath) {
		this.paramMap.put("metricType", metricType);
		this.paramMap.put("version", "37");
		this.paramMap.put("cassandraPath", cassandraPath);
	}

	public void close() {
		// NO-OPERATION

	}

	public void initialize() {
		// NO-OPERATION

	}

	/**
	 * Intercept a list of flume {@link Event}s which are read from the cassandra log.
	 *
	 * @param events Input list of events to be intercepted
	 *
	 * @return
	 *      A list of intercepted flume {@link Event}s.
	 */
	public List<Event> intercept(List<Event> events) {
		logger.debug("intercepting list of events...");
		List<Event> eventList = new ArrayList<>();
		for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
			Event next = iterator.next();
			Event current = intercept(next);

			if (current == null) {
				iterator.remove();
			}else {
				eventList.addAll(InterceptorEventUtil.eventTransformer(current));
			}

		}
		return eventList;
	}

	/**
	 * Intercept a single flume {@link Event} which is read from the tomcat log and intercepted as a key value json format.
	 *
	 * @param event Event to be intercepted
	 * @return
	 *      a intercepted flume {@link Event}.
	 */
	public Event intercept(Event event) {
		// Sample input data:
		// 10.128.6.60 - - [04/Jul/2016:10:51:44 +0800] "GET /E_Menu/userlogin/restaurant.action HTTP/1.1" 200 6736 32
		logger.debug("intercepting single event...");
		String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
		if (eventBodyStr.contains("t,") || eventBodyStr.isEmpty()) {
			return null;
		}
		logger.debug("Event processed by interceptor CassandraLogInterceptor: " + eventBodyStr);

		Parser parser = null;
		try {
			parser = LogParserFactory.getInstance().getParserByClass(this.getClass(), paramMap);
		} catch (Exception e) {
			logger.error("error when getting parser by class." + e.getMessage());
		}
		if (parser != null) {
			String parserResult = parser.parser(eventBodyStr);;
			event.setBody(parserResult.getBytes(Charsets.UTF_8));
			return event;
		}
		else {
			logger.debug("parser not found...");
			return null;
		}
	}

	/**
	 * The builder of {@link CassandraLogInterceptor}. This is used automatically through flume configuration.
	 */
	public static class CassandraLogInterceptorBuilder implements Builder {
		private static final Logger logger             = LoggerFactory.getLogger(CassandraLogInterceptorBuilder.class);
		private String metricType;
		private String cassandraPath;
		public void configure(Context context) {
			logger.debug("context: " + context);
			this.metricType = context.getString(METRIC_TYPE);
			this.cassandraPath = context.getString(CASSANDRA_PATH);
		}

		public Interceptor build() {
			logger.debug("building CassandraLogInterceptor...");
			return new CassandraLogInterceptor(metricType, cassandraPath);
		}
	}
}
