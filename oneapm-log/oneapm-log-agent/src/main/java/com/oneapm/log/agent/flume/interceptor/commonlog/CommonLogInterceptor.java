package com.oneapm.log.agent.flume.interceptor.commonlog;

import com.oneapm.log.agent.flume.interceptor.Constants;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.constants.OverallConstants;
import com.oneapm.log.common.utils.StringUtil;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * CommonMetric have TRACE DEBUG INFO WARN ERROR FATAL ALL OFF
 */

public class CommonLogInterceptor implements Interceptor, Constants,OverallConstants {

	public static final String COMMON_METRIC_TYPES = "CommonMetricTypes";
	public static final String DEFAULT_COMMON_METRIC_TYPE = "INFO";
	private static final Logger logger = LoggerFactory.getLogger(CommonLogInterceptor.class);
	Map<String,String> map = new HashMap<>();
	private String[] commonMetrics;

	public CommonLogInterceptor(String commonMetricTypes) {
		this.commonMetrics = StringUtil.getArray(commonMetricTypes, INTERCEPTOR_DEFAULT_METRICS_SEPERATOR);
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
		String matchedMetric = StringUtil.containStr(eventBodyStr, commonMetrics);
		if (matchedMetric == null || eventBodyStr.isEmpty()) {
			return null;
		}
		Parser parser = null;
		try {
			parser= LogParserFactory.getInstance().getParserByClass(this.getClass(),map);
			if (parser != null) {
				String parserResult = parser.parser(eventBodyStr);
				event.setBody(parserResult.getBytes(DEFAULT_CHARSET));
			} else {
				event.setBody("NO parser is found!".getBytes(DEFAULT_CHARSET));
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	public static class CommonLogInterceptorBuilder implements Builder {
		private String CommonMetricTypes;
		public void configure(Context context) {
			CommonMetricTypes = context.getString(COMMON_METRIC_TYPES, DEFAULT_COMMON_METRIC_TYPE);
		}
		public Interceptor build() {
			return new CommonLogInterceptor(CommonMetricTypes);
		}

	}

}
