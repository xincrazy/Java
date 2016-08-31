package com.oneapm.log.agent.flume.interceptor.druid.v073;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.Constants;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.utils.InterceptorEventUtil;
import com.oneapm.log.common.utils.StringUtil;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DruidLogInterceptor implements Interceptor, Constants {
	private static final Logger logger             = LoggerFactory.getLogger(DruidLogInterceptor.class);
	private static final String DRUID_METRIC_TYPES = "druidMetricTypes";
	private static final String TOLERABLE_REQUEST_TIME = "tolerableRequestTime";
	private static final String DEFAULT_TOLERABLE_REQUEST_TIME = "500";
	private static final String DRUID_NODE = "druidNode";
	private static final String DEFAULT_DRUID_METRIC_TYPE = "events/thrownAway";
	private Map<String, String> paramMap = new HashMap<>();
	private String[] druidMetrics;

	public DruidLogInterceptor(String druidMetricTypes, String druidNode, String tolerableRequestTime) {
		this.druidMetrics = StringUtil.getArray(druidMetricTypes, INTERCEPTOR_DEFAULT_METRICS_SEPERATOR);
		this.paramMap.put("druidNode", druidNode);
		this.paramMap.put("tolerableRequestTime", tolerableRequestTime);
	}

	public void close() {
		// NO-OPERATION

	}

	public void initialize() {
		// NO-OPERATION

	}

	public Event intercept(Event event) {
		// Sample input data:
		// 2016-05-11 09:14:36,641 INFO c.m.e.c.LoggingEmitter
		// [MonitorScheduler-0] Event
		// [{"feed":"metrics","timestamp":"2016-05-11T09:14:36.640Z","service":"realtime","host":"10.165.113.43:8084","metric":"events/thrownAway","value":8889,"user2":"druid_metric_1day"}]

		String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
		logger.debug("event before parsing: " + eventBodyStr);
		String matchedMetric = StringUtil.containStr(eventBodyStr, druidMetrics);

		Parser parser = null;

		if (matchedMetric != null) {
			try {
				parser = LogParserFactory.getInstance().getParserByClass(this.getClass(), paramMap);
			} catch (Exception e) {
				logger.error("error when getting parser by class." + e.getMessage());
			}
		}

		if (parser != null) {
			String parserResult = parser.parser(eventBodyStr);
			event.setBody(parserResult.getBytes(Charsets.UTF_8));
			logger.debug("event after parsing: " + parserResult);
		} else {
			logger.debug("no parser is found.");
			return null;
		}

		return event;
	}

	public List<Event> intercept(List<Event> events) {
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

	public static class DruidLogInterceptorBuilder implements Interceptor.Builder {
		private String druidMetricTypes;
		private String druidNode;
		private String tolerableRequestTime;

		public void configure(Context context) {
			druidMetricTypes = context.getString(DRUID_METRIC_TYPES, DEFAULT_DRUID_METRIC_TYPE);
			tolerableRequestTime = context.getString(TOLERABLE_REQUEST_TIME, DEFAULT_TOLERABLE_REQUEST_TIME);
			druidNode = context.getString(DRUID_NODE);
			logger.debug("druidMetricTypes: " + druidMetricTypes);
			logger.debug("druidNode: " + druidNode);
		}

		public Interceptor build() {
			logger.debug("inside DruidLogInterceptroBuilder");
			return new DruidLogInterceptor(druidMetricTypes, druidNode, tolerableRequestTime);
		}
	}
}
