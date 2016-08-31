package com.oneapm.log.agent.flume.interceptor.akka;

import com.oneapm.log.agent.flume.interceptor.Constants;
import com.oneapm.log.agent.flume.metric.akka.AkkaCheckerMetrics;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.constants.OverallConstants;
import com.oneapm.log.common.utils.StringUtil;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Iterator;
import java.util.List;


public class AkkaLogInterceptor implements Interceptor, Constants,OverallConstants {

	public static final String AKKA_METRIC_TYPES = "AkkaMetricTypes";
	public static final String DEFAULT_AKKA_METRIC_TYPE = AkkaCheckerMetrics.AKKA_COMMIT_BATCH_CHECKER.getAkkaCheckerContext();
	private static final Logger logger = LoggerFactory.getLogger(AkkaLogInterceptor.class);

	private String[] akkaMetrics;

	public AkkaLogInterceptor(String akkaMetricTypes) {
		this.akkaMetrics = StringUtil.getArray(akkaMetricTypes, INTERCEPTOR_DEFAULT_METRICS_SEPERATOR);
	}

	public void close() {
		// NO-OPERATION
	}
	public void initialize() {
		// NO-OPERATION
	}

	/**
	 * The method is the intercept for filter
	 * @param event
	 * @return
	 * The result is the data after filtering
     */

	public Event intercept(Event event) {

		logger.debug("intercepting single event...");
		String eventBodyStr = new String(event.getBody(),DEFAULT_CHARSET);
		String matchedMetric = StringUtil.containStr(eventBodyStr, akkaMetrics);
		if (matchedMetric == null || eventBodyStr.isEmpty()) {
			return null;
		}
		Parser parser = null;
		parser = LogParserFactory.getInstance().getParser(matchedMetric);
		if (parser != null) {
			String parserResult = parser.parser(eventBodyStr);
			event.setBody(parserResult.getBytes(DEFAULT_CHARSET));
		} else {
			event.setBody("NO parser is found!".getBytes(DEFAULT_CHARSET));
		}
		return event;
	}

	/**
	 * Remove the events if the event is null
	 * @param events
	 * @return
     */

	public List<Event> intercept(List<Event> events) {
		logger.debug("intercepting list of events...");
		for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
			Event next = iterator.next();
			Event current=intercept(next);
			if (current == null) {
				iterator.remove();
			}
		}
		return events;
	}

	public static class AkkaLogInterceptorBuilder implements Builder {
		private String akkaMetricTypes;
		public void configure(Context context) {
			akkaMetricTypes = context.getString(AKKA_METRIC_TYPES, DEFAULT_AKKA_METRIC_TYPE);
		}
		public Interceptor build() {
			return new AkkaLogInterceptor(akkaMetricTypes);
		}

	}

}
